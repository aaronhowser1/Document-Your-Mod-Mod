package com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

@FunctionalInterface
public interface KFunction2<A, B, R> extends BiFunction<A, B, R> {

    @Nonnull
    @Override
    default R apply(@Nonnull final A a, @Nonnull final B b) {
        return this.invoke(a, b);
    }

    @Nonnull R invoke(@Nonnull final A a, @Nonnull final B b);
}
