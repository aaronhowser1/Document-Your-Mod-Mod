package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.implementation.loader.BosonContextKey;

import javax.annotation.Nonnull;

public interface ContextKey<T> {
    @Nonnull
    static <T> ContextKey<T> invoke(@Nonnull final String name, @Nonnull final KClass<T> type) {
        // Tight coupling between API and impl since this is a rewrite for this particular use case
        return BosonContextKey.create(name, type);
    }

    @Nonnull String getName();
    @Nonnull KClass<T> getType();
}
