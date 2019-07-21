package com.aaronhowser1.documentmod.json;

import com.aaronhowser1.documentmod.DocumentMod;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ModDocumentation {

    public enum Type {
        ITEM,
        BLOCK
    }

    private static final ModDocumentation EMPTY_MOD_DOCUMENTATION = new ModDocumentation(ItemStack.EMPTY, Maps.newHashMap());

    private final ItemStack itemStack;
    private final Map<String, List<String>> translationKeys;

    private ModDocumentation(@Nonnull final ItemStack itemStack, @Nonnull final Map<String, List<String>> translationKeys) {
        this.itemStack = itemStack;
        this.translationKeys = translationKeys;
    }

    @Nonnull
    public static ModDocumentation create(@Nonnull final JsonObject object, @Nonnull final ResourceLocation name) {
        final ItemStack stack = parseItemStackJsonObject(JsonUtils.getJsonObject(object, "for"));
        if (stack == null || stack.isEmpty()) return EMPTY_MOD_DOCUMENTATION;

        final Map<String, List<String>> translationKeys = parseTranslationKeys(JsonUtils.getJsonObject(object, "documentation"));

        return new ModDocumentation(stack, translationKeys);
    }

    @Nullable
    private static ItemStack parseItemStackJsonObject(@Nonnull final JsonObject jsonObject) {
        final Type type = Type.valueOf(JsonUtils.getString(jsonObject, "type").toUpperCase(Locale.ENGLISH));
        final String registryName = JsonUtils.getString(jsonObject, "registry_name");
        final int metadata = JsonUtils.getInt(jsonObject, "metadata", 0);
        final NBTTagCompound base = parseNbt(jsonObject);
        ItemStack targetStack = null;
        switch (type) {
            case ITEM:
                final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
                if (item == null || item == Items.AIR) {
                    DocumentMod.logger.info("Item with given registry name '" + registryName + "' does not exist. Skipping");
                } else {
                    targetStack = new ItemStack(item, 1, metadata);
                }
                break;
            case BLOCK:
                final Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(registryName));
                if (block == null || block == Blocks.AIR) {
                    DocumentMod.logger.info("Block with given registry name '" + registryName + "' does not exist. Skipping");
                    targetStack = null;
                    break;
                }
                final ItemStack tmpStack = new ItemStack(Item.getItemFromBlock(block), 1, metadata);
                if (tmpStack.isEmpty()) {
                    DocumentMod.logger.info("Block '" + registryName + "' is not supported: does not have an ItemBlock");
                } else {
                    targetStack = tmpStack;
                }
        }
        if (targetStack == null) {
            return null;
        }
        if (base != null) {
            targetStack.deserializeNBT(base);
        }
        return targetStack;
    }

    @Nullable
    private static NBTTagCompound parseNbt(@Nonnull final JsonObject jsonObject) {
        if (!jsonObject.has("nbt")) return null;
        if (!jsonObject.get("nbt").isJsonObject()) throw new JsonSyntaxException("nbt must be an object!");

        final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("id", "minecraft:air");
        compound.setByte("Count", (byte) 1);
        compound.setShort("Damage", (short) 0);

        final JsonObject nbt = jsonObject.get("nbt").getAsJsonObject();
        final NBTTagCompound tag = new NBTTagCompound();
        // TODO Deserialize here

        compound.setTag("tag", tag);

        return compound;
    }

    @Nonnull
    private static Map<String, List<String>> parseTranslationKeys(@Nonnull final JsonObject object) {
        if (!object.has("en_us")) throw new JsonSyntaxException("Documentation object must have one en_us tag");

        final Map<String, List<String>> map = Maps.newHashMap();
        object.entrySet().forEach(entry -> {
            final List<String> entries = map.computeIfAbsent(entry.getKey(), s -> Lists.newArrayList());
            final JsonElement value = entry.getValue();
            if (!value.isJsonArray()) throw new JsonParseException("Entries must be Json Arrays");
            final JsonArray strings = value.getAsJsonArray();
            strings.forEach(jsonString -> {
                final String string = JsonUtils.getString(jsonString, entry.getKey() + "[?]");
                entries.add(string);
            });
        });
        return map;
    }

    @Nonnull
    private ItemStack getReferredStack() {
        return this.itemStack.copy();
    }

    @Nonnull
    private Map<String, List<String>> getTranslationKeys() {
        return ImmutableMap.copyOf(this.translationKeys);
    }

    @Nonnull
    private List<String> getStringsFor(@Nonnull final String language) {
        return ImmutableList.copyOf(this.translationKeys.getOrDefault(language, Lists.newArrayList()));
    }
}
