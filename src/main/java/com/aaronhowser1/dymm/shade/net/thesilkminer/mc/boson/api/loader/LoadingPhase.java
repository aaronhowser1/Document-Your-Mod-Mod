package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public interface LoadingPhase<T> {
    @Nonnull String getName();
    @Nonnull List<Filter> getFilters();
    @Nonnull Nullable<ContextBuilder> getContextBuilder();
    @Nonnull Nullable<Preprocessor<String, T>> getPreprocessor();
    @Nonnull Processor<T> getProcessor();
}
