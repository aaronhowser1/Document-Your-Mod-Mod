package com.aaronhowser1.documentmod.quark;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.automation.feature.*;
import vazkii.quark.building.feature.*;
import vazkii.quark.decoration.feature.*;
import vazkii.quark.misc.feature.*;
import vazkii.quark.tweaks.feature.DeployLaddersDown;
import vazkii.quark.tweaks.feature.GlassShards;
import vazkii.quark.vanity.feature.*;
import vazkii.quark.world.feature.*;
import java.util.stream.IntStream;

import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.*;
import static com.aaronhowser1.documentmod.config.DYMMConfig.vanillaitemschangedbyquark;
import static vazkii.quark.automation.feature.MetalButtons.*;
import static vazkii.quark.misc.feature.HorseWhistle.horsesAreMagical;
import static vazkii.quark.world.feature.Archaeologist.*;

@GameRegistry.ObjectHolder("quark")
public class QuarkItems
{
    public static final Item CHUTE = Items.AIR;
    public static final Item STURDY_STONE = Items.AIR;
    public static final Item GOLD_BUTTON = Items.AIR;
    public static final Item IRON_BUTTON = Items.AIR;
    public static final Item OBSIDIAN_PRESSURE_PLATE = Items.AIR;
    public static final Item IRON_ROD = Items.AIR;
    public static final Item REDSTONE_RANDOMIZER = Items.AIR;
    public static final Item RAIN_DETECTOR = Items.AIR;
    public static final Item TROWEL = Items.AIR;
    public static final Item GLASS_ITEM_FRAME = Items.AIR;
    public static final Item IRON_LADDER = Items.AIR;
    public static final Item CANDLE = Items.AIR;
    public static final Item WITCH_HAT = Items.AIR;
    public static final Item BIOTITE_ORE = Items.AIR;
    public static final Item SMOKER = Items.AIR;
    public static final Item DIAMOND_HEART = Items.AIR;
    public static final Item SOUL_BEAD = Items.AIR;
    public static final Item ANCIENT_TOME = Items.AIR;
    public static final Item RUNE = Items.AIR;
    public static final Item ENDERDRAGON_SCALE = Items.AIR;
    public static final Item ARROW_ENDER = Items.AIR;
    public static final Item ARROW_EXPLSOSIVE= Items.AIR;
    public static final Item ARROW_TORCH = Items.AIR;
    public static final Item PARROT_EGG = Items.AIR;
    public static final Item PICKARANG = Items.AIR;
    public static final Item SLIME_BUCKET = Items.AIR;
    public static final Item SOUL_POWDER = Items.AIR;
    public static final Item BLACK_ASH = Items.AIR;
    public static final Item ENDER_WATCHER = Items.AIR;
    public static final Item ARCHAEOLOGIST_HAT = Items.AIR;
    public static final Item CHARCOAL_BLOCK = Items.AIR;
    public static final Item COLORED_ITEM_FRAME = Items.AIR;
    public static final Item SUGAR_BLOCK = Items.AIR;
    public static final Item REDSTONE_INDUCTOR = Items.AIR;
    public static final Item GRATE = Items.AIR;
    public static final Item ROPE = Items.AIR;
    public static final Item GRAVISAND = Items.AIR;
    public static final Item LIT_LAMP = Items.AIR;
    public static final Item GLASS_SHARDS = Items.AIR;
    public static final Item HORSE_WHISTLE = Items.AIR;
    public static final Item GLOWSTONE_DUST_BLOCK = Items.AIR;
    public static final Item GUNPOWDER_BLOCK = Items.AIR;
    public static final Item BLAZE_ROD = Items.AIR;

