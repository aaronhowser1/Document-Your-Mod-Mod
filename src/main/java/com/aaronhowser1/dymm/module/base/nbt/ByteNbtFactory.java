package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagByte;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class ByteNbtFactory implements NbtFactory<NBTTagByte> {
    @Nonnull
    @Override
    public NBTTagByte parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        return new NBTTagByte(JsonUtilities.asByte(value, keyName));
    }
}
