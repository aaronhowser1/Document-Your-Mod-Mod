package com.aaronhowser1.documentmod;

import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

@GameRegistry.ObjectHolder("minecraft")
public class QuarkVanillaItems
{
    public static void init()
    {
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
        addBlockInfo(WHITE_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(ORANGE_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(MAGENTA_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(LIGHT_BLUE_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(YELLOW_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(LIME_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(PINK_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(GRAY_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(SILVER_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(CYAN_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(PURPLE_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(BLUE_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(BROWN_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(GREEN_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(RED_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addBlockInfo(BLACK_SHULKER_BOX, "documentationmod.quark.vanilla.shulker");
        addItemInfo(COMPASS, "documentationmod.quark.vanilla.compass");
        addBlockInfo(LADDER, "documentationmod.quark.vanilla.ladder");
        addItemInfo(ARMOR_STAND, "documentationmod.quark.vanilla.armorstand");
        addItemInfo(SIGN, "documentationmod.quark.vanilla.sign");
        addBlockInfo(EMERALD_BLOCK, "documentationmod.quark.vanilla.emeraldblock");
        addItemInfo(ELYTRA, "documentationmod.quark.vanilla.elytra");
        addItemInfo(GLOWSTONE_DUST, "documentationmod.quark.vanilla.glowstonedust");
        addItemInfo(GUNPOWDER, "documentationmod.quark.vanilla.gunpowder");
        addItemInfo(DRAGON_BREATH, "documentationmod.quark.vanilla.dragonbreath");
    }
}