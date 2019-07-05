package com.aaronhowser1.documentmod.config;

import com.aaronhowser1.documentmod.DocumentMod;
import net.minecraftforge.common.config.Config;

@Config(modid=DocumentMod.MODID)
public class DYMMConfig {

    @net.minecraftforge.common.config.Config.Name("Use Quark?")
    public static boolean useQuark = true;

}
