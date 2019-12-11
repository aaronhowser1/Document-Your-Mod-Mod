package com.aaronhowser1.dym.api.loading.metadata;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public interface MetadataListenerRegistry {
    void register(@Nonnull final ResourceLocation metadataIdentifier, @Nonnull final MetadataListener listener);
    @Nonnull List<MetadataListener> findAllFor(@Nonnull final ResourceLocation metadataIdentifier);
}
