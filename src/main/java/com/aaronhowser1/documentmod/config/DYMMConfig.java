package com.aaronhowser1.documentmod.config;

import com.aaronhowser1.documentmod.DocumentMod;
import net.minecraftforge.common.config.Config;

@Config(modid=DocumentMod.MODID)
public class DYMMConfig {

    @Config.Comment("Add information tabs to items added by Quark?")
    public static boolean useQuark = true;

    @Config.Comment("Add information tabs to vanilla items that Quark changes?")
    public static boolean useVanillaQuark = true;
}
