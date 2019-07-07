package com.aaronhowser1.documentmod.quark;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.*;
import static com.aaronhowser1.documentmod.config.DYMMConfig.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

@GameRegistry.ObjectHolder("minecraft")
public class QuarkVanillaItems
{
    public static void init()
    {
//        //TODO Check the Quark config and only put descriptions on items that are enabled
        if(vanillaitemschangedbyquark.infoDispenser) addBlockInfo(DISPENSER, "documentationmod.quark.vanilla.dispenser");
        if(vanillaitemschangedbyquark.infoStickyPiston) addBlockInfo(STICKY_PISTON, "documentationmod.quark.vanilla.stickypiston");
        if(vanillaitemschangedbyquark.infoLead) addItemInfo(LEAD, "documentationmod.quark.vanilla.lead");

        Item[] boats = {BOAT, SPRUCE_BOAT, BIRCH_BOAT, JUNGLE_BOAT, ACACIA_BOAT, DARK_OAK_BOAT};
        for (Item i: boats)
        {
            if(vanillaitemschangedbyquark.infoBoat) addItemInfo(i, "documentationmod.quark.vanilla.boat");
        }

        Block[] shulkerBoxes = {WHITE_SHULKER_BOX, ORANGE_SHULKER_BOX, MAGENTA_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, YELLOW_SHULKER_BOX, LIME_SHULKER_BOX, PINK_SHULKER_BOX, GRAY_SHULKER_BOX, SILVER_SHULKER_BOX, CYAN_SHULKER_BOX, PURPLE_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, GREEN_SHULKER_BOX, RED_SHULKER_BOX, BLACK_SHULKER_BOX};
        for (Block b: shulkerBoxes)
        {
            if(vanillaitemschangedbyquark.infoShulkerBoxExplosion) addBlockInfo(b, "documentationmod.quark.vanilla.shulkerexplosion");
            if(vanillaitemschangedbyquark.infoShulkerBoxHover) addBlockInfo(b, "documentationmod.quark.vanilla.shulkerhover");
            if(vanillaitemschangedbyquark.infoShulkerBoxAdd) addBlockInfo(b, "documentationmod.quark.vanilla.shulkeradd");
        }

        if(vanillaitemschangedbyquark.infoCompass) addItemInfo(COMPASS, "documentationmod.quark.vanilla.compass");
        if(vanillaitemschangedbyquark.infoLadder) addBlockInfo(LADDER, "documentationmod.quark.vanilla.ladder");
        if(vanillaitemschangedbyquark.infoArmorStand) addItemInfo(ARMOR_STAND, "documentationmod.quark.vanilla.armorstand");
        if(vanillaitemschangedbyquark.infoSign) addItemInfo(SIGN, "documentationmod.quark.vanilla.sign");
        if(vanillaitemschangedbyquark.infoEmeraldBlock) addBlockInfo(EMERALD_BLOCK, "documentationmod.quark.vanilla.emeraldblock");
        if(vanillaitemschangedbyquark.infoElytra) addItemInfo(ELYTRA, "documentationmod.quark.vanilla.elytra");
        if(vanillaitemschangedbyquark.infoGlowstoneDust) addItemInfo(GLOWSTONE_DUST, "documentationmod.quark.vanilla.glowstonedust");
        if(vanillaitemschangedbyquark.infoGunpowder) addItemInfo(GUNPOWDER, "documentationmod.quark.vanilla.gunpowder");
        if(vanillaitemschangedbyquark.infoDragonBreath) addItemInfo(DRAGON_BREATH, "documentationmod.quark.vanilla.dragonbreath");
        if(vanillaitemschangedbyquark.infoMap) addItemInfo(MAP, "documentationmod.quark.vanilla.map");
        if(vanillaitemschangedbyquark.infoMap) addItemInfo(FILLED_MAP, "documentationmod.quark.vanilla.map");
        //TODO figure out why the enchanted book doesn't work
        if(vanillaitemschangedbyquark.infoEnchantedBook) addItemInfo(ENCHANTED_BOOK, "documentationmod.quark.vanilla.enchantedbook");
    }
}
