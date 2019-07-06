package com.aaronhowser1.documentmod;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

@GameRegistry.ObjectHolder("minecraft")
public class QuarkVanillaItems
{
    public static void init()
    {
        //TODO Check the Quark config and only put descriptions on items that are enabled
        addBlockInfo(DISPENSER, "documentationmod.quark.vanilla.dispenser");
        addBlockInfo(STICKY_PISTON, "documentationmod.quark.vanilla.stickypiston");
        addItemInfo(LEAD, "documentationmod.quark.vanilla.lead");
        addItemInfo(BOAT, "documentationmod.quark.vanilla.boat");
        addItemInfo(BOAT, "documentationmod.quark.vanilla.boat");
        addItemInfo(SPRUCE_BOAT, "documentationmod.quark.vanilla.boat");
        addItemInfo(BIRCH_BOAT, "documentationmod.quark.vanilla.boat");
        addItemInfo(JUNGLE_BOAT, "documentationmod.quark.vanilla.boat");
        addItemInfo(ACACIA_BOAT, "documentationmod.quark.vanilla.boat");
        addItemInfo(DARK_OAK_BOAT, "documentationmod.quark.vanilla.boat");

        Block[] shulkerBoxes = {WHITE_SHULKER_BOX, ORANGE_SHULKER_BOX, MAGENTA_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, YELLOW_SHULKER_BOX, LIME_SHULKER_BOX, PINK_SHULKER_BOX, GRAY_SHULKER_BOX, SILVER_SHULKER_BOX, CYAN_SHULKER_BOX, PURPLE_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, GREEN_SHULKER_BOX, RED_SHULKER_BOX, BLACK_SHULKER_BOX};
        for (Block b: shulkerBoxes)
        {
            addBlockInfo(b, "documentationmod.quark.vanilla.shulkerexplosion");
            addBlockInfo(b, "documentationmod.quark.vanilla.shulkerhover");
            addBlockInfo(b, "documentationmod.quark.vanilla.shulkeradd");
        }

        addItemInfo(COMPASS, "documentationmod.quark.vanilla.compass");
        addBlockInfo(LADDER, "documentationmod.quark.vanilla.ladder");
        addItemInfo(ARMOR_STAND, "documentationmod.quark.vanilla.armorstand");
        addItemInfo(SIGN, "documentationmod.quark.vanilla.sign");
        addBlockInfo(EMERALD_BLOCK, "documentationmod.quark.vanilla.emeraldblock");
        addItemInfo(ELYTRA, "documentationmod.quark.vanilla.elytra");
        addItemInfo(GLOWSTONE_DUST, "documentationmod.quark.vanilla.glowstonedust");
        addItemInfo(GUNPOWDER, "documentationmod.quark.vanilla.gunpowder");
        addItemInfo(DRAGON_BREATH, "documentationmod.quark.vanilla.dragonbreath");
        addItemInfo(MAP, "documentationmod.quark.vanilla.map");
        addItemInfo(FILLED_MAP, "documentationmod.quark.vanilla.map");
        //TODO figure out why the enchanted book doesn't work
        addItemInfo(ENCHANTED_BOOK, "documentationmod.quark.vanilla.enchantedbook");
    }
}