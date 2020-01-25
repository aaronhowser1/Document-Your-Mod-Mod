package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class StringNbtFactory implements NbtFactory<NBTTagString> {
    @Nonnull
    @Override
    public NBTTagString parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        return new NBTTagString(JsonUtilities.asString(value, keyName));
    }
}
