package com.aaronhowser1.dymm.api.loading;

import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.loading.metadata.MetadataListenerRegistry;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DocumentationLoader {
    @Nonnull ResourceLocation getIdentifier();
    @Nullable DocumentationEntry loadFromJson(@Nonnull final JsonObject object);

    default void registerMetadataListeners(@Nonnull final MetadataListenerRegistry registry) {}
    default void onLoad() {}
    default void onGlobalLoadingStateUnbinding() {}
}
