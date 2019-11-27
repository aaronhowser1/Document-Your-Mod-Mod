package com.aaronhowser1.dym;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

import javax.annotation.Nonnull;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, dependencies = Constants.MOD_DEPENDENCIES, clientSideOnly = true)
public final class DocumentYourMod {

    private final com.aaronhowser1.documentmod.DocumentMod bridge = new com.aaronhowser1.documentmod.DocumentMod();

    @Mod.EventHandler
    public void onConstruct(@Nonnull final FMLConstructionEvent event) {}

    @Mod.EventHandler
    public void onPreInitialization(@Nonnull final FMLPreInitializationEvent event) {bridge.preInit(event);}

    @Mod.EventHandler
    public void onInitialization(@Nonnull final FMLInitializationEvent event) {bridge.init(event);}

    @Mod.EventHandler
    public void onPostInitialization(@Nonnull final FMLPostInitializationEvent event) {bridge.postInit(event);}

    @Mod.EventHandler
    public void onLoadComplete(@Nonnull final FMLLoadCompleteEvent event) {bridge.loadComplete(event);}
}
