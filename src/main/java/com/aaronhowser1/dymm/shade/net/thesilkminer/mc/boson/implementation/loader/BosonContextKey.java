package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.implementation.loader;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.ContextKey;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class BosonContextKey<T> implements ContextKey<T> {
    private static final Map<String, ContextKey<?>> KEYS = new HashMap<>();

    private final String name;
    private final KClass<T> type;

    private BosonContextKey(@Nonnull final String name, @Nonnull final KClass<T> type) {
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T> ContextKey<T> create(@Nonnull final String name, @Nonnull final KClass<T> type) {
        return (ContextKey<T>) KEYS.computeIfAbsent(name, key -> new BosonContextKey<>(name, type));
    }

    @Nonnull
    @Override
    public String getName() {
        return this.name;
    }

    @Nonnull
    @Override
    public KClass<T> getType() {
        return this.type;
    }
}
