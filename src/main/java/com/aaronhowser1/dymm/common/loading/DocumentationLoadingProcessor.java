package com.aaronhowser1.dymm.common.loading;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.loading.DocumentationLoader;
import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dymm.api.loading.factory.TargetFactory;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Unit;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.AssertNotNullExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.ContextKey;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Processor;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public final class DocumentationLoadingProcessor implements Processor<JsonObject> {
    @FunctionalInterface
    private interface TriFunction<A, B, C, R> {
        @Nonnull R invoke(@Nonnull final A a, @Nonnull final B b, @Nonnull final C c);
    }

    private static final L LOG = L.create(Constants.MOD_NAME, "Documentation Processor");

    private static final int FLAG_FACTORIES = 0b0000_0001;
    private static final int FLAG_METADATA = 0b0000_0010;
    private static final int FLAG_DOCUMENTATION = 0b0000_0100;

    private static final String FACTORY_CONDITION = "condition";
    private static final String FACTORY_TARGET = "target";

    private static final String KEY_LOADER = "loader";

    private static final Map<String, Class<?>> NAME_TO_INTERFACE = new HashMap<>();
    private static final Map<String, ContextKey<?>> NAME_TO_CONTEXT_KEY = new HashMap<>();

    private final TriFunction<JsonObject, NameSpacedString, Context, Unit> processorFunction;

    private DocumentationLoadingProcessor(final int flag) {
        if (flag == FLAG_FACTORIES) this.processorFunction = this::processFactories;
        else if (flag == FLAG_METADATA) this.processorFunction = this::processMetadata;
        else if (flag == FLAG_DOCUMENTATION) this.processorFunction = this::processDocumentation;
        else throw new IllegalArgumentException("Flag " + flag + " was unrecognized");
    }

    static {
        NAME_TO_INTERFACE.put(FACTORY_CONDITION, ConditionFactory.class);
        NAME_TO_INTERFACE.put(FACTORY_TARGET, TargetFactory.class);

        NAME_TO_CONTEXT_KEY.put(FACTORY_CONDITION, ContextKey.invoke(FACTORY_CONDITION, KClass.get(Map.class)));
        NAME_TO_CONTEXT_KEY.put(FACTORY_TARGET, ContextKey.invoke(FACTORY_TARGET, KClass.get(Map.class)));
    }

    @Nonnull
    static DocumentationLoadingProcessor withFlag(final int flag) {
        return new DocumentationLoadingProcessor(flag);
    }

    @Nonnull
    static <T> Map<ResourceLocation, T> get(@Nonnull final Context globalContext, @Nonnull final String marker) {
        @SuppressWarnings("unchecked")
        final ContextKey<Map<ResourceLocation, T>> contextKey = (ContextKey<Map<ResourceLocation, T>>) NAME_TO_CONTEXT_KEY.get(marker);
        return AssertNotNullExpression.create(globalContext.get(contextKey)).invoke();
    }

    @Nonnull
    @Override
    public Unit process(@Nonnull final JsonObject content, @Nonnull final NameSpacedString identifier,
                        @Nonnull final Nullable<Context> globalContext, @Nonnull final Nullable<Context> phaseContext) {
        return this.processorFunction.invoke(content, identifier, AssertNotNullExpression.create(globalContext).invoke());
    }

    @Nonnull
    private Unit processFactories(@Nonnull final JsonObject content, @Nonnull final NameSpacedString identifier, @Nonnull final Context globalContext) {
        content.entrySet().forEach(it -> {
            final String name = it.getKey();
            LOG.debug("Attempting to read data from JsonObject for factory type '" + name + "'");
            final Class<?> factoryClass = NAME_TO_INTERFACE.get(name);
            if (factoryClass == null) {
                LOG.warn("'" + name + "' isn't a type of factory that is recognized by the system");
                return;
            }
            final JsonObject object = it.getValue().getAsJsonObject();
            object.entrySet().forEach(factoryEntry -> {
                final ResourceLocation id = this.constructFactoryName(identifier, factoryEntry.getKey());
                if (!factoryEntry.getValue().isJsonPrimitive() || !factoryEntry.getValue().getAsJsonPrimitive().isString()) {
                    throw new JsonSyntaxException("The entry for a factory must be composed of a string key and a string value, referring to the target class");
                }

                final Object factoryAny;
                try {
                    factoryAny = Class.forName(factoryEntry.getValue().getAsString()).getConstructor().newInstance();
                } catch (@Nonnull final ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }

                final Object factory = factoryClass.cast(factoryAny);

                @SuppressWarnings("unchecked")
                final Map<ResourceLocation, Object> map = (Map<ResourceLocation, Object>) globalContext
                        .computeIfAbsent(Objects.requireNonNull(NAME_TO_CONTEXT_KEY.get(name), name + " does not have a context key"), key -> new HashMap<>());

                if (map.get(id) == null) {
                    LOG.bigWarn("An attempt of overriding a previously registered factory has been identified\n" +
                            "Note that this attempt will not be blocked, but it may lead to errors in the future\n" +
                            "Name of conflict: " + id, L.DumpStackBehavior.DO_NOT_DUMP);
                }

                map.put(id, factory);
            });
        });
        return Unit.UNIT;
    }

    @Nonnull
    private ResourceLocation constructFactoryName(@Nonnull final NameSpacedString identifier, @Nonnull final String key) {
        if (key.indexOf(':') != -1) {
            final String[] parts = key.split(Pattern.quote(":"), 2);
            final String providedNamespace = parts[0];
            final String path = parts[1];
            if (identifier.getNameSpace().equals(providedNamespace)) {
                LOG.warn("Found additional namespace '" + providedNamespace + "' for identifier '" + identifier + "': this is unnecessary and will be stripped out");
            } else {
                LOG.warn("Attempting to register factory '" + path + "' with namespace '" + providedNamespace + "' instead of the expected '" +
                        identifier.getNameSpace() + "': this may cause errors later on");
            }
            return new ResourceLocation(providedNamespace, path);
        } else {
            return new ResourceLocation(identifier.getNameSpace(), key);
        }
    }

    @Nonnull
    private Unit processMetadata(@Nonnull final JsonObject content, @Nonnull final NameSpacedString identifier, @Nonnull final Context globalContext) {
        final String type = identifier.getPath().substring(1);
        LOG.info("Firing process metadata event of type '" + type + "' for '" + identifier + "'");
        LoaderRegistry.INSTANCE.fireEvent(content, type);
        return Unit.UNIT;
    }

    @Nonnull
    private Unit processDocumentation(@Nonnull final JsonObject content, @Nonnull final NameSpacedString identifier, @Nonnull final Context globalContext) {
        if (!content.has(KEY_LOADER)) {
            LOG.warn("The entry '" + identifier + "' has no loader specified! This is DEPRECATED and support for it will be removed soon. Replacing with default loader");
            content.add(KEY_LOADER, new JsonPrimitive(Constants.MOD_ID + ":default"));
        }
        return this.processDocumentationWithLoader(content, identifier, globalContext);
    }

    @Nonnull
    private Unit processDocumentationWithLoader(@Nonnull final JsonObject content, @Nonnull final NameSpacedString identifier, @Nonnull final Context globalContext) {
        final String loaderName = content.get(KEY_LOADER).getAsString();
        LOG.debug("Attempting to load '" + identifier + "' with loader name '" + loaderName + "'");
        if (loaderName.indexOf(':') == -1) {
            throw new JsonSyntaxException("For file '" + identifier + "': the loader name must be name-spaced");
        }
        final DocumentationLoader loader = LoaderRegistry.INSTANCE.findLoader(new ResourceLocation(loaderName));
        if (loader == null) {
            throw new JsonParseException("For file '" + identifier + "': unable to find the specified loader '" + loaderName + "': maybe it hasn't been registered?");
        }

        this.buildStateFor(loader, identifier, globalContext);
        final DocumentationEntry entry;
        try {
            entry = loader.loadFromJson(content);
        } catch (@Nonnull final Exception e) {
            throw new JsonParseException(e.getMessage(), e);
        }

        if (entry == null) {
            LOG.warn("Attempting to load documentation entry '" + identifier + "' resulted in a null entry being produced. While not an error, " +
                    "this is generally not advised. The registration will be skipped");
            return Unit.UNIT;
        }

        entry.setRegistryName(new ResourceLocation(identifier.getNameSpace(), identifier.getPath()));
        LoadingHandler.getRegistry().register(entry);

        return Unit.UNIT;
    }

    private void buildStateFor(@Nonnull final DocumentationLoader loader, @Nonnull final NameSpacedString identifier, @Nonnull final Context globalContext) {
        final ResourceLocation targetId = new ResourceLocation(identifier.getNameSpace(), identifier.getPath());
        LoadingState.rebuild(targetId, LOG, loader, globalContext);
    }
}
