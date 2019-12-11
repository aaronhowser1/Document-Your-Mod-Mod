package com.aaronhowser1.dym.api.loading;

import com.aaronhowser1.dym.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dym.api.loading.factory.TargetFactory;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface GlobalLoadingState {
    @Nonnull Reporter getReporter();
    @Nonnull ConditionFactory getConditionFactory(@Nonnull final ResourceLocation location);
    @Nonnull TargetFactory getTargetFactory(@Nonnull final ResourceLocation location);
}