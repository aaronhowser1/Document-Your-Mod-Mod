package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class CompoundNbtFactory implements NbtFactory<NBTTagCompound> {
    @Nonnull
    @Override
    public NBTTagCompound parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        final NBTTagCompound compound = new NBTTagCompound();
        final JsonObject jsonCompound = JsonUtilities.asJsonObject(value, keyName);
        jsonCompound.entrySet().forEach(entry -> {
            final String longKey = entry.getKey();
            final int colon = longKey.lastIndexOf(':');
            final String key = longKey.substring(0, colon);
            final String mnemonic = longKey.substring(colon + 1);
            final int newDepthLevel = depthLevel + 1;
            if (newDepthLevel > 10) throw new JsonParseException("Reached NBT that is too deep for parsing: more than 10 nested levels");
            compound.setTag(key, NbtFactoryRegistry.INSTANCE.getForType(mnemonic).parseFromJson(key, entry.getValue(), newDepthLevel));
        });
        return compound;
    }
}
