package com.aaronhowser1.documentmod.config;

import com.aaronhowser1.documentmod.DocumentMod;
import net.minecraftforge.common.config.Config;

@Config(modid=DocumentMod.MODID)
public class DYMMConfig {

    @Config.Comment("All Quark settings")
    @Config.Name("Quark")
    public static SubCategory quarksubcat = new SubCategory();

    public static class SubCategory {

        @Config.Comment("Add information tabs to items added by Quark?")
        @Config.Name("Quark Info")
        public boolean useQuark = true;

        @Config.Name("Quark's Vanilla Info")
        @Config.Comment("Add information tabs to vanilla items that Quark changes?")
        public boolean useVanillaQuark = true;

        @Config.Comment("Say that Dispensers can plant Seeds?")
        public boolean infoDispenserSeeds = true;
        @Config.Comment("Say that Dispensers can place blocks?")
        public boolean infoDispenserBlocks = true;
        @Config.Comment("Say that Dispensers can fill Jukeboxes?")
        public boolean infoDispenserRecords = true;
        @Config.Comment("Say that Sticky Pistons can move Tile Entities?")
        public boolean infoStickyPiston = true;
        @Config.Comment("Say that Leads can connect 2 Fences?")
        public boolean infoLead = true;
        @Config.Comment("Say that Boats can have Chests?")
        public boolean infoBoat = true;
        @Config.Comment("Say that Shulker Boxes don't explode?")
        public boolean infoShulkerBoxExplosion = true;
        @Config.Comment("Say that Shulker Boxes show their contents when hovered over?")
        public boolean infoShulkerBoxHover = true;
        @Config.Comment("Say that you can add items to Shulker Boxes without placing them?")
        public boolean infoShulkerBoxAdd = true;
        @Config.Comment("Say that Compasses in other dimensions point towards the portal?")
        public boolean infoCompass = true;
        @Config.Comment("Say that you can place Ladders from the top?")
        public boolean infoDeployLadder = true;
        @Config.Comment("Say that you can slide down Ladders?")
        public boolean infoSlideDownLadders = true;
        @Config.Comment("Say that you can swap Armor Stand armor by sneak right clicking?")
        public boolean infoArmorStand = true;
        @Config.Comment("Say that Elytras can be dyed?")
        public boolean infoElytra = true;
        @Config.Comment("Say that you can edit Signs?")
        public boolean infoSign = true;
        @Config.Comment("Say that Villagers follow Emerald Blocks?")
        public boolean infoEmeraldBlock = true;
        @Config.Comment("Say that you can place Glowstone Dust?")
        public boolean infoGlowstoneDust = true;
        @Config.Comment("Say that you can place Gunpowder?")
        public boolean infoGunpowder = true;
        @Config.Comment("Say that you can throw Dragon Breath?")
        public boolean infoDragonBreath = true;
        @Config.Comment("Say that Maps show their contents when hovered over?")
        public boolean infoMap = true;
        @Config.Comment("Say that Enchanted Books show what tools they can go on?")
        public boolean infoEnchantedBook = true;
        @Config.Comment("Say that Item Frames can be placed on floors and ceilings?")
        public boolean infoItemFrames = true;
        @Config.Comment("Say that there are new Quark Banner things?")
        public boolean infoQuarkBannerMotifs = true;
        @Config.Comment("Say that Banners have more layers?")
        public boolean infoBannerLayers = true;
        @Config.Comment("Say that Boats can have Boats can have Banners?")
        public boolean infoBannersOnBoats = true;
        @Config.Comment("Say that you can dye item names in the Anvil?")
        public boolean infoColorItemNames = true;
        @Config.Comment("Say that you can sit on Stairs?")
        public boolean infoSitOnStairs = true;
        @Config.Comment("Say that animals eat food off the ground?")
        public boolean infoAnimalsEatFood = true;
        @Config.Comment("Say that you can place Blaze Rods?")
        public boolean infoPlaceBlazeRods = true;
        @Config.Comment("Say that Pistons can move items?")
        public boolean infoPistonsMoveItems = true;
        @Config.Comment("Say that Endermites can burrow into Purpur to make Shulkers?")
        public boolean infoEndermiteShulkers = true;
        @Config.Comment("Say that you can make Map markers?")
        public boolean infoMapMarkers = true;
        @Config.Comment("Say that Noteblocks have a new interface?")
        public boolean infoNoteBlockInterface = true;
        @Config.Comment("Say that Noteblocks will play mob sounds?")
        public boolean infoNoteBlockMobSounds = true;
        @Config.Comment("Say that Poison Potatoes stop mobs from aging?")
        public boolean infoPoisonBabies = true;
        @Config.Comment("Say that Chickens shed feathers?")
        public boolean infoChickensShed = true;
        @Config.Comment("Say that Chickens can be sheared?")
        public boolean infoShearChickens = true;
        @Config.Comment("Say that Doors open together?")
        public boolean infoDoubleDoors = true;
        @Config.Comment("Say that Axes quickly break Leaves?")
        public boolean infoAxesLeaves = true;
        @Config.Comment("Say that Hoes break a 5x5 of plants?")
        public boolean infoHoesAoE = true;
        @Config.Comment("Say that not everyone has to sleep to skip night?")
        public boolean infoSleepVote = true;
        @Config.Comment("Say that you can right-click to harvest?")
        public boolean infoRightClickHarvest = true;
    }

    @Config.Name("Nature's Compass info")
    public static boolean useNaturesCompass = true;

    @Config.Name("Tinkers' Construct info")
    public static boolean useTinkersConstruct = true;

    @Config.Name("Twilight Forest info")
    public static boolean useTwilightForest = true;

    @Config.Name("Waystones info")
    public static boolean useWaystone = true;

    @Config.Name("Refined Storage info")
    public static boolean useRefinedStorage = true;

    @Config.Name("Debug")
    public static boolean debugModIsDocumented = false;
}