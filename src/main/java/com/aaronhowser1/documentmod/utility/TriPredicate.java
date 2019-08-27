package com.aaronhowser1.documentmod.utility;

import javax.annotation.Nullable;

@FunctionalInterface
public interface TriPredicate<T, S, U> {

    boolean test(@Nullable final T t, @Nullable final S s, @Nullable final U u);
}
