package com.aaronhowser1.documentmod.json.factory.nbt;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.json.DocumentationLoader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagListFactory extends NbtTagValueRequiringFactory<NBTTagList> {
    @Nonnull
    @Override
    public NBTTagList parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name, final int recursionLevel) {
        final JsonArray jsonList = this.toJsonArray(value);
        final NBTTagList nbtList = new NBTTagList();
        for (int i = 0; i < jsonList.size(); i++) {
            final JsonObject nbtTag = JsonUtils.getJsonObject(jsonList.get(i), "value[" + i + "]");
            final String type = JsonUtils.getString(nbtTag, "type");
            @SuppressWarnings("unchecked")
            final NbtTagFactory<?> factory = DocumentationLoader.INSTANCE.getFactory(NbtTagFactory.class, new ResourceLocation(DocumentMod.MOD_ID, type));
            if (factory == null) {
                throw new JsonSyntaxException("Unable to find NBT factory for NBT type '" + type + "'. Is it a valid NBT type?");
            }
            final NBTBase tag = factory.parseFromJson(nbtTag, name, recursionLevel + 1);
            nbtList.appendTag(tag);
        }
        return nbtList;
    }


}
