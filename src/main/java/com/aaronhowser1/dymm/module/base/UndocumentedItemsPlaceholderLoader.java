package com.aaronhowser1.dymm.module.base;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.documentation.Target;
import com.aaronhowser1.dymm.api.loading.DocumentationLoader;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.Reporter;
import com.aaronhowser1.dymm.api.loading.factory.TargetFactory;
import com.aaronhowser1.dymm.api.loading.metadata.MetadataListenerRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class UndocumentedItemsPlaceholderLoader implements DocumentationLoader {
    /*
     * Expected JSON:
     * {
     *   "targets": [
     *     {
     *       "type": "minecraft:item",
     *       "...": "..."
     *     },
     *     {
     *       "type": "minecraft:item",
     *       "...": "..."
     *     }
     *   ]
     * }
     */

    private boolean hasRegistered = false;
    private final Set<Target> targets = new HashSet<>();

    @Nonnull
    @Override
    public ResourceLocation getIdentifier() {
        return new ResourceLocation(Constants.MOD_ID, "undocumented_items_preloaded");
    }

    @Nullable
    @Override
    public DocumentationEntry loadFromJson(@Nonnull final JsonObject object) {
        final Reporter state = Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState()).getReporter();
        if (this.hasRegistered) {
            state.interrupt("A placeholder entry has already been registered! If you're attempting to use this loader for your own entries, DON'T! THIS IS NOT FOR EXTERNAL USE!");
            return null;
        }
        state.notify("Undocumented items placeholder registered successfully");
        this.hasRegistered = true;
        return BasicDocumentationEntry.create(new HashSet<>(this.targets), new HashSet<>(), new HashSet<>());
    }

    @Override
    public void registerMetadataListeners(@Nonnull final MetadataListenerRegistry registry) {
        registry.register("undocumented_targets", (object, namespace) -> {
            final Reporter state = Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState()).getReporter();
            state.notify("Reading purposefully undocumented items for namespace '" + namespace + "'");
            if (!object.has("targets")) {
                state.report("The metadata-carrying entry for '" + namespace + "' has an empty target list! This is useless!");
            }
            this.targets.addAll(this.parseTargets(JsonUtilities.getJsonArrayOrElse(object, "targets", JsonArray::new)));
        });
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
}
