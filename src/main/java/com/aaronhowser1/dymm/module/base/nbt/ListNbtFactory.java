package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.JsonUtilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class ListNbtFactory implements NbtFactory<NBTTagList> {
    @Nonnull
    @Override
    public NBTTagList parseFromJson(@Nonnull final String keyName, @Nonnull final JsonElement value, final int depthLevel) {
        // TODO Maybe a better API?
        final int colon = keyName.lastIndexOf(':');
        final String name = keyName.substring(0, colon);
        final String insideType = keyName.substring(colon + 1);

        final JsonArray array = JsonUtilities.asJsonArray(value, name);
        final NBTTagList list = new NBTTagList();
        for (int i = 0; i < array.size(); i++) {
            final JsonElement element = array.get(i);
            final int newDepthLevel = depthLevel + 1;
            if (newDepthLevel > 10) throw new JsonParseException("Reached NBT that is too deep for parsing: more than 10 nested levels");
            final NBTBase base = NbtFactoryRegistry.INSTANCE.getForType(insideType).parseFromJson(name + "[" + i + "]", element, newDepthLevel);
            list.appendTag(base);
        }
        return list;
    }
}
