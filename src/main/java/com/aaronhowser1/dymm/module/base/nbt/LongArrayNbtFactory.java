package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagLongArray;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class LongArrayNbtFactory implements NbtFactory<NBTTagLongArray> {
    @Nonnull
    @Override
    public NBTTagLongArray parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        final JsonArray jsonArray = JsonUtilities.asJsonArray(value, keyName);
        final long[] longArray = new long[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            longArray[i] = JsonUtilities.asLong(jsonArray.get(i), keyName + "[" + i + "]");
        }
        return new NBTTagLongArray(longArray);
    }
}