    public static void init()
    {
        if(ModuleLoader.isFeatureEnabled(Chute.class)) {
            addItemInfo(CHUTE, "documentationmod.quark.chute");
        }
        if(ModuleLoader.isFeatureEnabled(SturdyStone.class)) {
            addItemInfo(STURDY_STONE, "documentationmod.quark.sturdystone");
        }
        if(enableGold) {
            addItemInfo(GOLD_BUTTON, "documentationmod.quark.goldbutton");
        }
        if(enableIron) {
            addItemInfo(IRON_BUTTON, "documentationmod.quark.ironbutton");
        }
        if(ModuleLoader.isFeatureEnabled(ObsidianPressurePlate.class)) {
            addItemInfo(OBSIDIAN_PRESSURE_PLATE, "documentationmod.quark.obsidianpressureplate");
        }
        if(ModuleLoader.isFeatureEnabled(PistonSpikes.class)) {
            addItemInfo(IRON_ROD, "documentationmod.quark.ironrod");
        }
        if(ModuleLoader.isFeatureEnabled(RedstoneRandomizer.class)) {
            addItemInfo(REDSTONE_RANDOMIZER, "documentationmod.quark.redstonerandomizer");
        }
        if(ModuleLoader.isFeatureEnabled(RainDetector.class)) {
            addItemInfo(RAIN_DETECTOR, "documentationmod.quark.weatherdetector");
        }
        if(ModuleLoader.isFeatureEnabled(Trowel.class)) {
            addItemInfo(TROWEL, "documentationmod.quark.trowel");
        }
        if(ModuleLoader.isFeatureEnabled(GlassItemFrame.class)) {
            addItemInfo(GLASS_ITEM_FRAME, "documentationmod.quark.glassitemframe");
        }
        if(ModuleLoader.isFeatureEnabled(IronLadders.class)) {
            addItemInfo(IRON_LADDER, "documentationmod.quark.ironladder");
            if(vanillaitemschangedbyquark.infoLadder && ModuleLoader.isFeatureEnabled(DeployLaddersDown.class)) {
                addItemInfo(IRON_LADDER, "documentationmod.quark.vanilla.ladder");
            }
        }
        if(ModuleLoader.isFeatureEnabled(TallowAndCandles.class)) {
            IntStream.range(0, 16).forEachOrdered(n -> addItemWithDamageInfo(CANDLE, n, "documentationmod.quark.candle"));
        }
        if(ModuleLoader.isFeatureEnabled(WitchHat.class)) {
            addItemInfo(WITCH_HAT, "documentationmod.quark.witchhat");
        }
        if(ModuleLoader.isFeatureEnabled(Biotite.class)) {
            addItemInfo(BIOTITE_ORE, "documentationmod.quark.biotiteore");
        }
        if(ModuleLoader.isFeatureEnabled(NetherSmoker.class)) {
            addItemInfo(SMOKER, "documentationmod.quark.smoker");
        }
        if(ModuleLoader.isFeatureEnabled(Stonelings.class)) {
            addItemInfo(DIAMOND_HEART, "documentationmod.quark.diamondheart");
        }
        if(ModuleLoader.isFeatureEnabled(Wraiths.class)) {
            addItemInfo(SOUL_BEAD, "documentationmod.quark.soulbead");
        }
        if(ModuleLoader.isFeatureEnabled(AncientTomes.class)) {
            addItemInfo(ANCIENT_TOME, "documentationmod.quark.ancienttome");
        }
        if(ModuleLoader.isFeatureEnabled(ColorRunes.class)) {
            IntStream.range(0, 16).forEachOrdered(n -> addItemWithDamageInfo(RUNE, n, "documentationmod.quark.rune"));
        }
        if(ModuleLoader.isFeatureEnabled(EnderdragonScales.class)) {
            addItemInfo(ENDERDRAGON_SCALE, "documentationmod.quark.enderdragonscale");
        }
        if(ModuleLoader.isFeatureEnabled(ExtraArrows.class)) {
            addItemInfo(ARROW_ENDER, "documentationmod.quark.enderarrow");
            addItemInfo(ARROW_EXPLSOSIVE, "documentationmod.quark.explosivearrow");
            addItemInfo(ARROW_TORCH, "documentationmod.quark.torcharrow");
        }

        if(ModuleLoader.isFeatureEnabled(ParrotEggs.class)) {
            IntStream.range(0,5).forEachOrdered(n-> addItemWithDamageInfo(PARROT_EGG, n, "documentationmod.quark.parrotegg"));
        }
        if(ModuleLoader.isFeatureEnabled(Pickarang.class)) {
            addItemInfo(PICKARANG, "documentationmod.quark.pickarange");
        }
        if(ModuleLoader.isFeatureEnabled(SlimeBucket.class)) {
            addItemInfo(SLIME_BUCKET, "documentationmod.quark.slimebucket");
        }
        if(ModuleLoader.isFeatureEnabled(SoulPowder.class)) {
            addItemInfo(SOUL_POWDER, "documentationmod.quark.soulpowder");
        }
        if(ModuleLoader.isFeatureEnabled(BlackAsh.class)) {
            addItemInfo(BLACK_ASH, "documentationmod.quark.blackash");
        }
        if(ModuleLoader.isFeatureEnabled(EnderWatcher.class)) {
            addItemInfo(ENDER_WATCHER, "documentationmod.quark.enderwatcher");
        }
        if(enableHat && hatIncreasesOreYield) {
            addItemInfo(ARCHAEOLOGIST_HAT, "documentationmod.quark.archhat.ores");
            if(sellHat) addItemInfo(ARCHAEOLOGIST_HAT, "documentationmod.quark.archhat.sell");
            if(dropHat) addItemInfo(ARCHAEOLOGIST_HAT, "documentationmod.quark.archhat.drop");
        }
        if(ModuleLoader.isFeatureEnabled(CharcoalBlock.class)) {
            addItemInfo(CHARCOAL_BLOCK, "documentationmod.quark.charcoalblock");
        }
        if(ModuleLoader.isFeatureEnabled(SugarBlock.class)) {
            addItemInfo(SUGAR_BLOCK, "documentationmod.quark.sugarblock");
        }
        if(ModuleLoader.isFeatureEnabled(RedstoneInductor.class)) {
            addItemInfo(REDSTONE_INDUCTOR, "documentationmod.quark.redstoneinductor");
        }
        if(ModuleLoader.isFeatureEnabled(Grate.class)) {
            addItemInfo(GRATE, "documentationmod.quark.grate");
        }
        if(ModuleLoader.isFeatureEnabled(Rope.class)) {
            addItemInfo(ROPE, "documentationmod.quark.rope");
            addItemInfo(ROPE, "documentationmod.quark.rope.dispenser");
        }
        if(ModuleLoader.isFeatureEnabled(Gravisand.class)) {
            addItemInfo(GRAVISAND, "documentationmod.quark.gravisand");
        }
        if(ModuleLoader.isFeatureEnabled(LitLamp.class)) {
            addItemInfo(LIT_LAMP, "documentationmod.quark.litlamp");
        }
        if(ModuleLoader.isFeatureEnabled(GlassShards.class)) {
            IntStream.range(0, 16).forEachOrdered(n -> addItemWithDamageInfo(GLASS_SHARDS, n, "documentationmod.quark.glassshard"));
        }
        if(ModuleLoader.isFeatureEnabled(HorseWhistle.class)) {
            if (!horsesAreMagical) addItemInfo(HORSE_WHISTLE, "documentationmod.quark.horsewhistle");
            if (horsesAreMagical) addItemInfo(HORSE_WHISTLE, "documentationmod.quark.horsewhistle.ibelieveinmagic");
        }
    }
}