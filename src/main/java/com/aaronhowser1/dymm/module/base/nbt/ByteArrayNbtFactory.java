package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagByteArray;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class ByteArrayNbtFactory implements NbtFactory<NBTTagByteArray> {
    @Nonnull
    @Override
    public NBTTagByteArray parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        final JsonArray jsonArray = JsonUtilities.asJsonArray(value, keyName);
        final byte[] byteArray = new byte[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            byteArray[i] = JsonUtilities.asByte(jsonArray.get(i), keyName + "[" + i + "]");
        }
        return new NBTTagByteArray(byteArray);
    }
}
