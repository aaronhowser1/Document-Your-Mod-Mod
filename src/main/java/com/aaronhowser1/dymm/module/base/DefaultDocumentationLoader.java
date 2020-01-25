package com.aaronhowser1.dymm.module.base;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.documentation.Dependency;
import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.documentation.Target;
import com.aaronhowser1.dymm.api.loading.DocumentationLoader;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dymm.api.loading.factory.TargetFactory;
import com.aaronhowser1.dymm.api.loading.metadata.MetadataListenerRegistry;
import com.aaronhowser1.dymm.module.base.nbt.NbtFactoriesMetadataListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class DefaultDocumentationLoader implements DocumentationLoader {
    /*
     * Target JSON structure:
     * {
     *   "loader": "dymm:default",
     *   "conditions": [
     *     {
     *       "type": "dymm:condition",
     *       "...": "..."
     *     }
     *   ],
     *   "dependencies": [
     *     {
     *       "ordering": "before",
     *       "requirement": "required",
     *       "target": "dymm:other"
     *     }
     *   ],
     *   "targets": [
     *     {
     *       "type": "minecraft:item",
     *       "...": "..."
     *     },
     *     {
     *       "type": "minecraft:item_nbt",
     *       "...": "..."
     *     }
     *   ],
     *   "data": [
     *     {
     *       "type": "jei:description",
     *       "data": [
     *         "dymm.porkchop.tasty"
     *       ]
     *     },
     *     {
     *       "type": "quark:patchouli",
     *       "data": "string"
     *     }
     *   ]
     * }
     */
    @Nonnull
    @Override
    public ResourceLocation getIdentifier() {
        return new ResourceLocation(Constants.MOD_ID, "default");
    }

    @Nullable
    @Override
    public DocumentationEntry loadFromJson(@Nonnull final JsonObject object) {
        final JsonArray conditions = JsonUtilities.getJsonArrayOrElse(object, "conditions", JsonArray::new);
        if (!this.doConditionsPass(conditions)) return null;
        final Set<Dependency> dependencies = this.parseDependencies(JsonUtilities.getJsonArrayOrElse(object, "dependencies", JsonArray::new));
        final Set<Target> targets = this.parseTargets(JsonUtilities.getJsonArray(object, "targets"));
        final Set<DocumentationData> data = this.parseData(JsonUtilities.getJsonArray(object, "data"));
        return BasicDocumentationEntry.create(targets, data, dependencies);
    }

    @Override
    public void registerMetadataListeners(@Nonnull final MetadataListenerRegistry registry) {
        registry.register("configuration", new ConfigurationMetadataListener());
        registry.register("supported_versions", new SupportedVersionsMetadataListener());
        registry.register("nbt_factories", new NbtFactoriesMetadataListener());
    }

    private boolean doConditionsPass(@Nonnull final JsonArray conditions) {
        return JsonUtilities.checkEntriesAsJsonObjects(conditions, "conditions", this::doesConditionPass);
    }

    private boolean doesConditionPass(@Nonnull final JsonObject jsonCondition) {
        final ResourceLocation type = new ResourceLocation(JsonUtilities.getString(jsonCondition, "type"));
        final GlobalLoadingState state = Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState());
        final ConditionFactory factory = state.getConditionFactory(type);
        final Condition condition = factory.fromJson(state, jsonCondition);
        return condition.canParse();
    }

    @Nonnull
    private Set<Dependency> parseDependencies(@Nonnull final JsonArray dependencies) {
        final Set<Dependency> dependencySet = new HashSet<>();
        JsonUtilities.consumeEntriesAsJsonObjects(dependencies, "dependencies", it -> dependencySet.add(this.parseDependency(it)));
        return dependencySet;
    }

    @Nonnull
    private Dependency parseDependency(@Nonnull final JsonObject jsonDependency) {
        if (!jsonDependency.has("ordering") && !jsonDependency.has("requirement")) {
            throw new JsonSyntaxException("A dependency declaration must have either an 'ordering' or a 'requirement' property");
        }

        final Dependency.Ordering ordering = this.getOrderingFromString(JsonUtilities.getStringOrElse(jsonDependency, "ordering", () -> "either"));
        final Dependency.Requirement requirement = this.getRequirementFromString(JsonUtilities.getStringOrElse(jsonDependency, "requirement", () -> "optional"));

        if (ordering == Dependency.Ordering.EITHER && requirement == Dependency.Requirement.OPTIONAL) {
            throw new JsonSyntaxException("An optional dependency must specify an ordering property, or be declared as required instead");
        }

        final ResourceLocation target = new ResourceLocation(JsonUtilities.getString(jsonDependency, "target"));
        return BasicDependency.create(ordering, requirement, target);
    }

    @Nonnull
    private Dependency.Ordering getOrderingFromString(@Nonnull final String string) {
        return this.getFromEnum(string, Dependency.Ordering::values, it -> "The given string '" + it + "' is not a valid value for 'ordering'");
    }

    @Nonnull
    private Dependency.Requirement getRequirementFromString(@Nonnull final String string) {
        return this.getFromEnum(string, Dependency.Requirement::values, it -> "The given string '" + it + "' is not a valid value for 'requirement'");
    }

    @Nonnull
    private <T extends Enum<T>> T getFromEnum(@Nonnull final String string, @Nonnull final Supplier<T[]> valuesSupplier, @Nonnull final Function<String, String> errorMessage) {
        final Set<String> allowedNames = Arrays.stream(valuesSupplier.get())
                .map(Enum::name)
                .map(it -> it.toLowerCase(Locale.ENGLISH))
                .collect(Collectors.toSet());
        if (!allowedNames.contains(string)) throw new JsonParseException(errorMessage.apply(string));
        return Arrays.stream(valuesSupplier.get())
                .filter(it -> it.name().equals(string.toUpperCase(Locale.ENGLISH)))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Nonnull
    private Set<Target> parseTargets(@Nonnull final JsonArray targets) {
        if (targets.size() <= 0) throw new JsonParseException("A documentation entry must have at least one target");
        final Set<Target> targetSet = new HashSet<>();
        JsonUtilities.consumeEntriesAsJsonObjects(targets, "targets", it -> targetSet.addAll(this.parseTarget(it)));
        return targetSet;
    }

    @Nonnull
    private List<Target> parseTarget(@Nonnull final JsonObject jsonTarget) {
        final ResourceLocation type = new ResourceLocation(JsonUtilities.getString(jsonTarget, "type"));
        final GlobalLoadingState state = Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState());
        final TargetFactory factory = state.getTargetFactory(type);
        return factory.fromJson(state, jsonTarget);
    }

    @Nonnull
    private Set<DocumentationData> parseData(@Nonnull final JsonArray jsonData) {
        if (jsonData.size() == 0) {
            Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState())
                    .getReporter()
                    .report("This entry has no documentation data associated with it: this is useless. " +
                            "The entry will be registered anyway, but it will not appear anywhere");
            return new HashSet<>();
        }
        final Set<DocumentationData> data = new HashSet<>();
        JsonUtilities.consumeEntriesAsJsonObjects(jsonData, "data", it -> data.add(this.parseData(it)));
        return data;
    }

    @Nonnull
    private DocumentationData parseData(@Nonnull final JsonObject object) {
        final ResourceLocation type = new ResourceLocation(JsonUtilities.getString(object, "type"));
        final List<String> data = new ArrayList<>();
        try {
            final JsonArray dataArray = JsonUtilities.getJsonArray(object, "data");
            for (int i = 0; i < dataArray.size(); ++i) {
                final String item = JsonUtilities.asString(dataArray.get(i), "data[" + i + "]");
                if (item.isEmpty()) {
                    Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState())
                            .getReporter()
                            .notify("This entry presents an empty string inside its data page: this is usually the result of a mistake");
                }
                data.add(item);
            }
        } catch (@Nonnull final JsonSyntaxException e) {
            final String dataString = JsonUtilities.getString(object, "data");
            if (dataString.isEmpty()) {
                Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState())
                        .getReporter()
                        .notify("This entry presents an empty string inside its data page: this is usually the result of a mistake");
            }
            data.add(dataString);
        }
        return BasicDocumentationData.buildFrom(type, data);
    }
}
