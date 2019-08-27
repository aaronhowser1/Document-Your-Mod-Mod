package com.aaronhowser1.documentmod.json.factory.stack;

import com.aaronhowser1.documentmod.DocumentMod;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemAllNbtInSearchStackFactory implements StackFactory {
    @Nonnull
    @Override
    public List<ItemStack> parseFromJson(@Nonnull final JsonObject jsonObject, @Nonnull final ResourceLocation name) {
        final String registryName = JsonUtils.getString(jsonObject, "registry_name");
        final Item itemBase = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
        if (itemBase == null || itemBase == Items.AIR) {
            DocumentMod.logger.warn("In entry '" + name + ": NBT-based item with given registry name '" + registryName + "' does not exist; skipping");
            return ImmutableList.of();
        }
        final NonNullList<ItemStack> itemStacks = NonNullList.create();
        itemBase.getSubItems(CreativeTabs.SEARCH, itemStacks);
        return ImmutableList.copyOf(itemStacks);
    }
}
