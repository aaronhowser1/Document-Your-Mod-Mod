package com.aaronhowser1.documentmod.twilightforest;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.stream.IntStream;

import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.*;
import static com.aaronhowser1.documentmod.config.DYMMConfig.debugModIsDocumented;

@GameRegistry.ObjectHolder("twilightforest")
public class TwilightForest {
    public static final Item TWILIGHT_SCEPTER = Items.AIR;
    public static final Item LIFEDRAIN_SCEPTER = Items.AIR;
    public static final Item ZOMBIE_SCEPTER = Items.AIR;
    public static final Item SHIELD_SCEPTER = Items.AIR;
    public static final Item TROPHY = Items.AIR;
    public static final Item TRANSFORMATION_POWDER = Items.AIR;
    public static final Item MAGIC_MAP_EMPTY = Items.AIR;
    public static final Item MAGIC_MAP = Items.AIR;
    public static final Item MAZE_MAP_FOCUS = Items.AIR;
    public static final Item MAZE_MAP_EMPTY = Items.AIR;
    public static final Item MAZE_MAP = Items.AIR;
    public static final Item ORE_MAP_EMPTY = Items.AIR;
    public static final Item ORE_MAP = Items.AIR;
    public static final Item ORE_MAGNET = Items.AIR;
    public static final Item CRUMBLE_HORN = Items.AIR;
    public static final Item PEACOCK_FAN = Items.AIR;
    public static final Item MOONWORM_QUEEN = Items.AIR;
    public static final Item CHARM_OF_LIFE_1 = Items.AIR;
    public static final Item CHARM_OF_LIFE_2 = Items.AIR;
    public static final Item CHARM_OF_KEEPING_1 = Items.AIR;
    public static final Item CHARM_OF_KEEPING_2 = Items.AIR;
    public static final Item CHARM_OF_KEEPING_3 = Items.AIR;
    public static final Item TOWER_KEY = Items.AIR;
    public static final Item EXPERIMENT_115 = Items.AIR;
    public static final Item LAMP_OF_CINDERS = Items.AIR;
    public static final Item MAGIC_BEANS = Items.AIR;
    public static final Item TRIPLE_BOW = Items.AIR;
    public static final Item SEEKER_BOW = Items.AIR;
    public static final Item ICE_BOW = Items.AIR;
    public static final Item ENDER_BOW = Items.AIR;
    public static final Item ICE_SWORD = Items.AIR;
    public static final Item GLASS_SWORD = Items.AIR;
    public static final Item BLOCK_AND_CHAIN = Items.AIR;
    public static final Item FIREFLY = Items.AIR;
    public static final Item CICADA = Items.AIR;
    public static final Item FIRE_JET = Items.AIR;
    public static final Item TWILIGHT_SAPLING = Items.AIR;
    public static final Item MAGIC_LOG_CORE = Items.AIR;
    public static final Item TOWER_DEVICE = Items.AIR;
    public static final Item TROPHY_PEDESTAL = Items.AIR;
    public static final Item AURORA_BLACK = Items.AIR;
    public static final Item CASTLE_DOOR = Items.AIR;
    public static final Item UNCRAFTING_TABLE = Items.AIR;

