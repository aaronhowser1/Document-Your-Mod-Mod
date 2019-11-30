package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Nullable;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public interface Location {
    @Nonnull Path getPath();
    @Nonnull Nullable<String> getFriendlyName();
    @Nonnull Nullable<Context> getAdditionalContext();
}
