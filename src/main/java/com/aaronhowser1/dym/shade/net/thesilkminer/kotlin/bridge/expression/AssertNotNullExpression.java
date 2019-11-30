package com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.expression;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction0;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class AssertNotNullExpression<T> implements KFunction0<T> {
    private final Nullable<T> value;

    private AssertNotNullExpression(@Nonnull final Nullable<T> value) {
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    public static <T> AssertNotNullExpression<T> create(@Nonnull final Nullable<T> value) {
        return new AssertNotNullExpression<>(value);
    }

    @Nonnull
    public static <T> AssertNotNullExpression<T> create(@javax.annotation.Nullable T value) {
        return create(Nullable.get(value));
    }

    @Nonnull
    @Override
    public T invoke() {
        return Objects.requireNonNull(this.value.unwrap());
    }
}
