package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagShortFactory extends NbtTagValueRequiringFactory<NBTTagShort> {
    @Nonnull
    @Override
    public NBTTagShort parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name) {
        return new NBTTagShort(this.tryShortNarrowing(this.toLong(value, false)));
    }
}
