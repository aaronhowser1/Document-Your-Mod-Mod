package com.aaronhowser1.documentmod.json;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.ModId;
import com.aaronhowser1.documentmod.config.DYMMConfig;
import com.aaronhowser1.documentmod.json.conditions.DocumentModConfigurationOption;
import com.aaronhowser1.documentmod.json.factory.Factory;
import com.aaronhowser1.documentmod.quark.QuarkBooleanFieldCheckerConditionFactory;
import com.aaronhowser1.documentmod.quark.QuarkFeatureCheckingConditionFactory;
import com.aaronhowser1.documentmod.quark.QuarkModuleCheckingConditionFactory;
import com.aaronhowser1.documentmod.utility.TriConsumer;
import com.aaronhowser1.documentmod.utility.TriPredicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public enum DocumentationLoader {
    INSTANCE;

    private static final String CONDITIONS_KEY = "conditions";

    private static final Map<Class<? extends Factory<?>>, Map<ResourceLocation, Factory<?>>> FACTORIES = Maps.newHashMap();
    private static final Map<ResourceLocation, ConditionFactory> CONDITION_FACTORIES = Maps.newHashMap();

    private IForgeRegistry<ModDocumentation> registry = null;
    private ModContainer thisModContainer = null;
    private ProgressManager.ProgressBar bar = null;

    static {
        CONDITION_FACTORIES.put(new ResourceLocation(ModId.DOCUMENT_YOUR_MOD_MOD, "configuration_option"), new DocumentModConfigurationOption());
        CONDITION_FACTORIES.put(new ResourceLocation(ModId.QUARK, "check_feature"), new QuarkFeatureCheckingConditionFactory());
        CONDITION_FACTORIES.put(new ResourceLocation(ModId.QUARK, "check_module"), new QuarkModuleCheckingConditionFactory());
        CONDITION_FACTORIES.put(new ResourceLocation(ModId.QUARK, "check_boolean_field"), new QuarkBooleanFieldCheckerConditionFactory());
    }

    public void loadAndRegister(@Nonnull final IForgeRegistry<ModDocumentation> registry) {
        this.registry = registry;
        this.loadFromJson();
        this.registry = null;
    }

    private void loadFromJson() {
        DocumentMod.logger.info("Attempting to load data-driven mod documentation");
        this.loadFactoriesFromJson();
        this.loadDocumentationFromJson();
    }

    private void loadFactoriesFromJson() {
        DocumentMod.logger.info("Finding and registering factories");
        this.bar = ProgressManager.push("Loading JSON factories", Loader.instance().getActiveModList().size());
        this.thisModContainer = Loader.instance().getActiveModList().stream().filter(item -> DocumentMod.MODID.equals(item.getModId())).findFirst().orElse(null);
        Loader.instance().getActiveModList().forEach(this::loadFactoriesForMod);
        ProgressManager.pop(this.bar);
        this.bar = null;
        this.thisModContainer = null;
        DocumentMod.logger.info("All JSON factories found have been loaded");
        this.dumpLoadedFactories();
    }

    private void loadFactoriesForMod(@Nonnull final ModContainer modContainer) {
        this.bar.step(modContainer.getName());
        DocumentMod.logger.debug("Attempting to load factories for mod " + modContainer.getName() + " (" + modContainer + ")");
        DocumentMod.logger.trace(" Attempt #1: Loading from mod's container");
        this.loadFactoriesForSource(modContainer, modContainer.getSource());

        if (!Objects.equals(modContainer, this.thisModContainer)) {
            DocumentMod.logger.trace(" Attempt #2: Loading from our own container");
            this.loadFactoriesForSource(modContainer, this.thisModContainer.getSource());
        } else {
            DocumentMod.logger.trace(" Attempt #2 skipped because it is our mod");
        }
    }

    private void loadFactoriesForSource(@Nonnull final ModContainer container, @Nonnull final File source) {
        this.findDirectoryAndWalk(container, source, (path, root) -> this.loadFactoriesFromFile(container, path, root));
    }

    private void loadFactoriesFromFile(@Nonnull final ModContainer container, @Nonnull final Path path, @Nonnull final Path root) {
        this.loadJsonFile(container, path, root,
                (modContainer, relativePath, name) -> Objects.equals(name, "_factories"),
                (modContainer, jsonObject, resourceLocation) -> this.loadFactories(Objects.requireNonNull(modContainer), Objects.requireNonNull(jsonObject)));
    }

    private void loadFactories(@Nonnull final ModContainer container, @Nonnull final JsonObject object) {
        DocumentMod.logger.trace("Found factories file for mod " + container.getModId() + ". Proceeding with loading now");
        // TODO
        DocumentMod.logger.error("TODO " + container);
    }

    private void dumpLoadedFactories() {
        final Consumer<String> consumer = DYMMConfig.debugModIsDocumented ? DocumentMod.logger::info : DocumentMod.logger::trace;

        consumer.accept("Dumping factories: ");
        FACTORIES.forEach((type, factories) -> {
            consumer.accept("    " + type + ":");
            factories.forEach((name, factory) -> consumer.accept("        " + name + " -> " + factory));
        });
    }

    private void loadDocumentationFromJson() {
        DocumentMod.logger.info("Reading JSON archive for mod documentation");
        this.bar = ProgressManager.push("Reading JSON documentation", Loader.instance().getActiveModList().size());
        this.thisModContainer = Loader.instance().getActiveModList().stream().filter(item -> DocumentMod.MODID.equals(item.getModId())).findFirst().orElse(null);
        Loader.instance().getActiveModList().forEach(this::loadModDocumentation);
        ProgressManager.pop(this.bar);
        this.bar = null;
        this.thisModContainer = null;
        DocumentMod.logger.info("Done reading JSON archive");
    }

    private void loadModDocumentation(@Nonnull final ModContainer modContainer) {
        this.bar.step(modContainer.getName());

        // Since setRegistryName spams when it finds a prefix that is not the one of the current mod container
        // and we don't want that because it's us who are doing the registering for all mods and we do not
        // care about weird prefixes, I'll use the usual hacky method of setting the mod container to the
        // one we are currently doing registration with
        final ModContainer previous = Loader.instance().activeModContainer();
        Loader.instance().setActiveModContainer(modContainer);

        // And now let's keep going according to business
        DocumentMod.logger.debug("Attempting to lookup mod documentation for mod " + modContainer.getName() + " (" + modContainer + ")");
        final List<ModDocumentation> documentation = Lists.newArrayList();
        DocumentMod.logger.trace(" Attempt #1: Loading from mod's container");
        this.loadModDocumentationForSource(modContainer, modContainer.getSource(), documentation);

        if (Objects.equals(modContainer, this.thisModContainer)) {
            DocumentMod.logger.trace("Attempt #2 skipped because it is our mod");
        } else {
            DocumentMod.logger.trace(" Attempt #2: Loading from our own container");
            this.loadModDocumentationForSource(modContainer, this.thisModContainer.getSource(), documentation);
        }

        documentation.forEach(it -> this.registry.register(it));

        // And let's revert things so that it appears that nothing has been done
        Loader.instance().setActiveModContainer(previous);
    }

    private void loadModDocumentationForSource(@Nonnull final ModContainer container, @Nonnull final File sourceFile, @Nonnull final List<ModDocumentation> list) {
        this.findDirectoryAndWalk(container, sourceFile, (path, root) -> this.loadJsonDocumentationFile(container, path, root, list));
    }

    private void loadJsonDocumentationFile(@Nonnull final ModContainer modContainer, @Nonnull final Path path, @Nonnull final Path root, @Nonnull final List<ModDocumentation> list) {
        this.loadJsonFile(modContainer, path, root, (container, relativePath, name) -> {
            if (Objects.isNull(container) || Objects.isNull(relativePath) || Objects.isNull(name)) return false;
            // This is the example file we have built, there is no reason to load it
            if (name.startsWith("_")) {
                DocumentMod.logger.debug("Skipping loading of file " + relativePath + " for mod id " + container.getModId());
                return false;
            }
            return true;
        }, (container, jsonObject, resourceLocation) -> {
            if (Objects.isNull(container) || Objects.isNull(jsonObject) || Objects.isNull(resourceLocation)) return;

            if (!this.processLoadingConditions(jsonObject, container)) {
                return;
            }
            final ModDocumentation doc = ModDocumentation.create(jsonObject, resourceLocation);
            if (doc == null) return;
            if (doc.getRegistryName() == null) {
                DocumentMod.logger.error("Found null name for mod documentation object identified by " + resourceLocation + ". This will cause errors later on!");
            }
            list.add(doc);
        });
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
        if (type.indexOf(':') == -1) throw new JsonSyntaxException("Missing namespace for the condition type");
        final ConditionFactory conditionFactory = CONDITION_FACTORIES.get(new ResourceLocation(type));
        if (conditionFactory == null) throw new JsonParseException("Condition type given '" + type + "' is not known");
        return conditionFactory;
    }

    private void findDirectoryAndWalk(@Nonnull final ModContainer container, @Nonnull final File sourceFile, @Nonnull final BiConsumer<Path, Path> biConsumer) {
        final String jsonFilesDirectory = "assets/" + container.getModId() + "/dym";
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

            final Path finalRoot = root;

            Files.walk(root).forEach(path -> biConsumer.accept(path, finalRoot));
        } catch (IOException e) {
            DocumentMod.logger.warn("An error has occurred while attempting to walk the candidate " + container);
            DocumentMod.logger.warn("This will now be skipped. The exception and the relevant stacktrace will be printed to STDERR");
            e.printStackTrace(System.err);
        } finally {
            IOUtils.closeQuietly(fileSystem);
        }
    }

    private void loadJsonFile(@Nonnull final ModContainer container, @Nonnull final Path path, @Nonnull final Path root,
                              @Nonnull final TriPredicate<ModContainer, Path, String> fileNameFilter,
                              @Nonnull final TriConsumer<ModContainer, JsonObject, ResourceLocation> consumer) {
        final Path relativePath = root.relativize(path);
        if (!"json".equalsIgnoreCase(FilenameUtils.getExtension(relativePath.toString()))) {
            DocumentMod.logger.debug("Found non-json file in " + relativePath + ". Skipping");
            return;
        }

        final String name = FilenameUtils.removeExtension(relativePath.toString()).replaceAll("\\\\", "/");

        // "pattern.json" is a reserved name in JSON-land, so we cannot allow it
        if ("pattern".equals(name)) {
            throw new IllegalStateException("File name 'pattern.json' is invalid.\nThat name is reserved in JSON and has a special meaning " +
                    "that does not apply to this case.\nPlease remove or rename the invalid file.\nPath: " + relativePath);
        }

        if (!fileNameFilter.test(container, relativePath, name)) {
            return;
        }

        final ResourceLocation resourceLocation = new ResourceLocation(container.getModId(), name);

        try (final BufferedReader reader = Files.newBufferedReader(path)) {
            final JsonObject jsonObject = JsonUtils.fromJson(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(), reader, JsonObject.class);
            if (jsonObject == null) return;
            consumer.accept(container, jsonObject, resourceLocation);
        } catch (final JsonParseException e) {
            DocumentMod.logger.error("An error has occurred while attempting to parse JSON for file " + path);
            DocumentMod.logger.error("Error message: " + e.getMessage());
            DocumentMod.logger.error("Resource location where the issue happened: " + resourceLocation);
            DocumentMod.logger.error("The stacktrace will be printed to STDERR");
            e.printStackTrace(System.err);
        } catch (final IOException e) {
            DocumentMod.logger.warn("An error has occurred while attempting to read file " + path);
            DocumentMod.logger.warn("This file will be skipped. The exception will be printed now to STDERR");
            e.printStackTrace(System.err);
        }
    }
}
