package com.aaronhowser1.documentmod.json.stacks;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.json.StackFactory;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLongArray;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemStackFactory implements StackFactory {

    @Nonnull
    @Override
    public List<ItemStack> parseFromJson(@Nonnull final JsonObject jsonObject) {
        final String registryName = JsonUtils.getString(jsonObject, "registry_name");
        final int metadata = JsonUtils.getInt(jsonObject, "metadata", 0);
        final NBTTagCompound base = this.parseNbt(jsonObject);
        final Item item = this.getItemFromRegistryName(registryName);
        if (item == null) return ImmutableList.of();
        final ItemStack targetStack = new ItemStack(item, 1, metadata);
        if (base != null) {
            targetStack.deserializeNBT(base);
        }
        return ImmutableList.of(targetStack);
    }

    @Nullable
    protected Item getItemFromRegistryName(@Nonnull final String registryName) {
        final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
        if (item == null || item == Items.AIR) {
            DocumentMod.logger.warn("Item with given registry name '" + registryName + "' does not exist. Skipping");
            return null;
        }
        return item;
    }

    @Nullable
    private NBTTagCompound parseNbt(@Nonnull final JsonObject jsonObject) {
        if (!jsonObject.has("nbt")) return null;
        if (!jsonObject.get("nbt").isJsonObject()) throw new JsonSyntaxException("nbt must be an object!");

        final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("id", "minecraft:air");
        compound.setByte("Count", (byte) 1);
        compound.setShort("Damage", (short) 0);

        final JsonObject nbt = jsonObject.get("nbt").getAsJsonObject();
        final NBTTagCompound tag = new NBTTagCompound();

        this.parseNbtTagCompound(nbt, tag, 0);

        compound.setTag("tag", tag);

        return compound;
    }

    private void parseNbtTagCompound(@Nonnull final JsonObject jsonObject, @Nonnull final NBTTagCompound compound, final int recursionLevel) {
        if (recursionLevel >= 10) {
            throw new JsonSyntaxException("You're more than 10 levels deep! This is a bit too much for NBT, you know?");
        }
        jsonObject.entrySet().forEach(entry -> {
            final String key = entry.getKey();
            final JsonObject value = JsonUtils.getJsonObject(entry.getValue(), key);
            this.parseNbtTag(compound, key, value, recursionLevel + 1);
        });
    }

    private void parseNbtTag(@Nonnull final NBTTagCompound compound, @Nonnull final String key, @Nonnull final JsonObject value, final int recursionLevel) {
        if (recursionLevel >= 10) {
            throw new JsonSyntaxException("You're more than 10 levels deep! This is a bit too much for NBT, you know?");
        }
        final String nbtType = JsonUtils.getString(value, "type");
        switch (nbtType) {
            case "end":
                compound.setTag(key, new NBTTagEnd());
                break;
            case "byte":
                compound.setByte(key, (byte) JsonUtils.getInt(value, "value"));
                break;
            case "short":
                compound.setShort(key, (short) JsonUtils.getInt(value, "value"));
                break;
            case "int":
                compound.setInteger(key, JsonUtils.getInt(value, "value"));
                break;
            case "long":
                compound.setLong(key, (long) JsonUtils.getInt(value, "value"));
                break;
            case "float":
                compound.setFloat(key, JsonUtils.getFloat(value, "value"));
                break;
            case "double":
                compound.setDouble(key, (double) JsonUtils.getFloat(value, "value"));
                break;
            case "byte_array":
                final JsonArray jsonByteArray = JsonUtils.getJsonArray(value, "value");
                final byte[] byteArray = new byte[jsonByteArray.size()];
                for (int i = 0; i < byteArray.length; ++i) {
                    byteArray[i] = (byte) JsonUtils.getInt(jsonByteArray.get(i), key + "[" + i + "]");
                }
                compound.setByteArray(key, byteArray);
                break;
            case "string":
                compound.setString(key, JsonUtils.getString(value, "value"));
                break;
            case "list":
                final JsonArray jsonTagListArray = JsonUtils.getJsonArray(value, "value");
                final NBTTagList nbtTagList = new NBTTagList();
                for (int i = 0; i < jsonTagListArray.size(); ++i) {
                    final NBTTagCompound throwAwayCompound = new NBTTagCompound();
                    final String throwAwayKey = "item_" + i;
                    final JsonObject nbtTag = JsonUtils.getJsonObject(jsonTagListArray.get(i), key + "[" + i + "]");
                    this.parseNbtTag(throwAwayCompound, throwAwayKey, nbtTag, recursionLevel + 1);
                    nbtTagList.appendTag(throwAwayCompound.getTag(throwAwayKey));
                }
                compound.setTag(key, nbtTagList);
                break;
            case "compound":
                final NBTTagCompound childCompound = new NBTTagCompound();
                this.parseNbtTagCompound(JsonUtils.getJsonObject(value, "value"), childCompound, recursionLevel + 1);
                compound.setTag(key, childCompound);
                break;
            case "int_array":
                final JsonArray jsonIntArray = JsonUtils.getJsonArray(value, "value");
                final int[] intArray = new int[jsonIntArray.size()];
                for (int i = 0; i < intArray.length; ++i) {
                    intArray[i] = JsonUtils.getInt(jsonIntArray.get(i), key + "[" + i + "]");
                }
                compound.setIntArray(key, intArray);
                break;
            case "long_array":
                final JsonArray jsonLongArray = JsonUtils.getJsonArray(value, "value");
                final long[] longArray = new long[jsonLongArray.size()];
                for (int i = 0; i < longArray.length; ++i) {
                    longArray[i] = (long) JsonUtils.getInt(jsonLongArray.get(i), key + "[" + i + "]");
                }
                final NBTTagLongArray nbtTagLongArray = new NBTTagLongArray(longArray);
                compound.setTag(key, nbtTagLongArray);
                break;
            default:
                throw new JsonParseException("The given type " + nbtType + " is not a valid NBT type");
        }
    }
}
