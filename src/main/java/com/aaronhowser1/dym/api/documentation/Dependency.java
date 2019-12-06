package com.aaronhowser1.dym.api.documentation;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface Dependency {
    enum Ordering {
        AFTER, BEFORE, EITHER
    }
    enum Requirement {
        REQUIRED, OPTIONAL
    }

    @Nonnull Ordering getOrdering();
    @Nonnull Requirement getRequirement();
    @Nonnull ResourceLocation getTarget();
}
