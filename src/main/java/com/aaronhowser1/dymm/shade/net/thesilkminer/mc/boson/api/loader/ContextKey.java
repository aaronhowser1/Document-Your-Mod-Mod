package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.ApiBindings;

import javax.annotation.Nonnull;

public interface ContextKey<T> {
    @Nonnull
    static <T> ContextKey<T> invoke(@Nonnull final String name, @Nonnull final KClass<T> type) {
        return ApiBindings.BOSON_API.invoke().createLoaderContextKey(name, type);
    }

    @Nonnull String getName();
    @Nonnull KClass<T> getType();
}
