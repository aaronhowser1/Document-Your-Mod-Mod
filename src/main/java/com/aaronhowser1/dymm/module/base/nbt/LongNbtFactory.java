package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagLong;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class LongNbtFactory implements NbtFactory<NBTTagLong> {
    @Nonnull
    @Override
    public NBTTagLong parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        return new NBTTagLong(JsonUtilities.asLong(value, keyName));
    }
}
