package com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.expression.WhenExpression;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction0;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class Lazy<T> implements KFunction0<T> {
    private KFunction0<T> constructor;
    private boolean constructorCalled;
    private T constructed;

    private Lazy(@Nonnull final KFunction0<T> constructor) {
        this.constructor = Objects.requireNonNull(constructor);
        this.constructorCalled = false;
    }

    @Nonnull
    public static <T> Lazy<T> lazy(@Nonnull final KFunction0<T> constructor) {
        return new Lazy<>(constructor);
    }

    @Nonnull
    @Override
    public T invoke() {
        if (this.constructorCalled) return this.constructed;

        this.constructed = Objects.requireNonNull(this.constructor.invoke());
        this.constructor = null;
        this.constructorCalled = true;
        return this.invoke();
    }

    @Override
    public int hashCode() {
        if (!this.constructorCalled) this.invoke();
        return this.constructed.hashCode();
    }

    @Override
    @SuppressWarnings({"ConstantConditions", "OverlyStrongTypeCast"})
    public boolean equals(@Nullable final Object obj) {
        if (!this.constructorCalled) this.invoke();

        return WhenExpression.create(ImmutableList.of(
                WhenExpression.Case.create(this == obj, () -> true),
                WhenExpression.Case.create(obj == null, () -> false),
                WhenExpression.Case.create(obj instanceof Lazy, () -> this.constructed.equals(((Lazy<?>) obj).invoke()))
        ), () -> false).invoke();
    }

    @Override
    public String toString() {
        if (!this.constructorCalled) this.invoke();
        return this.constructed.toString();
    }
}
