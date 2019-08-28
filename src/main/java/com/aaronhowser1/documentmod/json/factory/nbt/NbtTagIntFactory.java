package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagIntFactory extends NbtTagValueRequiringFactory<NBTTagInt> {

    @Nonnull
    @Override
    public NBTTagInt parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name) {
        return new NBTTagInt(this.tryIntNarrowing(this.toLong(value, false)));
    }
}
