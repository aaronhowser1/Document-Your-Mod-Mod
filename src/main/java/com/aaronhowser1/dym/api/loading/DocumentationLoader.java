package com.aaronhowser1.dym.api.loading;

import com.aaronhowser1.dym.api.documentation.DocumentationEntry;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DocumentationLoader {
    @Nonnull ResourceLocation getIdentifier();
    @Nullable DocumentationEntry loadFromJson(@Nonnull final JsonObject object);

    default void onLoad() {}
}
