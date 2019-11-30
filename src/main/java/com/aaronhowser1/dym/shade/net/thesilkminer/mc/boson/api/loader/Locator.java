package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Lazy;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Unit;

import javax.annotation.Nonnull;
import java.util.List;

@FunctionalInterface
public interface Locator {
    @Nonnull List<Lazy<Location>> getLocations();

    @Nonnull
    default Unit clean() {
        return Unit.UNIT;
    }
}
