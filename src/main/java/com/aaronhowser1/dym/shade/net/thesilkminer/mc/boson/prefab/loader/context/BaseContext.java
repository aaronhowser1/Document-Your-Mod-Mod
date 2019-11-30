package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.context;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Unit;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.expression.IfExpression;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction1;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.ContextKey;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BaseContext implements Context {
    private final Map<ContextKey<?>, Object> map = new LinkedHashMap<>();

    private BaseContext() {}

    @Nonnull
    public static BaseContext create() {
        return new BaseContext();
    }

    @Nonnull
    @Override
    public <T> Nullable<T> get(@Nonnull final ContextKey<? extends T> key) {
        return Nullable.get(key.getType().cast(Nullable.get(this.map.get(key))));
    }

    @Nonnull
    @Override
    public <T> Unit set(@Nonnull final ContextKey<? extends T> key, @Nonnull final T value) {
        this.map.put(key, value);
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public <T> T computeIfAbsent(@Nonnull final ContextKey<? extends T> key, @Nonnull final KFunction1<ContextKey<?>, T> supplier) {
        return key.getType().cast(Nullable.get(this.map.computeIfAbsent(key, supplier::invoke)));
    }

    @Nonnull
    @Override
    public <T, R> Nullable<R> ifPresent(@Nonnull final ContextKey<? extends T> key, @Nonnull final KFunction1<T, R> consumer) {
        return IfExpression.build(
                this.map.get(key) != null,
                () -> Nullable.get(consumer.invoke(key.getType().cast(Nullable.get(this.map.get(key))))),
                () -> Nullable.<R>get(null)
        ).invoke();
    }
}
