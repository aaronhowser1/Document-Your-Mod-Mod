package com.aaronhowser1.documentmod.json;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.config.DYMMConfig;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ProgressManager;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleLoader;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;

public enum DocumentationLoader {
    INSTANCE;

    private static final String CONDITIONS_KEY = "conditions";

    private static final Map<String, Boolean> CONFIGURATION_OPTION_CACHE = Maps.newHashMap();
    private static final Map<String, ConditionFactory> CONDITION_FACTORIES = Maps.newHashMap();

    private ModContainer thisModContainer = null;
    private ProgressManager.ProgressBar bar = null;

    static {
        CONDITION_FACTORIES.put("dym:configuration_option", (object, modContainer) -> {
            final String name = JsonUtils.getString(object, "name");
            final boolean value = JsonUtils.getBoolean(object, "value");
            final boolean actualValue = CONFIGURATION_OPTION_CACHE.computeIfAbsent(name, s -> {
                try {
                    final Class<?> configClass = DYMMConfig.class;
                    Object grabbingObject;
                    Field grabbingField;
                    if (name.contains(".")) {
                        final String[] path = name.split(Pattern.quote("."));
                        if (path.length != 2) throw new JsonSyntaxException("Configuration option must be at most one level deep");
                        final Field tmpField = configClass.getDeclaredField(path[0]);
                        final Object tmpObject = tmpField.get(null);
                        final Class<?> tmpClass = tmpObject.getClass();
                        grabbingObject = tmpObject;
                        grabbingField = tmpClass.getDeclaredField(path[1]);
                    } else {
                        grabbingObject = null;
                        grabbingField = configClass.getDeclaredField(name);
                    }
                    final Boolean configStatus = (Boolean) grabbingField.get(grabbingObject);
                    if (configStatus == null) throw new ReflectiveOperationException();
                    return configStatus;
                } catch (final ReflectiveOperationException e) {
                    throw new JsonParseException("Given configuration option '" + name + "' does not exist", e);
                }
            });
            return () -> value == actualValue;
        });
        CONDITION_FACTORIES.put("quark:check_feature", (object, modContainer) -> {
            if (Loader.isModLoaded("quark")) return () -> false;
            final String className = JsonUtils.getString(object, "class");
            try {
                final Class<?> maybeClass = Class.forName(className);
                final Class<?> featureClass = Class.forName("vazkii.quark.base.module.Feature");
                if (!featureClass.isAssignableFrom(maybeClass)) throw new JsonParseException("Given class is not a feature class");
                //noinspection unchecked
                return () -> ModuleLoader.isFeatureEnabled((Class<? extends Feature>) maybeClass);
            } catch (final ReflectiveOperationException e) {
                throw new JsonParseException("Given class name does not exist: " + className);
            }
        });
        CONDITION_FACTORIES.put("quark:check_module", (object, modContainer) -> {
            if (Loader.isModLoaded("quark")) return () -> false;
            final String className = JsonUtils.getString(object, "class");
            try {
                final Class<?> maybeClass = Class.forName(className);
                final Class<?> moduleClass = Class.forName("vazkii.quark.base.module.Module");
                if (!moduleClass.isAssignableFrom(maybeClass)) throw new JsonParseException("Given class is not a feature class");
                //noinspection unchecked
                return () -> ModuleLoader.isModuleEnabled((Class<? extends Module>) maybeClass);
            } catch (final ReflectiveOperationException e) {
                throw new JsonParseException("Given class name does not exist: " + className);
            }
        });
    }

    public void loadFromJson() {
        DocumentationRegistry.INSTANCE.wipe();
        DocumentMod.logger.info("Reading JSON archive for mod documentation");
        this.bar = ProgressManager.push("Reading JSON documentation", Loader.instance().getActiveModList().size());
        this.thisModContainer = Loader.instance().getActiveModList().stream().filter(item -> DocumentMod.MODID.equals(item.getModId())).findFirst().orElse(null);
        Loader.instance().getActiveModList().forEach(this::loadModDocumentation);
        ProgressManager.pop(this.bar);
        this.bar = null;
        this.thisModContainer = null;
    }

