package com.aaronhowser1.documentmod.json.factory.nbt;

import com.aaronhowser1.documentmod.json.factory.Factory;
import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface NbtTagFactory<T extends NBTBase> extends Factory<NbtTagFactory<T>> {
    @Nonnull T parseFromJson(@Nonnull final JsonObject object, @Nonnull final ResourceLocation name, final int recursionLevel);
}
