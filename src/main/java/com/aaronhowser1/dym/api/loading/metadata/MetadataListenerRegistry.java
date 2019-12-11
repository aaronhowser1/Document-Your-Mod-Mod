package com.aaronhowser1.dym.api.loading.metadata;

import javax.annotation.Nonnull;
import java.util.List;

public interface MetadataListenerRegistry {
    void register(@Nonnull final String metadataIdentifier, @Nonnull final MetadataListener listener);
    @Nonnull List<MetadataListener> findAllFor(@Nonnull final String metadataIdentifier);
}
