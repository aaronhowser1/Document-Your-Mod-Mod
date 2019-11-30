package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Nullable;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface ContextBuilder {
    @Nonnull Context buildContext(@Nonnull final Nullable<LoadingPhase<?>> phase, @Nonnull final Object... object);
}
