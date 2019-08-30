package com.aaronhowser1.documentmod;

import com.aaronhowser1.documentmod.api.utility.ContainerFinder;
import com.aaronhowser1.documentmod.config.DYMMConfig;
import com.aaronhowser1.documentmod.event.ChangeMetadataEvent;
import com.aaronhowser1.documentmod.event.LoadFinishedEvent;
import com.aaronhowser1.documentmod.event.ReloadModDocumentationEvent;
import com.aaronhowser1.documentmod.json.DocumentationRegistry;
import com.aaronhowser1.documentmod.json.ModDocumentation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Mod(modid = DocumentMod.MOD_ID, name = DocumentMod.NAME, version = DocumentMod.VERSION, dependencies = "required-after:jei@[1.12.2-4.15.0.268,);", clientSideOnly = true)
public class DocumentMod
{
    public static final String MOD_ID = "dym";
    static final String NAME = "Document Your Mod Mod";
    static final String VERSION = "@VERSION@";

    @Mod.Instance(MOD_ID)
    @SuppressWarnings("unused")
    public static DocumentMod instance;

    public static Logger logger = LogManager.getLogger("Document Your Mod Mod"); // Exactly what FMLPreInitializationEvent#getModLog does

    private boolean hasChecked = false;

    @Mod.EventHandler
    public void preInit(@Nonnull final FMLPreInitializationEvent event) {}

    @Mod.EventHandler
    public void init(@Nonnull final FMLInitializationEvent event) {}

    @Mod.EventHandler
    public void postInit(@Nonnull final FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.post(new ReloadModDocumentationEvent());
        MinecraftForge.EVENT_BUS.post(new ChangeMetadataEvent(ContainerFinder.INSTANCE.ensureContainerFromId(MOD_ID).getMetadata()));
    }

    @Mod.EventHandler
    public void loadComplete(@Nonnull final FMLLoadCompleteEvent event) {
        if (this.hasChecked) return;
        this.hasChecked = true;

        MinecraftForge.EVENT_BUS.post(new LoadFinishedEvent());
    }
}
