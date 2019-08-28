package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagLongArray;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagLongArrayFactory extends NbtTagValueRequiringFactory<NBTTagLongArray> {
    @Nonnull
    @Override
    public NBTTagLongArray parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name) {
        final JsonArray jsonArray = this.toJsonArray(value);
        final long[] longArray = new long[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            longArray[i] = this.toLong(jsonArray.get(i), true);
        }
        return new NBTTagLongArray(longArray);
    }
}
