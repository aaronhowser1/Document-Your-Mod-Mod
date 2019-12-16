package com.aaronhowser1.dymm.module.base;

import com.aaronhowser1.dymm.api.documentation.Dependency;
import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.documentation.Target;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class BasicDocumentationEntry extends IForgeRegistryEntry.Impl<DocumentationEntry> implements DocumentationEntry {
    private final Set<Target> targets;
    private final Set<DocumentationData> data;
    private final Set<Dependency> dependencies;

    private BasicDocumentationEntry(@Nonnull final Set<Target> targets, @Nonnull final Set<DocumentationData> data, @Nonnull final Set<Dependency> dependencies) {
        this.targets = new HashSet<>(targets);
        this.data = new HashSet<>(data);
        this.dependencies = new HashSet<>(dependencies);
    }

    @Nonnull
    public static DocumentationEntry create(@Nonnull final Set<Target> targets, @Nonnull final Set<DocumentationData> data, @Nonnull final Set<Dependency> dependencies) {
        return new BasicDocumentationEntry(Objects.requireNonNull(targets), Objects.requireNonNull(data), Objects.requireNonNull(dependencies));
    }

    @Nonnull
    @Override
    public Set<Target> getTargets() {
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
}
