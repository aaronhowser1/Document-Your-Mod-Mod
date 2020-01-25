package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagShort;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class ShortNbtFactory implements NbtFactory<NBTTagShort> {
    @Nonnull
    @Override
    public NBTTagShort parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        return new NBTTagShort(JsonUtilities.asShort(value, keyName));
    }
}
