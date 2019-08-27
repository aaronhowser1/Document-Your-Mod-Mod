package com.aaronhowser1.documentmod.json.factory.stack;

import com.aaronhowser1.documentmod.DocumentMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;

public class BlockStackFactory extends ItemStackFactory implements StackFactory {
    @Override
    protected Item getItemFromRegistryName(@Nonnull final String registryName, @Nonnull final ResourceLocation name) {
        final Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(registryName));
        if (block == null || block == Blocks.AIR) {
            DocumentMod.logger.warn("In entry '" + name + "': Block with given registry name '" + registryName + "' does not exist; skipping");
            return null;
        }
        final Item itemBlock = Item.getItemFromBlock(block);
        if (itemBlock == Items.AIR) {
            DocumentMod.logger.warn("In entry '" + name + "': Block '" + registryName + "' is not supported, because it does not have an ItemBlock");
            return null;
        }
        return itemBlock;
    }
}
