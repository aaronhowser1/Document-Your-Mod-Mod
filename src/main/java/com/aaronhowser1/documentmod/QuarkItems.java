package com.aaronhowser1.documentmod;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.addItemInfo;

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
    public static final Item WEATHER_DETECTOR = Items.AIR;
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

    public static void init()
    {
        addItemInfo(CHUTE, "documentationmod.quark.chute");
        addItemInfo(STURDY_STONE, "documentationmod.quark.sturdystone");
        addItemInfo(GOLD_BUTTON, "documentationmod.quark.goldbutton");
        addItemInfo(IRON_BUTTON, "documentationmod.quark.ironbutton");
        addItemInfo(OBSIDIAN_PRESSURE_PLATE, "documentationmod.quark.obsidianpressureplate");
        addItemInfo(IRON_ROD, "documentationmod.quark.ironrod");
        addItemInfo(REDSTONE_RANDOMIZER, "documentationmod.quark.redstonerandomizer");
        addItemInfo(WEATHER_DETECTOR, "documentationmod.quark.weatherdetector");
        addItemInfo(TROWEL, "documentationmod.quark.trowel");
        addItemInfo(GLASS_ITEM_FRAME, "documentationmod.quark.glassitemframe");
        addItemInfo(IRON_LADDER, "documentationmod.quark.ironladder");
        addItemInfo(CANDLE, "documentationmod.quark.candle");
        addItemInfo(WITCH_HAT, "documentationmod.quark.witchhat");
        addItemInfo(BIOTITE_ORE, "documentationmod.quark.biotiteore");
        addItemInfo(SMOKER, "documentationmod.quark.smoker");
        addItemInfo(DIAMOND_HEART, "documentationmod.quark.diamondheart");
        addItemInfo(SOUL_BEAD, "documentationmod.quark.soulbead");
        addItemInfo(ANCIENT_TOME, "documentationmod.quark.ancienttome");
        addItemInfo(RUNE, "documentationmod.quark.rune");
        addItemInfo(ENDERDRAGON_SCALE, "documentationmod.quark.enderdragonscale");
        addItemInfo(ARROW_ENDER, "documentationmod.quark.enderarrow");
        addItemInfo(ARROW_EXPLSOSIVE, "documentationmod.quark.explosivearrow");
        addItemInfo(ARROW_TORCH, "documentationmod.quark.torcharrow");
        addItemInfo(PARROT_EGG, "documentationmod.quark.parrotegg");
        addItemInfo(PICKARANG, "documentationmod.quark.pickarange");
        addItemInfo(SLIME_BUCKET, "documentationmod.quark.slimebucket");
        addItemInfo(SOUL_POWDER, "documentationmod.quark.soulpowder");
        addItemInfo(BLACK_ASH, "documentationmod.quark.blackash");
        addItemInfo(ENDER_WATCHER, "documentationmod.quark.enderwatcher");
    }
}

