package com.aaronhowser1.documentmod;

import com.aaronhowser1.documentmod.json.DocumentationLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = DocumentMod.MODID,
        name = DocumentMod.NAME,
        version = DocumentMod.VERSION,
        dependencies =
                "required-after:jei@[1.12.2-4.15.0.268,);" +
                "after:quark@[r1.5-167,);" +
                "after:naturescompass;" +
                "after:tconstruct;" +
                "after:twilightforest;" +
                "after:refinedstorage;" +
                "after:waystones;",
        clientSideOnly = true
)
public class DocumentMod
{
    public static final String MODID = "documentmod";
    public static final String NAME = "Document Your Mod Mod";
    public static final String VERSION = "@VERSION@";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        DocumentationLoader.INSTANCE.loadFromJson();
    }
}
