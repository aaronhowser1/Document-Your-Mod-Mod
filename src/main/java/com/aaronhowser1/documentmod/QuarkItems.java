package com.aaronhowser1.documentmod;

import mezz.jei.api.IModRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.addInfo;

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
//    public static final Item ANCIENT_TOME = Items.AIR;
//    public static final Item RUNE = Items.AIR;
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
        addInfo(CHUTE, "documentationmod.quark.chute");
        addInfo(STURDY_STONE, "documentationmod.quark.sturdystone");
        addInfo(GOLD_BUTTON, "documentationmod.quark.goldbutton");
        addInfo(IRON_BUTTON, "documentationmod.quark.ironbutton");
        addInfo(OBSIDIAN_PRESSURE_PLATE, "documentationmod.quark.obsidianpressureplate");
        addInfo(IRON_ROD, "documentationmod.quark.ironrod");
        addInfo(REDSTONE_RANDOMIZER, "documentationmod.quark.redstonerandomizer");
        addInfo(WEATHER_DETECTOR, "documentationmod.quark.weatherdetector");
        addInfo(TROWEL, "documentationmod.quark.trowel");
        addInfo(GLASS_ITEM_FRAME, "documentationmod.quark.glassitemframe");
        addInfo(IRON_LADDER, "documentationmod.quark.ironladder");
        addInfo(CANDLE, "documentationmod.quark.candle");
        addInfo(WITCH_HAT, "documentationmod.quark.witchhat");
        addInfo(BIOTITE_ORE, "documentationmod.quark.biotiteore");
        addInfo(SMOKER, "documentationmod.quark.smoker");
        addInfo(DIAMOND_HEART, "documentationmod.quark.diamondheart");
        addInfo(SOUL_BEAD, "documentationmod.quark.soulbead");
//        addInfo(ANCIENT_TOME, "documentationmod.quark.ancienttome");
//        addInfo(RUNE, "documentationmod.quark.rune");
        addInfo(ENDERDRAGON_SCALE, "documentationmod.quark.enderdragonscale");
        addInfo(ARROW_ENDER, "documentationmod.quark.enderarrow");
        addInfo(ARROW_EXPLSOSIVE, "documentationmod.quark.explosivearrow");
        addInfo(ARROW_TORCH, "documentationmod.quark.torcharrow");
        addInfo(PARROT_EGG, "documentationmod.quark.parrotegg");
        addInfo(PICKARANG, "documentationmod.quark.pickarange");
        addInfo(SLIME_BUCKET, "documentationmod.quark.slimebucket");
        addInfo(SOUL_POWDER, "documentationmod.quark.soulpowder");
        addInfo(BLACK_ASH, "documentationmod.quark.blackash");
        addInfo(ENDER_WATCHER, "documentationmod.quark.enderwatcher");
    }
}

