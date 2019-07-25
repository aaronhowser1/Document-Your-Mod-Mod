package com.aaronhowser1.documentmod.json.stacks;

import com.aaronhowser1.documentmod.json.StackFactory;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.List;

public class EnchantedBookStackFactory implements StackFactory {
    @Nonnull
    @Override
    public List<ItemStack> parseFromJson(@Nonnull final JsonObject jsonObject) {
        final NonNullList<ItemStack> list = NonNullList.create();
        Items.ENCHANTED_BOOK.getSubItems(CreativeTabs.SEARCH, list);
        return ImmutableList.copyOf(list);
    }
}
