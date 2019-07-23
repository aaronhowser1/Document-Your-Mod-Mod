package com.aaronhowser1.documentmod;

import com.aaronhowser1.documentmod.json.DocumentationLoader;
import com.aaronhowser1.documentmod.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

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
@Mod.EventBusSubscriber(modid = DocumentMod.MODID)
public class DocumentMod
{
    public static final String MODID = "documentmod";
    public static final String NAME = "Document Your Mod Mod";
    public static final String VERSION = "@VERSION@";

    @Mod.Instance(MODID)
    public static DocumentMod instance;

    public static Logger logger;

    @SidedProxy(modId = DocumentMod.MODID, clientSide = "com.aaronhowser1.documentmod.proxy.ClientProxy", serverSide = "com.aaronhowser1.documentmod.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(@Nonnull final FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(@Nonnull final FMLPostInitializationEvent event) {
        DocumentationLoader.INSTANCE.loadFromJson();
        proxy.postInit(event);
    }
}
