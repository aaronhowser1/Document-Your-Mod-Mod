package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagByteFactory extends NbtTagValueRequiringFactory<NBTTagByte> {

    @Nonnull
    @Override
    public NBTTagByte parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name) {
        return new NBTTagByte(this.tryByteNarrowing(this.toLong(value, false)));
    }
}
