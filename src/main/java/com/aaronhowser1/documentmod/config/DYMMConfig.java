package com.aaronhowser1.documentmod.config;

import com.aaronhowser1.documentmod.DocumentMod;
import net.minecraftforge.common.config.Config;

@Config(modid=DocumentMod.MOD_ID)
public class DYMMConfig {

    @Config.Comment("All Quark settings")
    @Config.Name("Quark")
    public static Quark quark = new Quark();

    public static class Quark {

        @Config.Comment("Add information tabs to items added by Quark?")
        @Config.Name("Quark Info")
        public boolean useQuark = true;

        @Config.Name("Quark's Vanilla Info")
        @Config.Comment("Add information tabs to vanilla items that Quark changes?")
        public boolean useVanillaQuark = true;

        @Config.Name("Quark's Decorative Blocks")
        @Config.Comment("Add information to Quark's decorative blocks?")
        public boolean useQuarkDecor = true;

        @Config.Name("Quark's GUI Elements")
        @Config.Comment("Add information about Quark's UI elements?")
        public boolean useQuarkGUI = true;
    }

    @Config.Name("Nature's Compass info")
    @Config.Comment("Document Nature's Compass?")
    public static boolean useNaturesCompass = true;

    @Config.Name("Tinkers' Construct info")
    @Config.Comment("Document Tinkers' Construct?")
    public static boolean useTinkersConstruct = true;

    @Config.Name("Twilight Forest info")
    @Config.Comment("Document Twilight Forest?")
    public static boolean useTwilightForest = true;

    @Config.Name("Waystones info")
    @Config.Comment("Document Waystones?")
    public static boolean useWaystone = true;

    @Config.Name("Refined Storage info")
    @Config.Comment("Document Refined Storage")
    public static boolean useRefinedStorage = true;

    @Config.Name("Iron Chest info")
    @Config.Comment("Document Iron Chest")
    public static boolean useIronChest = true;

    @Config.Name("Debug - Mod Documented")
    @Config.Comment("Displays in console what mods are documented")
    public static boolean debugModIsDocumented = false;

    @Config.Name("Debug - Not Documented Items")
    @Config.Comment("Displays in console what items in documented mods aren't documented")
    public static boolean debugItemsNoEntry = false;
}