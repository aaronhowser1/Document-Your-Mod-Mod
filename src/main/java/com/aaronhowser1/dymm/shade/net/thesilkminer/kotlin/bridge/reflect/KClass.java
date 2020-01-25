package com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.AssertNotNullExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.WhenExpression;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class KClass<T> {
    private final Class<T> javaClass;

    private KClass(@Nonnull final Class<T> javaClass) {
        this.javaClass = Objects.requireNonNull(javaClass);
    }

    @Nonnull
    public static <T> KClass<T> get(@Nonnull final Class<T> javaClass) {
        return new KClass<>(javaClass);
    }

    @Nonnull
    public T cast(@Nonnull final Nullable<Object> object) {
        return this.javaClass.cast(AssertNotNullExpression.create(object).invoke());
    }

    @Override
    public int hashCode() {
        return this.javaClass.hashCode();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean equals(@javax.annotation.Nullable final Object obj) {
        return WhenExpression.create(ImmutableList.of(
                WhenExpression.Case.create(this == obj, () -> true),
                WhenExpression.Case.create(obj == null, () -> false),
                WhenExpression.Case.create(obj instanceof KClass, () -> this.javaClass.equals(((KClass<?>) obj).javaClass))
        ), () -> false).invoke();
    }

    @Override
    public String toString() {
        return this.javaClass.toString();
    }

    @Nonnull
    public Nullable<String> getSimpleName() {
        return Nullable.get(this.javaClass.getSimpleName());
    }

    @Nonnull
    public Nullable<String> getQualifiedName() {
        return Nullable.get(this.javaClass.getName());
    }

    @Nonnull
    public Class<T> java() {
        return this.javaClass;
    }

    public <F> boolean isSuperClassOf(@Nonnull final KClass<F> kClass) {
        return kClass.javaClass.equals(this.javaClass) || this.isSuperClassOf(KClass.get(kClass.javaClass.getSuperclass()));
    }
}
