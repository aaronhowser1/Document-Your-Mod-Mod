package com.aaronhowser1.dymm.api.loading.factory;

import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface ConditionFactory {
    @Nonnull Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object);
}
