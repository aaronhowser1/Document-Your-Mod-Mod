package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.filter;

import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Filter;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Location;

import javax.annotation.Nonnull;
import java.nio.file.Files;

public final class RegularFileFilter implements Filter {
    private RegularFileFilter() {}

    @Nonnull
    public static RegularFileFilter create() {
        return new RegularFileFilter();
    }

    @Override
    public boolean canLoad(@Nonnull final Location location) {
        return Files.isRegularFile(location.getPath());
    }
}
