package com.aaronhowser1.documentmod.json.factory.stack;

import com.aaronhowser1.documentmod.DocumentMod;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemMetaRangeStackFactory implements StackFactory {

    @Nonnull
    @Override
    public List<ItemStack> parseFromJson(@Nonnull final JsonObject jsonObject, @Nonnull final ResourceLocation name) {
        final String registryName = JsonUtils.getString(jsonObject, "registry_name");
        final int metaBegin = JsonUtils.getInt(jsonObject, "metadata_begin");
        final int metaEnd = JsonUtils.getInt(jsonObject, "metadata_end");
        final boolean startInclusive = !jsonObject.has("start_inclusive") || JsonUtils.getBoolean(jsonObject, "start_inclusive");
        final boolean endInclusive = !jsonObject.has("end_inclusive") || JsonUtils.getBoolean(jsonObject, "end_inclusive");

        final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
        if (item == null || item == Items.AIR) {
            DocumentMod.logger.warn("In entry '" + name + "': Item with given registry name '" + registryName + "' does not exist; skipping");
            return ImmutableList.of();
        }

        final List<ItemStack> stacks = Lists.newArrayList();
        for (int i = metaBegin; i <= metaEnd; ++i) {
            if (!startInclusive && i == metaBegin) continue;
            if (!endInclusive && i == metaEnd) continue;
            stacks.add(new ItemStack(item, 1, i));
        }

        return ImmutableList.copyOf(stacks);
    }
}
