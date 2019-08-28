package com.aaronhowser1.documentmod.json.factory.nbt;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.json.DocumentationLoader;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class NbtTagCompoundFactory extends NbtTagValueRequiringFactory<NBTTagCompound> {
    @Nonnull
    @Override
    public NBTTagCompound parseFromJson(@Nonnull final JsonObject object, @Nonnull final ResourceLocation name, final int recursionLevel) {
        if (recursionLevel != 0) return super.parseFromJson(object, name, recursionLevel);
        // If recursion level is 0, we do not need to care about "value" or "type": we know it is an NBTTagCompound and
        // the entries of the JsonObject are what we need to parse. Basically, the given JsonObject is a value.
        return this.parseFromValue(object, name, 1); // Set recursion level so that we know how deep we are correctly
    }

    @Nonnull
    @Override
    public NBTTagCompound parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name, final int recursionLevel) {
        final NBTTagCompound compound = new NBTTagCompound();
        final JsonObject jsonCompound = JsonUtils.getJsonObject(value, "value");
        jsonCompound.entrySet().forEach(entry -> {
            final String key = entry.getKey();
            final JsonObject compoundValue = JsonUtils.getJsonObject(entry.getValue(), key);
            final String type = JsonUtils.getString(compoundValue, "type");
            @SuppressWarnings("unchecked")
            final NbtTagFactory<?> factory = DocumentationLoader.INSTANCE.getFactory(NbtTagFactory.class, new ResourceLocation(DocumentMod.MODID, type));
            if (factory == null) {
                throw new JsonSyntaxException("Unable to find NBT factory for NBT type '" + type + "'. Is it a valid NBT type?");
            }
            final NBTBase base = factory.parseFromJson(compoundValue, name, recursionLevel + 1);
            compound.setTag(key, base);
        });
        return compound;
    }
}
