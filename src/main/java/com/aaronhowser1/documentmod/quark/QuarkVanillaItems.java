package com.aaronhowser1.documentmod.quark;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.automation.feature.*;
import vazkii.quark.client.feature.*;
import vazkii.quark.decoration.feature.*;
import vazkii.quark.management.feature.*;
import vazkii.quark.misc.feature.*;
import vazkii.quark.tweaks.feature.*;
import vazkii.quark.vanity.feature.*;

import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.*;
import static com.aaronhowser1.documentmod.config.DYMMConfig.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

@GameRegistry.ObjectHolder("minecraft")
public class QuarkVanillaItems
{
    public static void init()
    {
        if(vanillaitemschangedbyquark.infoDispenser && (ModuleLoader.isFeatureEnabled(DispenserRecords.class) && ModuleLoader.isFeatureEnabled(DispensersPlaceBlocks.class) && ModuleLoader.isFeatureEnabled(DispensersPlaceSeeds.class))) {
            addBlockInfo(DISPENSER, "documentationmod.quark.vanilla.dispenser");
        }
        if(vanillaitemschangedbyquark.infoStickyPiston && ModuleLoader.isFeatureEnabled(PistonsMoveTEs.class)) {
            addBlockInfo(STICKY_PISTON, "documentationmod.quark.vanilla.stickypiston");
        }
        if(vanillaitemschangedbyquark.infoLead && ModuleLoader.isFeatureEnabled(TieFences.class)) {
            addItemInfo(LEAD, "documentationmod.quark.vanilla.lead");
        }
        Item[] boats = {BOAT, SPRUCE_BOAT, BIRCH_BOAT, JUNGLE_BOAT, ACACIA_BOAT, DARK_OAK_BOAT};
        for (Item i: boats)
        {
            if(vanillaitemschangedbyquark.infoBoat && ModuleLoader.isFeatureEnabled(ChestsInBoats.class)) {
                addItemInfo(i, "documentationmod.quark.vanilla.boat");
            }
        }
        Block[] shulkerBoxes = {WHITE_SHULKER_BOX, ORANGE_SHULKER_BOX, MAGENTA_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, YELLOW_SHULKER_BOX, LIME_SHULKER_BOX, PINK_SHULKER_BOX, GRAY_SHULKER_BOX, SILVER_SHULKER_BOX, CYAN_SHULKER_BOX, PURPLE_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, GREEN_SHULKER_BOX, RED_SHULKER_BOX, BLACK_SHULKER_BOX};
        for (Block b: shulkerBoxes)
        {
            if(vanillaitemschangedbyquark.infoShulkerBoxExplosion && ModuleLoader.isFeatureEnabled(BlastproofShulkerBoxes.class)) {
                addBlockInfo(b, "documentationmod.quark.vanilla.shulkerexplosion");
            }
            if(vanillaitemschangedbyquark.infoShulkerBoxHover && ModuleLoader.isFeatureEnabled(ShulkerBoxTooltip.class)) {
                addBlockInfo(b, "documentationmod.quark.vanilla.shulkerhover");
            }
            if(vanillaitemschangedbyquark.infoShulkerBoxAdd && ModuleLoader.isFeatureEnabled(RightClickAddToShulkerBox.class)) {
                addBlockInfo(b, "documentationmod.quark.vanilla.shulkeradd");
            }
        }
        if(vanillaitemschangedbyquark.infoCompass && ModuleLoader.isFeatureEnabled(CompassesWorkEverywhere.class)) {
            addItemInfo(COMPASS, "documentationmod.quark.vanilla.compass");
        }
        if(vanillaitemschangedbyquark.infoLadder && ModuleLoader.isFeatureEnabled(DeployLaddersDown.class)) {
            addBlockInfo(LADDER, "documentationmod.quark.vanilla.ladder");
        }
        if(vanillaitemschangedbyquark.infoArmorStand && ModuleLoader.isFeatureEnabled(QuickArmorSwapping.class)) {
            addItemInfo(ARMOR_STAND, "documentationmod.quark.vanilla.armorstand");
        }
        if(vanillaitemschangedbyquark.infoSign && ModuleLoader.isFeatureEnabled(RightClickSignEdit.class)) {
            addItemInfo(SIGN, "documentationmod.quark.vanilla.sign");
        }
        if(vanillaitemschangedbyquark.infoEmeraldBlock && ModuleLoader.isFeatureEnabled(VillagerPursueEmeralds.class)) {
            addBlockInfo(EMERALD_BLOCK, "documentationmod.quark.vanilla.emeraldblock");
        }
        if(vanillaitemschangedbyquark.infoElytra && ModuleLoader.isFeatureEnabled(DyableElytra.class)) {
            addItemInfo(ELYTRA, "documentationmod.quark.vanilla.elytra");
        }
        if(ModuleLoader.isFeatureEnabled(PlaceVanillaDusts.class)) {
            if(vanillaitemschangedbyquark.infoGlowstoneDust){
                addItemInfo(GLOWSTONE_DUST, "documentationmod.quark.vanilla.glowstonedust");
            }
            if(vanillaitemschangedbyquark.infoGunpowder){
                addItemInfo(GUNPOWDER, "documentationmod.quark.vanilla.gunpowder");
            }
        }
        if(vanillaitemschangedbyquark.infoDragonBreath && ModuleLoader.isFeatureEnabled(ThrowableDragonBreath.class)) {
            addItemInfo(DRAGON_BREATH, "documentationmod.quark.vanilla.dragonbreath");
        }
        if(vanillaitemschangedbyquark.infoMap && ModuleLoader.isFeatureEnabled(MapTooltip.class)) {
            addItemInfo(MAP, "documentationmod.quark.vanilla.map");
            addItemInfo(FILLED_MAP, "documentationmod.quark.vanilla.map");
        }
        //TODO figure out why the enchanted book doesn't work
        if(vanillaitemschangedbyquark.infoEnchantedBook && ModuleLoader.isFeatureEnabled(EnchantedBooksShowItems.class)) {
            addItemInfo(ENCHANTED_BOOK, "documentationmod.quark.vanilla.enchantedbook");
        }
    }
}
