package com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.ElvisExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.IfExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.WhenExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction1;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class Nullable<T> {
    private final T value;

    private Nullable(@javax.annotation.Nullable final T value) {
        this.value = value;
    }

    @Nonnull
    public static <T> Nullable<T> get(@javax.annotation.Nullable final Nullable<T> nullable) {
        return get(nullable == null? null : nullable.value);
    }

    @Nonnull
    public static <T> Nullable<T> get(@javax.annotation.Nullable final T value) {
        return new Nullable<>(value);
    }

    @Nonnull
    public <R> Nullable<R> ifPresent(@Nonnull final KFunction1<T, R> function) {
        return IfExpression.build(Objects.isNull(this.value), () -> Nullable.<R>get(null), () -> Nullable.get(function.invoke(Objects.requireNonNull(this.value)))).invoke();
    }

    @javax.annotation.Nullable
    public T unwrap() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return ElvisExpression.create(this.ifPresent(Object::hashCode), () -> 0).invoke();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean equals(@javax.annotation.Nullable final Object obj) {
        return WhenExpression.create(ImmutableList.of(
                WhenExpression.Case.create(this == obj, () -> true),
                WhenExpression.Case.create(obj == null, () -> false),
                WhenExpression.Case.create(obj instanceof Nullable, () -> {
                    final Nullable<?> other = (Nullable<?>) obj;
                    return (this.value == null && other.value == null) || (this.value.equals(other.value));
                })
        ), () -> false).invoke();
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public String toString() {
        return ElvisExpression.create(this.ifPresent(Object::toString), () -> "null").invoke();
    }
}
