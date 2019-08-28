package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagDoubleFactory extends NbtTagValueRequiringFactory<NBTTagDouble> {

    @Nonnull
    @Override
    public NBTTagDouble parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name) {
        return new NBTTagDouble(this.toDouble(value, true));
    }
}
