package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagLongFactory extends NbtTagValueRequiringFactory<NBTTagLong> {
    @Nonnull
    @Override
    public NBTTagLong parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name) {
        return new NBTTagLong(this.toLong(value, true));
    }
}
