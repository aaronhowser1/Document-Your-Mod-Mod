package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.implementation;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.BosonApi;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.ContextKey;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Loader;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.LoaderBuilder;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.implementation.loader.BosonContextKey;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.implementation.loader.BosonLoader;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.implementation.naming.ResourceLocationBackedNameSpacedString;

import javax.annotation.Nonnull;

public final class BosonApiBinding implements BosonApi {
    @Nonnull
    @Override
    public NameSpacedString constructNameSpacedString(@Nonnull final Nullable<String> nameSpace, @Nonnull final String path) {
        return ResourceLocationBackedNameSpacedString.create(nameSpace, path);
    }

    @Nonnull
    @Override
    public <T> ContextKey<T> createLoaderContextKey(@Nonnull final String name, @Nonnull final KClass<T> type) {
        return BosonContextKey.create(name, type);
    }

    @Nonnull
    @Override
    public Loader buildLoader(@Nonnull final LoaderBuilder builder) {
        return BosonLoader.from(builder);
    }
}
