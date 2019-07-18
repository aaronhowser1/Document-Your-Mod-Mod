package com.aaronhowser1.documentmod.tinkersconstruct;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.tweaks.feature.DeployLaddersDown;
import vazkii.quark.tweaks.feature.LookDownLadders;

import java.util.stream.IntStream;

import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.*;
import static com.aaronhowser1.documentmod.config.DYMMConfig.quarksubcat;

@GameRegistry.ObjectHolder("tconstruct")
public class TinkersConstruct{

    public static final Item SLIMESLING = Items.AIR;
    public static final Item SLIME_BOOTS = Items.AIR;
    public static final Item PIGGYBACKPACK = Items.AIR;
    public static final Item SLIME_CHANNEL = Items.AIR;
    public static final Item THROWBALL = Items.AIR;
    public static final Item MOMS_SPAGHETTI = Items.AIR;
    public static final Item MATERIALS = Items.AIR;
    public static final Item SOIL = Items.AIR;
    public static final Item PICKAXE = Items.AIR;
    public static final Item SHOVEL = Items.AIR;
    public static final Item HATCHET = Items.AIR;
    public static final Item MATTOCK = Items.AIR;
    public static final Item KAMA = Items.AIR;
    public static final Item SCYTHE = Items.AIR;
    public static final Item HAMMER = Items.AIR;
    public static final Item EXCAVATOR = Items.AIR;
    public static final Item LUMBERAXE = Items.AIR;
    public static final Item BROADSWORD = Items.AIR;
    public static final Item LONGSWORD = Items.AIR;
    public static final Item RAPIER = Items.AIR;
    public static final Item BATTLESIGN = Items.AIR;
    public static final Item FRYPAN = Items.AIR;
    public static final Item CLEAVER = Items.AIR;
    public static final Item ARROW = Items.AIR;
    public static final Item SHORTBOW = Items.AIR;
    public static final Item LONGBOW = Items.AIR;
    public static final Item BOLT = Items.AIR;
    public static final Item CROSSBOW = Items.AIR;
    public static final Item SHURIKEN = Items.AIR;
    public static final Item BOLT_CORE = Items.AIR;
    public static final Item FANCY_FRAME = Items.AIR;
    public static final Item SLIME_SAPLING = Items.AIR;
    public static final Item SLIME_DIRT = Items.AIR;
    public static final Item SLIME_GRASS = Items.AIR;
    public static final Item STONE_LADDER = Items.AIR;
    public static final Item SLIME_CONGEALED = Items.AIR;

