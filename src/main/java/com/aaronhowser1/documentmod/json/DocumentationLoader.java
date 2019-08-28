package com.aaronhowser1.documentmod.json;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.config.DYMMConfig;
import com.aaronhowser1.documentmod.json.factory.condition.ConditionFactory;
import com.aaronhowser1.documentmod.json.factory.Factory;
import com.aaronhowser1.documentmod.json.factory.nbt.NbtTagFactory;
import com.aaronhowser1.documentmod.json.factory.stack.StackFactory;
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
import r.cpw.mods.fml.common.toposort.DocumentationSorter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
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
import java.util.stream.Collectors;

public enum DocumentationLoader {
    INSTANCE;

    private static final String JSON_FACTORIES_CONDITION_TAG = "condition";
    private static final String JSON_FACTORIES_STACK_TAG = "stack";
    private static final String JSON_FACTORIES_NBT_TAG = "nbt";

    private static final String CONDITIONS_KEY = "conditions";

    private static final Map<Class<? extends Factory<?>>, Map<ResourceLocation, Factory<?>>> FACTORIES = Maps.newHashMap();

    private IForgeRegistry<ModDocumentation> registry = null;
    private ModContainer thisModContainer = null;
    private ProgressManager.ProgressBar bar = null;

    @Nullable
    public <T extends Factory<T>> T getFactory(@Nonnull final Class<T> type, @Nonnull final ResourceLocation name) {
        final Map<ResourceLocation, Factory<?>> tmp = FACTORIES.get(type);
        if (tmp == null) return null;
        try {
            @SuppressWarnings("unchecked")
            final Factory<T> factory = (Factory<T>) tmp.get(name);
            if (factory == null) return null;
            return factory.asT();
        } catch (@Nonnull final ClassCastException e) {
            return null;
        }
    }

    public void loadAndRegister(@Nonnull final IForgeRegistry<ModDocumentation> registry) {
        this.loadAndRegister(registry, this::loadFromJson);
    }

    private void loadAndRegister(@Nonnull final IForgeRegistry<ModDocumentation> registry, @Nonnull final Runnable run) {
        this.registry = registry;
        run.run();
        this.registry = null;
    }

