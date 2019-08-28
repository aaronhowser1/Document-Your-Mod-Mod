package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagEndFactory implements NbtTagFactory<NBTTagEnd> {

    @Nonnull
    @Override
    public NBTTagEnd parseFromJson(@Nonnull final JsonObject object, @Nonnull final ResourceLocation name, final int recursionLevel) {
        return new NBTTagEnd();
    }
}
