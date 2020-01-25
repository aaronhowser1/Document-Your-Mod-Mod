package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagDouble;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class DoubleNbtFactory implements NbtFactory<NBTTagDouble> {
    @Nonnull
    @Override
    public NBTTagDouble parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        return new NBTTagDouble(JsonUtilities.asDouble(value, keyName));
    }
}
