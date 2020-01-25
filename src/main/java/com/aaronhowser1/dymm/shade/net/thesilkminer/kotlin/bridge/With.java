package com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction1;

import javax.annotation.Nonnull;

public final class With {
    private With() {}

    @Nonnull
    public static <T, R> R with(@Nonnull final T type, @Nonnull final KFunction1<T, R> function) {
        return function.invoke(type);
    }
}
