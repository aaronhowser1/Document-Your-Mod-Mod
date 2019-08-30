package com.aaronhowser1.documentmod.event;

import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

public final class ChangeMetadataEvent extends Event {
    private final ModMetadata metadata;

    public ChangeMetadataEvent(@Nonnull final ModMetadata metadata) {
        this.metadata = metadata;
    }

    @Nonnull
    public ModMetadata getMetadata() {
        return this.metadata;
    }
}
