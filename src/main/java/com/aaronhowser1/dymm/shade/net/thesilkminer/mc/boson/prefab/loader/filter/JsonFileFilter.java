package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.filter;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.With;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Filter;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Location;

import javax.annotation.Nonnull;

public final class JsonFileFilter implements Filter {
    private JsonFileFilter() {}

    @Nonnull
    public static JsonFileFilter create() {
        return new JsonFileFilter();
    }

    @Override
    public boolean canLoad(@Nonnull final Location location) {
        return With.with(location.getPath().getFileName(), $this$receiver -> $this$receiver.toString().endsWith(".json"));
    }
}