    private void loadModDocumentation(@Nonnull final ModContainer modContainer) {
        this.bar.step(modContainer.getName());
        DocumentMod.logger.debug("Attempting to lookup mod documentation for mod " + modContainer.getName() + " (" + modContainer + ")");
        final String jsonFilesDirectory = "assets/" + modContainer.getModId() + "/dym";
        final File sourceFile = this.thisModContainer.getSource();

        FileSystem fileSystem = null;
        try {
            Path root = null;

            if (sourceFile.isFile()) {
                // This is a JAR mod, so we need to build a FileSystem that allows us to walk through the JAR
                // like we would do with a normal directory
                fileSystem = FileSystems.newFileSystem(sourceFile.toPath(), null);
                root = fileSystem.getPath("/" + jsonFilesDirectory);
            } else if (sourceFile.isDirectory()) {
                // We are in a development environment, so the mod's source is actually src/main
                // We can traverse it like normal
                root = sourceFile.toPath().resolve(jsonFilesDirectory);
            }

            if (root == null || !Files.exists(root)) {
                DocumentMod.logger.trace("No documentation directory exists, skipping");
                return;
            }

            final Path finalRoot = root.toFile().toPath();

            Files.walk(root).forEach(file -> this.loadJsonFile(modContainer, file, finalRoot));
        } catch (IOException e) {
            DocumentMod.logger.warn("An error has occurred while attempting to read the JSON file for candidate " + modContainer);
            DocumentMod.logger.warn("This will now be skipped. The exception and the relevant stacktrace will be printed to STDERR");
            e.printStackTrace(System.err);
        } finally {
            IOUtils.closeQuietly(fileSystem);
        }
    }

    private void loadJsonFile(@Nonnull final ModContainer modContainer, @Nonnull final Path path, @Nonnull final Path root) {
        final Path relativePath = root.relativize(path);
        if (!"json".equalsIgnoreCase(FilenameUtils.getExtension(relativePath.toString()))) {
            DocumentMod.logger.debug("Found non-json file in " + relativePath + ". Skipping");
            return;
        }
        // This is the example file we have built, there is no reason to load it
        if (relativePath.toString().contains("_schema.json") && DocumentMod.MODID.equals(modContainer.getModId())) {
            DocumentMod.logger.debug("Skipping schema loading in " + relativePath + " for mod id " + DocumentMod.MODID);
            return;
        }

        final String name = FilenameUtils.removeExtension(relativePath.toString()).replaceAll("\\\\", "/");
        final ResourceLocation resourceLocation = new ResourceLocation(modContainer.getModId(), name);

        try (final BufferedReader reader = Files.newBufferedReader(path)) {
            final JsonObject jsonObject = JsonUtils.fromJson(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(), reader, JsonObject.class);
            if (jsonObject == null) return;
            if (!this.processLoadingConditions(jsonObject, modContainer)) {
                return;
            }
            ModDocumentation.create(jsonObject, resourceLocation).forEach(documentation -> {
                DocumentationRegistry.INSTANCE.registerForMod(modContainer.getModId(), documentation);
            });
        } catch (final JsonParseException e) {
            DocumentMod.logger.error("An error has occurred while attempting to parse JSON for file " + path);
            DocumentMod.logger.error("Resource location where the issue happened: " + resourceLocation);
            DocumentMod.logger.error("The stacktrace will be printed to STDERR");
            e.printStackTrace(System.err);
        } catch (final IOException e) {
            DocumentMod.logger.warn("An error has occurred while attempting to read file " + path);
            DocumentMod.logger.warn("This file will be skipped. The exception will be printed now to STDERR");
            e.printStackTrace(System.err);
        }
    }

    private boolean processLoadingConditions(@Nonnull final JsonObject jsonObject, @Nonnull final ModContainer modContainer) {
        return !jsonObject.has(CONDITIONS_KEY) || this.processLoadingConditions(JsonUtils.getJsonArray(jsonObject, CONDITIONS_KEY), modContainer);
    }

    private boolean processLoadingConditions(@Nonnull final JsonArray jsonArray, @Nonnull final ModContainer modContainer) {
        for (final JsonElement jsonElement : jsonArray) {
            if (!jsonElement.isJsonObject()) throw new JsonSyntaxException("conditions member must be a JsonObject");
            final ConditionFactory conditionFactory = this.getConditionFactory(jsonElement.getAsJsonObject());
            final BooleanSupplier booleanSupplier = conditionFactory.produce(jsonElement.getAsJsonObject(), modContainer);
            if (!booleanSupplier.getAsBoolean()) return false;
        }
        return true;
    }

    private ConditionFactory getConditionFactory(@Nonnull final JsonObject jsonObject) {
        final String type = JsonUtils.getString(jsonObject, "type");
        if (type.trim().isEmpty()) throw new JsonSyntaxException("Condition type cannot be blank");
        final ConditionFactory conditionFactory = CONDITION_FACTORIES.get(type);
        if (conditionFactory == null) throw new JsonParseException("Condition type given '" + type + "' is not known");
        return conditionFactory;
    }
}
