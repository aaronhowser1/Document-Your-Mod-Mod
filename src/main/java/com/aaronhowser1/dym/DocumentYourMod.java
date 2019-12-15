package com.aaronhowser1.dym;

import com.aaronhowser1.dym.common.configuration.MainConfigurationHandler;
import com.aaronhowser1.dym.common.configuration.OldConfigurationMigrationTool;
import com.aaronhowser1.dym.common.loading.LoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, dependencies = Constants.MOD_DEPENDENCIES)
public final class DocumentYourMod {
    private static final L LOG = L.create(Constants.MOD_NAME, "Lifecycle");

    @Mod.EventHandler
    public void onConstruct(@Nonnull final FMLConstructionEvent event) {}

    @Mod.EventHandler
    public void onPreInitialization(@Nonnull final FMLPreInitializationEvent event) {
        LOG.info("Loading main mod configurations");
        MainConfigurationHandler.initializeMainModConfiguration();

        LOG.info("Attempting to migrate old configuration data");
        OldConfigurationMigrationTool.attemptToMigrateConfiguration();

        LOG.info("Discovering and registering loaders");
        LoaderRegistry.INSTANCE.discoverLoadersFromClasspath();
    }

    @Mod.EventHandler
    public void onInitialization(@Nonnull final FMLInitializationEvent event) {}

    @Mod.EventHandler
    public void onPostInitialization(@Nonnull final FMLPostInitializationEvent event) {}

    @Mod.EventHandler
    public void onLoadComplete(@Nonnull final FMLLoadCompleteEvent event) {}
}
