package com.aaronhowser1.dymm.module.base;

import com.aaronhowser1.dymm.api.documentation.Dependency;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class BasicDependency implements Dependency {
    private final Ordering ordering;
    private final Requirement requirement;
    private final ResourceLocation dependencyTarget;

    private BasicDependency(@Nonnull final Ordering ordering, @Nonnull final Requirement requirement, @Nonnull final ResourceLocation target) {
        this.ordering = ordering;
        this.requirement = requirement;
        this.dependencyTarget = target;
    }

    @Nonnull
    public static Dependency create(@Nonnull final Ordering ordering, @Nonnull final Requirement requirement, @Nonnull final ResourceLocation target) {
        return new BasicDependency(Objects.requireNonNull(ordering), Objects.requireNonNull(requirement), Objects.requireNonNull(target));
    }

    @Nonnull
    @Override
    public Ordering getOrdering() {
        return this.ordering;
    }

    @Nonnull
    @Override
    public Requirement getRequirement() {
        return this.requirement;
    }

    @Nonnull
    @Override
    public ResourceLocation getTarget() {
        return this.dependencyTarget;
    }
}
