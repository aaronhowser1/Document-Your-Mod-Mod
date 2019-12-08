package com.aaronhowser1.dym;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

import javax.annotation.Nonnull;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, dependencies = Constants.MOD_DEPENDENCIES)
public final class DocumentYourMod {
    @Mod.EventHandler
    public void onConstruct(@Nonnull final FMLConstructionEvent event) {}

    @Mod.EventHandler
    public void onPreInitialization(@Nonnull final FMLPreInitializationEvent event) {}

    @Mod.EventHandler
    public void onInitialization(@Nonnull final FMLInitializationEvent event) {}

    @Mod.EventHandler
    public void onPostInitialization(@Nonnull final FMLPostInitializationEvent event) {}

    @Mod.EventHandler
    public void onLoadComplete(@Nonnull final FMLLoadCompleteEvent event) {}
}