    public static void init() {

        IntStream.range(0, 5).forEachOrdered(n -> addItemWithDamageInfo(SLIMESLING, n, "documentationmod.tconstruct.slimesling"));
        IntStream.range(0, 5).forEachOrdered(n -> addItemWithDamageInfo(SLIME_BOOTS, n, "documentationmod.tconstruct.slimeboots"));
        addItemInfo(PIGGYBACKPACK, "documentationmod.tconstruct.piggybackpack");
        IntStream.range(0, 5).forEachOrdered(n -> addItemWithDamageInfo(SLIME_CHANNEL, n, "documentationmod.tconstruct.slimechannel"));
        addItemWithDamageInfo(THROWBALL, 0, "documentationmod.tconstruct.glowball");
        addItemWithDamageInfo(THROWBALL, 1, "documentationmod.tconstruct.EFLN");
        addItemInfo(MOMS_SPAGHETTI, "documentationmod.tconstruct.armsheavy");
        addItemWithDamageInfo(MATERIALS, 12, "documentationmod.tconstruct.modifiers.wide");
        addItemWithDamageInfo(MATERIALS, 13, "documentationmod.tconstruct.modifiers.tall");
        addItemWithDamageInfo(MATERIALS, 14, "documentationmod.tconstruct.modifiers.durability");
        addItemWithDamageInfo(MATERIALS, 16, "documentationmod.tconstruct.modifiers.silk");
        addItemWithDamageInfo(MATERIALS, 17, "documentationmod.tconstruct.modifiers.lifesteal");
        addItemWithDamageInfo(MATERIALS, 19, "documentationmod.tconstruct.modifiers.mending");
        addItemWithDamageInfo(SOIL, 4, "documentationmod.tconstruct.modifiers.smite");
        addItemWithDamageInfo(FANCY_FRAME, 6, "documentationmod.quark.glassitemframe");
        IntStream.range(0,3).forEachOrdered(n -> addItemWithDamageInfo(SLIME_SAPLING, n, "documentationmod.tconstruct.slimesaplings"));
        addItemWithDamageInfo(SLIME_SAPLING, 0, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_SAPLING, 1, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_DIRT, 0, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_DIRT, 1, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_DIRT, 2, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 0, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 1, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 2, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 3, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 5, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 6, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 7, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 8, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 10, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 11, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 12, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 13, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_CONGEALED, 0, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_CONGEALED, 1, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_CONGEALED, 2, "documentationmod.tconstruct.slimeislands");
        addItemWithDamageInfo(SLIME_SAPLING, 2, "documentationmod.tconstruct.netherslimeislands");
        addItemWithDamageInfo(SLIME_DIRT, 3, "documentationmod.tconstruct.netherslimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 4, "documentationmod.tconstruct.netherslimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 9, "documentationmod.tconstruct.netherslimeislands");
        addItemWithDamageInfo(SLIME_GRASS, 14, "documentationmod.tconstruct.netherslimeislands");
        addItemWithDamageInfo(SLIME_CONGEALED, 4, "documentationmod.tconstruct.netherslimeislands");
        if(quarksubcat.infoSlideDownLadders && ModuleLoader.isFeatureEnabled(LookDownLadders.class)) {
            addItemInfo(STONE_LADDER, "documentationmod.quark.vanilla.ladder.slide");
        }
        //TODO: figure out how to add info to items if they have NBT
        addItemInfo(PICKAXE, "documentationmod.tconstruct.tool.pickaxe");
        addItemInfo(SHOVEL, "documentationmod.tconstruct.tool.shovel");
        addItemInfo(HATCHET, "documentationmod.tconstruct.tool.hatchet");
        addItemInfo(MATTOCK, "documentationmod.tconstruct.tool.mattock");
        addItemInfo(KAMA, "documentationmod.tconstruct.tool.kama");
        addItemInfo(SCYTHE, "documentationmod.tconstruct.tool.scythe");
        addItemInfo(HAMMER, "documentationmod.tconstruct.tool.hammer");
        addItemInfo(EXCAVATOR, "documentationmod.tconstruct.tool.excavator");
        addItemInfo(LUMBERAXE, "documentationmod.tconstruct.tool.lumberaxe");
        addItemInfo(BROADSWORD, "documentationmod.tconstruct.tool.broadsword");
        addItemInfo(LONGSWORD, "documentationmod.tconstruct.tool.longsword");
        addItemInfo(RAPIER, "documentationmod.tconstruct.tool.rapier");
        addItemInfo(BATTLESIGN, "documentationmod.tconstruct.tool.battlesign");
        addItemInfo(FRYPAN, "documentationmod.tconstruct.tool.frypan");
        addItemInfo(CLEAVER, "documentationmod.tconstruct.tool.cleaver");
        addItemInfo(ARROW, "documentationmod.tconstruct.tool.arrow");
        addItemInfo(SHORTBOW, "documentationmod.tconstruct.tool.shortbow");
        addItemInfo(LONGBOW, "documentationmod.tconstruct.tool.longbow");
        addItemInfo(BOLT, "documentationmod.tconstruct.tool.bolt");
        addItemInfo(CROSSBOW, "documentationmod.tconstruct.tool.crossbow");
        addItemInfo(SHURIKEN, "documentationmod.tconstruct.tool.shuriken");
        addItemInfo(BOLT_CORE, "documentationmod.tconstruct.boltcore");
    }
}
