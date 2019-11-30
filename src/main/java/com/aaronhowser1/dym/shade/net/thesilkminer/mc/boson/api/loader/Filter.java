package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Filter {
    boolean canLoad(@Nonnull final Location location);
}
