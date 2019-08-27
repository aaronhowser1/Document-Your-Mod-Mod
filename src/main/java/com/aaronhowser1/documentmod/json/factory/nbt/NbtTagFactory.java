package com.aaronhowser1.documentmod.json.factory.nbt;

import com.aaronhowser1.documentmod.json.factory.Factory;
import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTBase;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface NbtTagFactory extends Factory<NbtTagFactory> {
    @Nonnull NBTBase parseFromJson(@Nonnull final JsonObject object);
}
