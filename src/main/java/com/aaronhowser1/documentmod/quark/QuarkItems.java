package com.aaronhowser1.documentmod.quark;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.tweaks.feature.DeployLaddersDown;
import vazkii.quark.tweaks.feature.LookDownLadders;

import java.util.stream.IntStream;

import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.*;
import static com.aaronhowser1.documentmod.config.DYMMConfig.*;
import static vazkii.quark.misc.feature.HorseWhistle.horsesAreMagical;
import static vazkii.quark.world.feature.Archaeologist.*;
import static vazkii.quark.world.feature.Wraiths.enableCurse;

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
    public static final Item ARROW_EXPLOSIVE= Items.AIR;
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
    public static final Item IRON_CHAIN = Items.AIR;

    public static void init()
    {
        addItemInfo(CHUTE, "documentationmod.quark.chute");
        addItemInfo(STURDY_STONE, "documentationmod.quark.sturdystone");
        addItemInfo(GOLD_BUTTON, "documentationmod.quark.goldbutton");
        addItemInfo(IRON_BUTTON, "documentationmod.quark.ironbutton");
        addItemInfo(OBSIDIAN_PRESSURE_PLATE, "documentationmod.quark.obsidianpressureplate");
        addItemInfo(IRON_ROD, "documentationmod.quark.ironrod");
        addItemInfo(REDSTONE_RANDOMIZER, "documentationmod.quark.redstonerandomizer");
        addItemInfo(RAIN_DETECTOR, "documentationmod.quark.weatherdetector");
        addItemInfo(TROWEL, "documentationmod.quark.trowel");
        addItemInfo(GLASS_ITEM_FRAME, "documentationmod.quark.glassitemframe");
        addItemInfo(IRON_LADDER, "documentationmod.quark.ironladder");
        if(ModuleLoader.isFeatureEnabled(DeployLaddersDown.class)) {
            addItemInfo(IRON_LADDER, "documentationmod.quark.vanilla.ladder.deploy");
        }
        if(ModuleLoader.isFeatureEnabled(LookDownLadders.class)) {
            addItemInfo(IRON_LADDER, "documentationmod.quark.vanilla.ladder.slide");
        }
        IntStream.range(0, 16).forEachOrdered(n -> addItemWithDamageInfo(CANDLE, n, "documentationmod.quark.candle"));
        addItemInfo(WITCH_HAT, "documentationmod.quark.witchhat");
        addItemInfo(BIOTITE_ORE, "documentationmod.quark.biotiteore");
        addItemInfo(SMOKER, "documentationmod.quark.smoker");
        addItemInfo(DIAMOND_HEART, "documentationmod.quark.diamondheart");
        if(enableCurse) addItemInfo(SOUL_BEAD, "documentationmod.quark.soulbead");
        addItemInfo(ANCIENT_TOME, "documentationmod.quark.ancienttome");
        IntStream.range(0, 16).forEachOrdered(n -> addItemWithDamageInfo(RUNE, n, "documentationmod.quark.rune"));
        addItemInfo(ENDERDRAGON_SCALE, "documentationmod.quark.enderdragonscale");
        addItemInfo(ARROW_ENDER, "documentationmod.quark.enderarrow");
        addItemInfo(ARROW_EXPLOSIVE, "documentationmod.quark.explosivearrow");
        addItemInfo(ARROW_TORCH, "documentationmod.quark.torcharrow");
        IntStream.range(0,5).forEachOrdered(n-> addItemWithDamageInfo(PARROT_EGG, n, "documentationmod.quark.parrotegg"));
        addItemInfo(PICKARANG, "documentationmod.quark.pickarange");
        addItemInfo(SLIME_BUCKET, "documentationmod.quark.slimebucket");
        addItemInfo(SOUL_POWDER, "documentationmod.quark.soulpowder");
        addItemInfo(BLACK_ASH, "documentationmod.quark.blackash");
        addItemInfo(ENDER_WATCHER, "documentationmod.quark.enderwatcher");
        addItemInfo(ARCHAEOLOGIST_HAT, "documentationmod.quark.archhat.ores");
        if(sellHat) addItemInfo(ARCHAEOLOGIST_HAT, "documentationmod.quark.archhat.sell");
        if(dropHat) addItemInfo(ARCHAEOLOGIST_HAT, "documentationmod.quark.archhat.drop");
        addItemInfo(CHARCOAL_BLOCK, "documentationmod.quark.charcoalblock");
        addItemInfo(SUGAR_BLOCK, "documentationmod.quark.sugarblock");
        addItemInfo(REDSTONE_INDUCTOR, "documentationmod.quark.redstoneinductor");
        addItemInfo(GRATE, "documentationmod.quark.grate");
        addItemInfo(ROPE, "documentationmod.quark.rope");
        addItemInfo(ROPE, "documentationmod.quark.rope.dispenser");
        addItemInfo(GRAVISAND, "documentationmod.quark.gravisand");
        addItemInfo(LIT_LAMP, "documentationmod.quark.litlamp");
        IntStream.range(0, 16).forEachOrdered(n -> addItemWithDamageInfo(GLASS_SHARDS, n, "documentationmod.quark.glassshard"));
        if (!horsesAreMagical) addItemInfo(HORSE_WHISTLE, "documentationmod.quark.horsewhistle");
        if (horsesAreMagical) addItemInfo(HORSE_WHISTLE, "documentationmod.quark.horsewhistle.ibelieveinmagic");
        addItemInfo(IRON_CHAIN, "documentationmod.quark.ironchains");

        if(debugModIsDocumented) System.out.println("Quark documented");
    }
}