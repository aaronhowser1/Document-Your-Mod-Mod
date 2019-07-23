package com.aaronhowser1.documentmod.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

public class CommonProxy {
    public void preInit(@Nonnull final FMLPreInitializationEvent event) {}
    public void init(@Nonnull final FMLInitializationEvent event) {}
    public void postInit(@Nonnull final FMLPostInitializationEvent event) {}
    public void loadComplete(@Nonnull final FMLLoadCompleteEvent event) {}

    public boolean canTranslate(@Nonnull final String key) {
        return true; // There is no way to check on the server
    }
}
