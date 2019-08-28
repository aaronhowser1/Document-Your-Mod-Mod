package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagIntArrayFactory extends NbtTagValueRequiringFactory<NBTTagIntArray> {
    @Nonnull
    @Override
    public NBTTagIntArray parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name) {
        final JsonArray jsonArray = this.toJsonArray(value);
        final int[] intArray = new int[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            intArray[i] = this.tryIntNarrowing(this.toLong(jsonArray.get(i), false));
        }
        return new NBTTagIntArray(intArray);
    }
}
