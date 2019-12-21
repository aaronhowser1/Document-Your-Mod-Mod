package com.aaronhowser1.dymm.module.base.nbt;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.NBTTagEnd;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class EndNbtFactory implements NbtFactory<NBTTagEnd> {
    @Nonnull
    @Override
    public NBTTagEnd parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        if (!value.isJsonNull()) throw new JsonSyntaxException("A tag of type 'end' must have 'null' as a value");
        return new NBTTagEnd();
    }
}
