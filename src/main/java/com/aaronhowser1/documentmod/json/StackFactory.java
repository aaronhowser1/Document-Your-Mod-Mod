package com.aaronhowser1.documentmod.json;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

@FunctionalInterface
public interface StackFactory {
    @Nonnull List<ItemStack> parseFromJson(@Nonnull final JsonObject jsonObject);
}
