package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Preprocessor<T, R> {
    @Nonnull Nullable<R> preProcessData(@Nonnull final T content, @Nonnull final NameSpacedString identifier,
                                        @Nonnull final Nullable<Context> globalContext, @Nonnull final Nullable<Context> phaseContext);
}
