package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagFloatFactory extends NbtTagValueRequiringFactory<NBTTagFloat> {

    @Nonnull
    @Override
    public NBTTagFloat parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name) {
        return new NBTTagFloat(this.tryFloatNarrowing(this.toDouble(value, false)));
    }
}
