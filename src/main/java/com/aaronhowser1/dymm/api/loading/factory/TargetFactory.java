package com.aaronhowser1.dymm.api.loading.factory;

import com.aaronhowser1.dymm.api.documentation.Target;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import java.util.List;

@FunctionalInterface
public interface TargetFactory {
    @Nonnull List<Target> fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object);
}
