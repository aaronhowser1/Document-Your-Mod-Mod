package com.aaronhowser1.documentmod.json.factory.stack;

import com.aaronhowser1.documentmod.json.factory.Factory;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

@FunctionalInterface
public interface StackFactory extends Factory<StackFactory> {
    @Nonnull List<ItemStack> parseFromJson(@Nonnull final JsonObject jsonObject);
}
