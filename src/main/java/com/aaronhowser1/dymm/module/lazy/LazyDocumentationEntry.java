package com.aaronhowser1.dymm.module.lazy;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.documentation.Dependency;
import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.documentation.Target;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.TargetFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class LazyDocumentationEntry extends IForgeRegistryEntry.Impl<DocumentationEntry> implements DocumentationEntry {
    private final Set<DocumentationData> data;
    private final Set<Dependency> dependencies;

    private Set<Target> targets;
    private JsonArray targetsArray;

    private LazyDocumentationEntry(@Nonnull final JsonArray jsonTargets, @Nonnull final Set<DocumentationData> data, @Nonnull final Set<Dependency> dependencies) {
        this.targets = new HashSet<>();
        this.data = new HashSet<>(data);
        this.dependencies = new HashSet<>(dependencies);
        this.targetsArray = jsonTargets;
    }

    @Nonnull
    static DocumentationEntry create(@Nonnull final JsonArray jsonTargets, @Nonnull final Set<DocumentationData> data, @Nonnull final Set<Dependency> dependencies) {
        return new LazyDocumentationEntry(Objects.requireNonNull(jsonTargets), Objects.requireNonNull(data), Objects.requireNonNull(dependencies));
    }

    @Nonnull
    @Override
    public Set<Target> getTargets() {
        if (Objects.isNull(this.targets)) {
            this.targets = this.parseTargetsArray();
            this.targetsArray = null;
        }
        return new HashSet<>(this.targets);
    }

    @Nonnull
    @Override
    public Set<DocumentationData> getDocumentationData() {
        return new HashSet<>(this.data);
    }

    @Nonnull
    @Override
    public Set<Dependency> getDependencies() {
        return new HashSet<>(this.dependencies);
    }

    @Nonnull
    private Set<Target> parseTargetsArray() {
        final Set<Target> targets = new HashSet<>();
        JsonUtilities.consumeEntriesAsJsonObjects(this.targetsArray, "targets", it -> targets.addAll(this.parseTarget(it)));
        return targets;
    }

    @Nonnull
    private List<Target> parseTarget(@Nonnull final JsonObject jsonTarget) {
        final ResourceLocation type = new ResourceLocation(JsonUtilities.getString(jsonTarget, "type"));
        final GlobalLoadingState state = Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState());
        final TargetFactory factory = state.getTargetFactory(type);
        return factory.fromJson(state, jsonTarget);
    }
}