    public static void init() {
        /*
        addItemInfo(TWILIGHT_SCEPTER, "documentationmod.twilightforest.twilightscepter");
        addItemInfo(LIFEDRAIN_SCEPTER, "documentationmod.twilightforest.lifedrainscepter");
        addItemInfo(ZOMBIE_SCEPTER, "documentationmod.twilightforest.zombiescepter");
        addItemInfo(SHIELD_SCEPTER, "documentationmod.twilightforest.shieldscepter");
        addItemWithDamageInfo(TROPHY, 0, "documentationmod.twilightforest.trophy.naga");
        addItemWithDamageInfo(TROPHY, 1, "documentationmod.twilightforest.trophy.lich");
        addItemWithDamageInfo(TROPHY, 2, "documentationmod.twilightforest.trophy.hydra");
        addItemWithDamageInfo(TROPHY, 3, "documentationmod.twilightforest.trophy.urghast");
        addItemWithDamageInfo(TROPHY, 4, "documentationmod.twilightforest.trophy.knightphantom");
        addItemWithDamageInfo(TROPHY, 5, "documentationmod.twilightforest.trophy.snowqueen");
        addItemWithDamageInfo(TROPHY, 6, "documentationmod.twilightforest.trophy.minoshroom");
        addItemWithDamageInfo(TROPHY, 8, "documentationmod.twilightforest.trophy.questingram");
        addItemInfo(TRANSFORMATION_POWDER, "documentationmod.twilightforest.transformationpowder");
        addItemInfo(TRANSFORMATION_POWDER, "documentationmod.twilightforest.transformationpowder.list");
        addItemInfo(MAGIC_MAP_EMPTY, "documentationmod.twilightforest.magicmap");
        addItemInfo(MAGIC_MAP, "documentationmod.twilightforest.magicmap");
        addItemInfo(MAZE_MAP_FOCUS, "documentationmod.twilightforest.mazemapfocus");
        addItemInfo(MAZE_MAP_EMPTY, "documentationmod.twilightforest.mazemap");
        addItemInfo(MAZE_MAP, "documentationmod.twilightforest.mazemap");
        addItemInfo(ORE_MAP_EMPTY, "documentationmod.twilightforest.oremap");
        addItemInfo(ORE_MAP, "documentationmod.twilightforest.oremap");
        addItemInfo(ORE_MAGNET, "documentationmod.twilightforest.oremagnet");
        addItemInfo(CRUMBLE_HORN, "documentationmod.twilightforest.crumblehorn");
        addItemInfo(PEACOCK_FAN, "documentationmod.twilightforest.peacockfan");
        addItemInfo(MOONWORM_QUEEN, "documentationmod.twilightforest.moonwormqueen");
        addItemInfo(CHARM_OF_LIFE_1, "documentationmod.twilightforest.lifecharm.1");
        addItemInfo(CHARM_OF_LIFE_2, "documentationmod.twilightforest.lifecharm.2");
        addItemInfo(CHARM_OF_KEEPING_1, "documentationmod.twilightforest.keepcharm.1");
        addItemInfo(CHARM_OF_KEEPING_2, "documentationmod.twilightforest.keepcharm.2");
        addItemInfo(CHARM_OF_KEEPING_3, "documentationmod.twilightforest.keepcharm.3");
        addItemInfo(TOWER_KEY, "documentationmod.twilightforest.towerkey");
        addItemInfo(EXPERIMENT_115, "documentationmod.twilightforest.scarycake");
        addItemInfo(LAMP_OF_CINDERS, "documentationmod.twilightforest.lamp");
        addItemInfo(MAGIC_BEANS, "documentationmod.twilightforest.magicbeans");
        addItemInfo(TRIPLE_BOW, "documentationmod.twilightforest.bow.triple");
        addItemInfo(SEEKER_BOW, "documentationmod.twilightforest.bow.seeker");
        addItemInfo(ICE_BOW, "documentationmod.twilightforest.bow.ice");
        addItemInfo(ENDER_BOW, "documentationmod.twilightforest.bow.ender");
        addItemInfo(ICE_SWORD, "documentationmod.twilightforest.sword.ice");
        addItemInfo(GLASS_SWORD, "documentationmod.twilightforest.sword.glass");
        addItemInfo(BLOCK_AND_CHAIN, "documentationmod.twilightforest.blockandchain");
        addItemInfo(FIREFLY, "documentationmod.twilightforest.firefly");
        addItemInfo(CICADA, "documentationmod.twilightforest.cicada");
        addItemWithDamageInfo(FIRE_JET, 0, "documentationmod.twilightforest.smokingblock");
        addItemWithDamageInfo(FIRE_JET, 3, "documentationmod.twilightforest.firejet");
        addItemWithDamageInfo(FIRE_JET, 1, "documentationmod.twilightforest.encasedsmoker");
        addItemWithDamageInfo(FIRE_JET, 6, "documentationmod.twilightforest.encasedfirejet");
        addItemWithDamageInfo(TWILIGHT_SAPLING, 5, "documentationmod.twilightforest.magictree.time");
        addItemWithDamageInfo(TWILIGHT_SAPLING, 6, "documentationmod.twilightforest.magictree.transformation");
        addItemWithDamageInfo(TWILIGHT_SAPLING, 7, "documentationmod.twilightforest.magictree.miner");
        addItemWithDamageInfo(TWILIGHT_SAPLING, 8, "documentationmod.twilightforest.magictree.sort");
        addItemWithDamageInfo(MAGIC_LOG_CORE, 0, "documentationmod.twilightforest.magictree.time");
        addItemWithDamageInfo(MAGIC_LOG_CORE, 1, "documentationmod.twilightforest.magictree.transformation");
        addItemWithDamageInfo(MAGIC_LOG_CORE, 2, "documentationmod.twilightforest.magictree.miner");
        addItemWithDamageInfo(MAGIC_LOG_CORE, 3, "documentationmod.twilightforest.magictree.sort");
        addItemWithDamageInfo(TOWER_DEVICE, 0, "documentationmod.twilightforest.reappearingblock");
        addItemWithDamageInfo(TOWER_DEVICE, 2, "documentationmod.twilightforest.vanishingblock");
        addItemWithDamageInfo(TOWER_DEVICE, 2, "documentationmod.twilightforest.lockedvanishingblock");
        addItemWithDamageInfo(TOWER_DEVICE, 4, "documentationmod.twilightforest.lockedvanishingblock");
        addItemWithDamageInfo(TOWER_DEVICE, 5, "documentationmod.twilightforest.carminitebuilder");
        addItemWithDamageInfo(TOWER_DEVICE, 9, "documentationmod.twilightforest.antibuilder");
        addItemWithDamageInfo(TOWER_DEVICE, 10, "documentationmod.twilightforest.ghasttrap");
        addItemWithDamageInfo(TOWER_DEVICE, 12, "documentationmod.twilightforest.carminitereactor");
        addItemInfo(TROPHY_PEDESTAL, "documentationmod.twilightforest.trophypedestal");
        addItemInfo(AURORA_BLACK, "documentationmod.twilightforest.aurora");
        IntStream.range(0,4).forEachOrdered(n -> addItemWithDamageInfo(CASTLE_DOOR, n, "documentationmod.twilightforest.castledoor"));
        addItemInfo(UNCRAFTING_TABLE, "documentationmod.twilightforest.uncraftingtable");

         */

        if(debugModIsDocumented) System.out.println("Twilight Forest documented");
    }
}
