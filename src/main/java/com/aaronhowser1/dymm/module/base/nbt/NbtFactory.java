package com.aaronhowser1.dymm.module.base.nbt;

import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTBase;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface NbtFactory<T extends NBTBase> {
    @Nonnull T parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel);
}
