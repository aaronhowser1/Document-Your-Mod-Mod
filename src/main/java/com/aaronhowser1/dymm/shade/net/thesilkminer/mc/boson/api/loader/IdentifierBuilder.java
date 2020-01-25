package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface IdentifierBuilder {
    @Nonnull NameSpacedString makeIdentifier(@Nonnull final Location location, @Nonnull final Nullable<Context> globalContext, @Nonnull final Nullable<Context> phaseContext);
}
