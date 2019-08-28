package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagByteArrayFactory extends NbtTagValueRequiringFactory<NBTTagByteArray> {
    @Nonnull
    @Override
    public NBTTagByteArray parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name) {
        final JsonArray jsonArray = this.toJsonArray(value);
        final byte[] byteArray = new byte[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            byteArray[i] = this.tryByteNarrowing(this.toLong(jsonArray.get(i), false));
        }
        return new NBTTagByteArray(byteArray);
    }
}
