package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Unit;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Loader {
    @Nonnull Unit load();
}
