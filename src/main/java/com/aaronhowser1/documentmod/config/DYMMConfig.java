package com.aaronhowser1.documentmod.config;

import com.aaronhowser1.documentmod.DocumentMod;
import net.minecraftforge.common.config.Config;

@Config(modid=DocumentMod.MODID)
public class DYMMConfig {

    @Config.Comment("Add information tabs to items added by Quark?")
    @Config.Name("Quark Info")
    public static boolean useQuark = true;

    @Config.Name("Quark's Vanilla Info")
    @Config.Comment("Add information tabs to vanilla items that Quark changes?")
    public static boolean useVanillaQuark = true;

    @Config.Comment("Toggle each vanilla item's info tab")
    @Config.Name("Vanilla Items Changed By Quark")
    public static SubCategory vanillaitemschangedbyquark = new SubCategory();

    public static class SubCategory {
        @Config.Comment("Say that Dispensers can place blocks etc?")
        public boolean infoDispenser = true;
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
        @Config.Comment("Say that you can slide down Ladders and place them from the top?")
        public boolean infoLadder = true;
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
    }
}