package com.aaronhowser1.dym.api.loading.factory;

import com.aaronhowser1.dym.api.documentation.Target;
import com.aaronhowser1.dym.api.loading.GlobalLoadingState;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface TargetFactory {
    @Nonnull Target fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object);
}
