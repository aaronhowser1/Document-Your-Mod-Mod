package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.id;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.ApiBindings;

import javax.annotation.Nonnull;

public interface NameSpacedString extends Comparable<NameSpacedString> {
    @Nonnull
    static NameSpacedString invoke(@Nonnull final Nullable<String> nameSpace, @Nonnull final String path) {
        return ApiBindings.BOSON_API.invoke().constructNameSpacedString(nameSpace, path);
    }

    @Nonnull
    static NameSpacedString invoke(@Nonnull final String path) {
        return invoke(Nullable.get(null), path);
    }

    @Nonnull String getNameSpace();
    @Nonnull String getPath();
}
