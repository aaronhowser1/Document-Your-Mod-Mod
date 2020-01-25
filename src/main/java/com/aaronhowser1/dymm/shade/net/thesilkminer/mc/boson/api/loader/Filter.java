package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Filter {
    boolean canLoad(@Nonnull final Location location);
}
