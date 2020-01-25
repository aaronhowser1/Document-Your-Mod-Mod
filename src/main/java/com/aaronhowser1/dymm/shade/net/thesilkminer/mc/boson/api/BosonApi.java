package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.ContextKey;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Loader;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.LoaderBuilder;

import javax.annotation.Nonnull;

public interface BosonApi {
    @Nonnull NameSpacedString constructNameSpacedString(@Nonnull final Nullable<String> nameSpace, @Nonnull final String path);
    @Nonnull <T> ContextKey<T> createLoaderContextKey(@Nonnull final String name, @Nonnull final KClass<T> type);
    @Nonnull Loader buildLoader(@Nonnull final LoaderBuilder builder);
}
