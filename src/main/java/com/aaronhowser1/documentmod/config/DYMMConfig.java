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

    @Config.Name("Iron Chest info")
    public static boolean useIronChest = true;

    @Config.Name("Debug - Mod Documented")
    public static boolean debugModIsDocumented = false;

    @Config.Name("Debug - Not Documented Items")
    public static boolean debugItemsNoEntry = false;
}