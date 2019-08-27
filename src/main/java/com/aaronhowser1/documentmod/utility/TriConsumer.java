package com.aaronhowser1.documentmod.utility;

import javax.annotation.Nullable;

@FunctionalInterface
public interface TriConsumer<T, S, U> {
    void accept(@Nullable final  T t, @Nullable final S s, @Nullable final U u);
}
