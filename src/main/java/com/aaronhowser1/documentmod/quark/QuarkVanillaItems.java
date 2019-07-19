package com.aaronhowser1.documentmod.quark;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.automation.feature.*;
import vazkii.quark.client.feature.*;
import vazkii.quark.decoration.feature.*;
import vazkii.quark.management.feature.*;
import vazkii.quark.misc.feature.*;
import vazkii.quark.tweaks.feature.*;
import vazkii.quark.vanity.feature.*;

import java.util.stream.IntStream;

import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.*;
import static com.aaronhowser1.documentmod.config.DYMMConfig.*;
import static com.aaronhowser1.documentmod.quark.QuarkItems.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

public class QuarkVanillaItems
{
    public static void init()
    {
        Item[] vanillaSeeds = {PUMPKIN_SEEDS, BEETROOT_SEEDS, WHEAT_SEEDS, MELON_SEEDS, Items.NETHER_WART};
        if(quarksubcat.infoDispenserSeeds && ModuleLoader.isFeatureEnabled(DispensersPlaceSeeds.class)) {
            addBlockInfo(DISPENSER, "documentationmod.quark.vanilla.dispenser.seeds");
            for (Item i: vanillaSeeds) {
                addItemInfo(i, "documentationmod.quark.vanilla.dispenser.seeds");
            }
        }
        if(quarksubcat.infoDispenserBlocks && ModuleLoader.isFeatureEnabled(DispensersPlaceBlocks.class)) {
            addBlockInfo(DISPENSER, "documentationmod.quark.vanilla.dispenser.blocks");
        }
        if(quarksubcat.infoDispenserRecords && ModuleLoader.isFeatureEnabled(DispenserRecords.class)) {
            addBlockInfo(DISPENSER, "documentationmod.quark.vanilla.dispenser.records");
            addBlockInfo(JUKEBOX, "documentationmod.quark.vanilla.dispenser.records");
            Item[] records = {RECORD_13, RECORD_CAT, RECORD_BLOCKS, RECORD_CHIRP, RECORD_FAR, RECORD_MALL, RECORD_MELLOHI, RECORD_STAL, RECORD_STRAD, RECORD_WARD, RECORD_11, RECORD_WAIT};
            for (Item i: records) {
                addItemInfo(i, "documentationmod.quark.vanilla.dispenser.records");
            }
        }
        if(quarksubcat.infoStickyPiston && ModuleLoader.isFeatureEnabled(PistonsMoveTEs.class)) {
            addBlockInfo(STICKY_PISTON, "documentationmod.quark.vanilla.piston.tileentities");
            addBlockInfo(PISTON, "documentationmod.quark.vanilla.piston.tileentities");
        }
        if(quarksubcat.infoLead && ModuleLoader.isFeatureEnabled(TieFences.class)) {
            addItemInfo(LEAD, "documentationmod.quark.vanilla.lead");
        }
        Item[] boats = {BOAT, SPRUCE_BOAT, BIRCH_BOAT, JUNGLE_BOAT, ACACIA_BOAT, DARK_OAK_BOAT};
        for (Item i: boats) {
            if(quarksubcat.infoBoat && ModuleLoader.isFeatureEnabled(ChestsInBoats.class)) {
                addItemInfo(i, "documentationmod.quark.vanilla.boat");
            }
        }
        Block[] shulkerBoxes = {WHITE_SHULKER_BOX, ORANGE_SHULKER_BOX, MAGENTA_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, YELLOW_SHULKER_BOX, LIME_SHULKER_BOX, PINK_SHULKER_BOX, GRAY_SHULKER_BOX, SILVER_SHULKER_BOX, CYAN_SHULKER_BOX, PURPLE_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, GREEN_SHULKER_BOX, RED_SHULKER_BOX, BLACK_SHULKER_BOX};
        for (Block b: shulkerBoxes)
        {
            if(quarksubcat.infoShulkerBoxExplosion && ModuleLoader.isFeatureEnabled(BlastproofShulkerBoxes.class)) {
                addBlockInfo(b, "documentationmod.quark.vanilla.shulker.explosion");
            }
            if(quarksubcat.infoShulkerBoxHover && ModuleLoader.isFeatureEnabled(ShulkerBoxTooltip.class)) {
                addBlockInfo(b, "documentationmod.quark.vanilla.shulker.hover");
            }
            if(quarksubcat.infoShulkerBoxAdd && ModuleLoader.isFeatureEnabled(RightClickAddToShulkerBox.class)) {
                addBlockInfo(b, "documentationmod.quark.vanilla.shulker.add");
            }
        }
        if(quarksubcat.infoCompass && ModuleLoader.isFeatureEnabled(CompassesWorkEverywhere.class)) {
            addItemInfo(COMPASS, "documentationmod.quark.vanilla.compass");
        }
        if(quarksubcat.infoDeployLadder && ModuleLoader.isFeatureEnabled(DeployLaddersDown.class)) {
            addBlockInfo(LADDER, "documentationmod.quark.vanilla.ladder.deploy");
        }
        if(quarksubcat.infoSlideDownLadders && ModuleLoader.isFeatureEnabled(LookDownLadders.class)) {
            addBlockInfo(LADDER, "documentationmod.quark.vanilla.ladder.slide");
        }
        if(quarksubcat.infoArmorStand && ModuleLoader.isFeatureEnabled(QuickArmorSwapping.class)) {
            addItemInfo(ARMOR_STAND, "documentationmod.quark.vanilla.armorstand");
        }
        if(quarksubcat.infoSign && ModuleLoader.isFeatureEnabled(RightClickSignEdit.class)) {
            addItemInfo(SIGN, "documentationmod.quark.vanilla.sign");
        }
        if(quarksubcat.infoEmeraldBlock && ModuleLoader.isFeatureEnabled(VillagerPursueEmeralds.class)) {
            addBlockInfo(EMERALD_BLOCK, "documentationmod.quark.vanilla.emeraldblock");
        }
        if(quarksubcat.infoElytra && ModuleLoader.isFeatureEnabled(DyableElytra.class)) {
            addItemInfo(ELYTRA, "documentationmod.quark.vanilla.elytra");
        }
        if(ModuleLoader.isFeatureEnabled(PlaceVanillaDusts.class)) {
            if(quarksubcat.infoGlowstoneDust){
                addItemInfo(GLOWSTONE_DUST, "documentationmod.quark.vanilla.glowstonedust");
                addItemInfo(GLOWSTONE_DUST_BLOCK, "documentationmod.quark.vanilla.glowstonedust");
            }
            if(quarksubcat.infoGunpowder){
                addItemInfo(GUNPOWDER, "documentationmod.quark.vanilla.gunpowder");
                addItemInfo(GUNPOWDER_BLOCK, "documentationmod.quark.vanilla.gunpowder");
            }
        }
        if(quarksubcat.infoDragonBreath && ModuleLoader.isFeatureEnabled(ThrowableDragonBreath.class)) {
            addItemInfo(DRAGON_BREATH, "documentationmod.quark.vanilla.dragonbreath");
        }
        if(quarksubcat.infoMap && ModuleLoader.isFeatureEnabled(MapTooltip.class)) {
            addItemInfo(MAP, "documentationmod.quark.vanilla.map");
            addItemInfo(FILLED_MAP, "documentationmod.quark.vanilla.map");
        }
        //TODO figure out why the enchanted book doesn't work
        if(quarksubcat.infoEnchantedBook && ModuleLoader.isFeatureEnabled(EnchantedBooksShowItems.class)) {
            addItemInfo(ENCHANTED_BOOK, "documentationmod.quark.vanilla.enchantedbook");
        }
        if(quarksubcat.infoItemFrames && ModuleLoader.isFeatureEnabled(FlatItemFrames.class)) {
            addItemInfo(ITEM_FRAME, "documentationmod.quark.vanilla.itemframe");
            if (ModuleLoader.isFeatureEnabled(FlatItemFrames.class))
                IntStream.range(0, 16).forEachOrdered(n -> addItemWithDamageInfo(COLORED_ITEM_FRAME, n, "documentationmod.quark.vanilla.itemframe"));
            if (ModuleLoader.isFeatureEnabled(GlassItemFrame.class))
                addItemInfo(GLASS_ITEM_FRAME, "documentationmod.quark.vanilla.itemframe");
        }
        if(quarksubcat.infoQuarkBannerMotifs && ModuleLoader.isFeatureEnabled(MoreBanners.class)) {
            IntStream.range(0,16).forEachOrdered(n -> addItemWithDamageInfo(BANNER, n, "documentationmod.quark.vanilla.banner.motifs"));
        }
        if(quarksubcat.infoBannerLayers && ModuleLoader.isFeatureEnabled(MoreBannerLayers.class)) {
            IntStream.range(0,16).forEachOrdered(n -> addItemWithDamageInfo(BANNER, n, "documentationmod.quark.vanilla.banner.layers"));
        }
        if(quarksubcat.infoBannersOnBoats && ModuleLoader.isFeatureEnabled(BoatSails.class)) {
            IntStream.range(0,16).forEachOrdered(n -> addItemWithDamageInfo(BANNER, n, "documentationmod.quark.vanilla.banner.boats"));
            for (Item i: boats) {
                addItemInfo(i, "documentationmod.quark.vanilla.banner.boats");
            }
        }
        if(quarksubcat.infoColorItemNames && ModuleLoader.isFeatureEnabled(DyeItemNames.class)) {
            IntStream.range(0,3).forEachOrdered(n -> addBlockWithDamageInfo(ANVIL, n, "documentationmod.quark.vanilla.dyeitemnames"));
        }
        Block[] vanillaStairs = {OAK_STAIRS, STONE_STAIRS, BRICK_STAIRS, STONE_BRICK_STAIRS, NETHER_BRICK_STAIRS, SANDSTONE_STAIRS, SPRUCE_STAIRS, BIRCH_STAIRS, JUNGLE_STAIRS, QUARTZ_STAIRS, ACACIA_STAIRS, DARK_OAK_STAIRS,RED_SANDSTONE_STAIRS, PURPUR_STAIRS};
        for (Block b: vanillaStairs) {
            if (quarksubcat.infoSitOnStairs && ModuleLoader.isFeatureEnabled(SitInStairs.class)) {
                addBlockInfo(b, "documentationmod.quark.vanilla.sitonstairs");
            }
        }
        if(quarksubcat.infoRightClickHarvest && ModuleLoader.isFeatureEnabled(RightClickHarvest.class)) {
            for (Item i: vanillaSeeds)
            {
                addItemInfo(i, "documentationmod.quark.vanilla.rightclickharvest");
            }
        }
        Item[] animalFood = {Items.WHEAT, CARROT, POTATO, BEETROOT, WHEAT_SEEDS, PUMPKIN_SEEDS, BEETROOT_SEEDS, PUMPKIN_SEEDS, Items.NETHER_WART};
        for (Item i: animalFood) {
            if(quarksubcat.infoAnimalsEatFood && ModuleLoader.isFeatureEnabled(AnimalsEatFloorFood.class)) {
                addItemInfo(i, "documentationmod.quark.vanilla.animalseatfood");
            }
        }
        if(quarksubcat.infoPlaceBlazeRods && ModuleLoader.isFeatureEnabled(PlaceBlazeRods.class)) {
            addItemInfo(Items.BLAZE_ROD, "documentationmod.quark.vanilla.blazerod");
            addItemInfo(QuarkItems.BLAZE_ROD, "documentationmod.quark.vanilla.blazerod");
        }
        if(quarksubcat.infoPistonsMoveItems && ModuleLoader.isFeatureEnabled(PistonsPushPullItems.class)) {
            addBlockInfo(PISTON, "documentationmod.quark.vanilla.pistonpush");
            addBlockInfo(STICKY_PISTON, "documentationmod.quark.vanilla.pistonpull");
        }
        if(quarksubcat.infoEndermiteShulkers && ModuleLoader.isFeatureEnabled(EndermitesIntoShulkers.class)) {
            addBlockInfo(PURPUR_BLOCK, "documentationmod.quark.vanilla.endermitesintoshulkers");
        }
        if(quarksubcat.infoMapMarkers && ModuleLoader.isFeatureEnabled(MapMarkers.class)) {
            addItemInfo(MAP, "documentationmod.quark.vanilla.mapmarkers");
            addItemInfo(FILLED_MAP, "documentationmod.quark.vanilla.mapmarkers");
        }
        if(quarksubcat.infoNoteBlockInterface && ModuleLoader.isFeatureEnabled(NoteBlockInterface.class)) {
            addBlockInfo(NOTEBLOCK, "documentationmod.quark.vanilla.noteblockinterface");
        }
        if(quarksubcat.infoNoteBlockMobSounds && ModuleLoader.isFeatureEnabled(NoteBlocksMobSounds.class)) {
            addBlockInfo(NOTEBLOCK, "documentationmod.quark.vanilla.noteblockmobs");
        }
        if(quarksubcat.infoPoisonBabies && ModuleLoader.isFeatureEnabled(PoisonPotatoUsage.class)) {
            addItemInfo(POISONOUS_POTATO, "documentationmod.quark.vanilla.poisonouspotato");
        }
        if(quarksubcat.infoChickensShed && ModuleLoader.isFeatureEnabled(ChickensShedFeathers.class)) {
            addItemInfo(FEATHER, "documentationmod.quark.vanilla.shedfeathers");
        }
        if(quarksubcat.infoShearChickens && ModuleLoader.isFeatureEnabled(ShearableChickens.class)) {
            addItemInfo(FEATHER, "documentationmod.quark.vanilla.shearchicken");
        }
        Item[] doors = {Items.ACACIA_DOOR, Items.BIRCH_DOOR, Items.DARK_OAK_DOOR, Items.JUNGLE_DOOR, Items.OAK_DOOR, Items.SPRUCE_DOOR};
        for (Item i: doors) {
            if(quarksubcat.infoDoubleDoors && ModuleLoader.isFeatureEnabled(DoubleDoors.class)) {
                addItemInfo(i, "documentationmod.quark.vanilla.doubledoors");
            }
        }
        if(quarksubcat.infoAxesLeaves && ModuleLoader.isFeatureEnabled(AxesBreakLeaves.class)) {
            IntStream.range(0,4).forEachOrdered(n -> addBlockWithDamageInfo(LEAVES, n, "documentationmod.quark.vanilla.axesleaves"));
            IntStream.range(0,2).forEachOrdered(n -> addBlockWithDamageInfo(LEAVES2, n, "documentationmod.quark.vanilla.axesleaves"));
            Item[] axes = {WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE};
            for (Item i: axes) {
                addItemInfo(i, "documentationmod.quark.vanilla.axesleaves");
            }
        }
        Item[] hoes = {WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, DIAMOND_HOE};
        if(quarksubcat.infoHoesAoE && ModuleLoader.isFeatureEnabled(HoeSickle.class)) {
            for (Item i: hoes) {
                addItemInfo(i, "documentationmod.quark.vanilla.hoeaoe");
            }
        }
        if(quarksubcat.infoSleepVote && ModuleLoader.isFeatureEnabled(ImprovedSleeping.class)) {
            IntStream.range(0,16).forEachOrdered(n -> addItemWithDamageInfo(Items.BED, n, "documentationmod.quark.vanilla.sleepvote"));
        }

        if(debugModIsDocumented) System.out.println("Quark Vanilla documented");
    }
}
