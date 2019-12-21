package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagFloat;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class FloatNbtFactory implements NbtFactory<NBTTagFloat> {
    @Nonnull
    @Override
    public NBTTagFloat parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        return new NBTTagFloat(JsonUtilities.asFloat(value, keyName));
    }
}
