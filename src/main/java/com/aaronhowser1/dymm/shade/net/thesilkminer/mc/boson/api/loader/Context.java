package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction1;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Unit;

import javax.annotation.Nonnull;

public interface Context {
    @Nonnull <T> Nullable<T> get(@Nonnull final ContextKey<? extends T> key);
    @Nonnull <T> Unit set(@Nonnull final ContextKey<? extends T> key, @Nonnull T value);
    @Nonnull <T> T computeIfAbsent(@Nonnull final ContextKey<? extends T> key, @Nonnull final KFunction1<ContextKey<?>, T> supplier);
    @Nonnull <T, R> Nullable<R> ifPresent(@Nonnull final ContextKey<? extends T> key, @Nonnull final KFunction1<T, R> consumer);
}
