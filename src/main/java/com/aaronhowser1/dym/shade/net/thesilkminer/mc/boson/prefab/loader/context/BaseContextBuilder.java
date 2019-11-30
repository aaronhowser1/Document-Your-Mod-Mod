package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.context;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.ContextBuilder;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.LoadingPhase;

import javax.annotation.Nonnull;

public final class BaseContextBuilder implements ContextBuilder {
    private BaseContextBuilder() {}

    @Nonnull
    public static BaseContextBuilder create() {
        return new BaseContextBuilder();
    }

    @Nonnull
    @Override
    public Context buildContext(@Nonnull final Nullable<LoadingPhase<?>> phase, @Nonnull final Object... object) {
        return BaseContext.create();
    }
}
