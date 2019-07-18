package com.aaronhowser1.documentmod;

import com.aaronhowser1.documentmod.naturescompass.NaturesCompass;
import com.aaronhowser1.documentmod.quark.QuarkItems;
import com.aaronhowser1.documentmod.quark.QuarkVanillaItems;
import com.aaronhowser1.documentmod.tinkersconstruct.TinkersConstruct;
import com.aaronhowser1.documentmod.twilightforest.TwilightForest;
import com.aaronhowser1.documentmod.waystones.Waystones;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.aaronhowser1.documentmod.config.DYMMConfig.*;

@JEIPlugin
public class DocumentModJEIIntegration implements IModPlugin
{
    public static IModRegistry registry;
    @Override
    public void register(IModRegistry r)
    {
        registry = r;
        if(quarksubcat.useQuark) {
            QuarkItems.init();
        }
        if(quarksubcat.useVanillaQuark) {
            QuarkVanillaItems.init();
        }
        if(useNaturesCompass) {
            NaturesCompass.init();
        }
        if(useTinkersConstruct) {
            TinkersConstruct.init();
        }
        if(useTwilightForest) {
            TwilightForest.init();
        }
        if(useWaystone) {
            Waystones.init();
        }
    }
    public static void addItemInfo(Item item, String desc)
    {
        if(item != Items.AIR) registry.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM, desc);
    }
    public static void addBlockInfo(Block block, String desc)
    {
        if(block != Blocks.AIR) registry.addIngredientInfo(new ItemStack(block), VanillaTypes.ITEM, desc);
    }
    public static void addItemWithDamageInfo(Item item, int meta, String desc)
    {
        if(item != Items.AIR) registry.addIngredientInfo(new ItemStack(item, 1, meta), VanillaTypes.ITEM, desc);
    }
    public static void addBlockWithDamageInfo(Block block, int meta, String desc)
    {
        if(block != Blocks.AIR) registry.addIngredientInfo(new ItemStack(block, 1, meta), VanillaTypes.ITEM, desc);
    }
}
