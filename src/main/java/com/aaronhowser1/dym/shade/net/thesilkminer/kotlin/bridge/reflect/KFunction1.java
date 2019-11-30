package com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect;

import javax.annotation.Nonnull;
import java.util.function.Function;

@FunctionalInterface
public interface KFunction1<A, R> extends Function<A, R> {
    @Nonnull
    @Override
    default R apply(@Nonnull final A a) {
        return this.invoke(a);
    }

    @Nonnull R invoke(@Nonnull final A a);
}
