package com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.CheckedException;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;

@FunctionalInterface
public interface KFunction0<R> extends Callable<R> {
    @Override
    default R call() {
        try {
            return this.invoke();
        } catch (@Nonnull final Exception e) {
            throw CheckedException.wrap(e);
        }
    }

    @Nonnull R invoke();
}
