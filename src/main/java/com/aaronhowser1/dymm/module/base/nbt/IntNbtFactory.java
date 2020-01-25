package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagInt;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class IntNbtFactory implements NbtFactory<NBTTagInt> {
    @Nonnull
    @Override
    public NBTTagInt parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        return new NBTTagInt(JsonUtilities.asInt(value, keyName));
    }
}
