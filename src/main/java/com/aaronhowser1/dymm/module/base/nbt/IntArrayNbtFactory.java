package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagIntArray;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class IntArrayNbtFactory implements NbtFactory<NBTTagIntArray> {
    @Nonnull
    @Override
    public NBTTagIntArray parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        final JsonArray jsonArray = JsonUtilities.asJsonArray(value, keyName);
        final int[] intArray = new int[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            intArray[i] = JsonUtilities.asInt(jsonArray.get(i), keyName + "[" + i + "]");
        }
        return new NBTTagIntArray(intArray);
    }
}