    private void loadFromJson() {
        DocumentMod.logger.info("Attempting to load data-driven mod documentation");
        this.loadFactoriesFromJson();
        final List<ModDocumentation> documentation = this.loadDocumentationFromJson();
        final List<ModDocumentation> sortedDocumentation = this.sortModDocumentation(documentation);
        this.registerDocumentationToRegistry(sortedDocumentation);
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

    @SuppressWarnings("unchecked")
    private void loadFactories(@Nonnull final ModContainer container, @Nonnull final JsonObject object) {
        DocumentMod.logger.info("Found factories file for mod " + container.getModId() + ". Proceeding with loading now");
        object.entrySet().forEach(entry -> {
            final String name = entry.getKey();
            if (JSON_FACTORIES_CONDITION_TAG.equals(name)) {
                final JsonObject conditionFactories = JsonUtils.getJsonObject(entry.getValue(), JSON_FACTORIES_CONDITION_TAG);
                conditionFactories.entrySet().forEach(factory -> this.loadAndRegisterFactory(container, factory, ConditionFactory.class));
            } else if (JSON_FACTORIES_NBT_TAG.equals(name)) {
                final JsonObject nbtFactories = JsonUtils.getJsonObject(entry.getValue(), JSON_FACTORIES_NBT_TAG);
                nbtFactories.entrySet().forEach(factory -> this.loadAndRegisterFactory(container, factory, NbtTagFactory.class));
            } else if (JSON_FACTORIES_STACK_TAG.equals(name)) {
                final JsonObject stackFactories = JsonUtils.getJsonObject(entry.getValue(), JSON_FACTORIES_STACK_TAG);
                stackFactories.entrySet().forEach(factory -> this.loadAndRegisterFactory(container, factory, StackFactory.class));
            } else {
                DocumentMod.logger.warn("Ignoring unknown factory type '" + name + "' for mod '" + container.getModId() + "'");
            }
        });
    }

    private <T extends Factory<T>> void loadAndRegisterFactory(@Nonnull final ModContainer container,
                                                               @Nonnull final Map.Entry<String, JsonElement> entry,
                                                               @Nonnull final Class<T> type) {
        final ResourceLocation name = new ResourceLocation(container.getModId(), entry.getKey());
        final String className = JsonUtils.getString(entry.getValue(), entry.getKey());
        final T factory = this.initFactoryClass(className, type);
        final Map<ResourceLocation, Factory<?>> factoryMap = FACTORIES.computeIfAbsent(type, key -> Maps.newHashMap());
        if (factoryMap.get(name) != null) {
            throw new JsonParseException("A factory with the given name already exists: " + name + " (class: " + factoryMap.get(name) + ")");
        }
        factoryMap.put(name, factory);
    }

    private <T extends Factory<T>> T initFactoryClass(@Nonnull final String name, @Nonnull final Class<T> type) {
        try {
            final Class<?> tClass = Class.forName(name);
            if (!type.isAssignableFrom(tClass)) {
                throw new JsonParseException("The given class " + name + " is not an instance of type " + type.getName());
            }
            @SuppressWarnings("unchecked")
            final Constructor<T> constructor = ((Class<T>) tClass).getConstructor();
            return constructor.newInstance();
        } catch (@Nonnull final ClassNotFoundException e) {
            throw new JsonParseException("Class " + name + " does not exist", e);
        } catch (@Nonnull final InstantiationException | IllegalAccessException e) {
            throw new JsonParseException("Class " + name + " cannot be instantiated. Make sure that it has a default public constructor", e);
        } catch (@Nonnull final ReflectiveOperationException e) {
            throw new JsonParseException("An error occurred while loading factory " + name, e);
        }
    }

    private void dumpLoadedFactories() {
        final Consumer<String> consumer = DYMMConfig.debugModIsDocumented ? DocumentMod.logger::info : DocumentMod.logger::trace;

        consumer.accept("Dumping factories: ");
        FACTORIES.forEach((type, factories) -> {
            consumer.accept("    " + type + ":");
            factories.forEach((name, factory) -> consumer.accept("        " + name + " -> " + factory));
        });
    }

    @Nonnull
    private List<ModDocumentation> loadDocumentationFromJson() {
        DocumentMod.logger.info("Reading JSON archive for mod documentation");
        this.bar = ProgressManager.push("Reading JSON documentation", Loader.instance().getActiveModList().size());
        this.thisModContainer = Loader.instance().getActiveModList().stream().filter(item -> DocumentMod.MODID.equals(item.getModId())).findFirst().orElse(null);

        final List<ModDocumentation> documentation = Loader.instance().getActiveModList().stream()
                .map(this::loadModDocumentation)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        ProgressManager.pop(this.bar);
        this.bar = null;
        this.thisModContainer = null;
        DocumentMod.logger.info("Done reading JSON archive");

        return documentation;
    }

    @Nonnull
    private List<ModDocumentation> loadModDocumentation(@Nonnull final ModContainer modContainer) {
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

        // And let's revert things so that it appears that nothing has been done
        Loader.instance().setActiveModContainer(previous);

        return documentation;
    }

    private void loadModDocumentationForSource(@Nonnull final ModContainer container, @Nonnull final File sourceFile, @Nonnull final List<ModDocumentation> list) {
        this.findDirectoryAndWalk(container, sourceFile, (path, root) -> this.loadJsonDocumentationFile(container, path, root, list));
    }

    private void loadJsonDocumentationFile(@Nonnull final ModContainer modContainer, @Nonnull final Path path, @Nonnull final Path root, @Nonnull final List<ModDocumentation> list) {
        this.loadJsonFile(modContainer, path, root, (container, relativePath, name) -> {
            if (Objects.isNull(container) || Objects.isNull(relativePath) || Objects.isNull(name)) return false;
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

    @Nonnull
    private List<ModDocumentation> sortModDocumentation(@Nonnull final List<ModDocumentation> documentation) {
        DocumentMod.logger.info("Sorting documentation data");

        final Map<ResourceLocation, ModDocumentation> namedLookup = Maps.newHashMap();
        documentation.forEach(item -> namedLookup.put(Objects.requireNonNull(item.getRegistryName()), item));

        final DocumentationSorter sorter = new DocumentationSorter(documentation, namedLookup);
        final List<ModDocumentation> sortedDocumentation = sorter.sort();

        DocumentMod.logger.info("Sorting completed");

        DocumentMod.logger.info("Checking for not-satisfied required requirements"); // Weird wording I know
        sortedDocumentation.stream()
                .map(ModDocumentation::getRequirements)
                .flatMap(List::stream)
                .filter(Requirement::isRequired)
                .distinct()
                .map(Requirement::getReferredRegistryName)
                .filter(name -> !namedLookup.containsKey(name))
                .findAny()
                .ifPresent(it -> {
                    throw new JsonParseException("The requirement specified by one or more entries is not satisfied.\nDepends on: " + it);
                });
        DocumentMod.logger.info("Check completed");

        return sortedDocumentation;
    }

    private void registerDocumentationToRegistry(@Nonnull final List<ModDocumentation> documentation) {
        DocumentMod.logger.info("Registering " + documentation.size() + " mod documentations to registry");
        final ProgressManager.ProgressBar bar = ProgressManager.push("Registering mod documentation", documentation.size());
        documentation.forEach(item -> {
            bar.step(Objects.toString(item.getRegistryName()));
            this.registry.register(item);
        });
        ProgressManager.pop(bar);
        DocumentMod.logger.info("Registration completed successfully");
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
        final ConditionFactory conditionFactory = this.getFactory(ConditionFactory.class, new ResourceLocation(type));
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
