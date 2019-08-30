package com.aaronhowser1.documentmod.api.utility;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public enum ContainerFinder {
    INSTANCE;

    @Nullable
    public ModContainer findContainerFromId(@Nullable final String id) {
        if (id == null) return null;
        return Loader.instance().getActiveModList().stream()
                .filter(it -> it.getModId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Nonnull
    public ModContainer ensureContainerFromId(@Nonnull final String id) {
        return Objects.requireNonNull(this.findContainerFromId(id));
    }
}
