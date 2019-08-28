package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagStringFactory extends NbtTagValueRequiringFactory<NBTTagString> {

    @Nonnull
    @Override
    public NBTTagString parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name) {
        return new NBTTagString(JsonUtils.getString(value, "value"));
    }
}
