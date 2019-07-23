package com.aaronhowser1.documentmod;

import com.aaronhowser1.documentmod.config.DYMMConfig;
import com.aaronhowser1.documentmod.json.DocumentationLoader;
import com.aaronhowser1.documentmod.json.DocumentationRegistry;
import com.aaronhowser1.documentmod.proxy.CommonProxy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.stream.Collectors;

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

    @EventHandler
    public void loadComplete(@Nonnull final FMLLoadCompleteEvent event) {
        proxy.loadComplete(event);
        if (DYMMConfig.debugItemsNoEntry) {
            // Okay, ready for the spam?
            Loader.instance().getActiveModList().stream()
                    .map(ModContainer::getModId)
                    .map(modId -> ImmutablePair.of(modId, DocumentationRegistry.INSTANCE.getDocumentationForMod(modId)))
                    .filter(pair -> !pair.getRight().isEmpty())
                    .map(pair -> ImmutableTriple.of(
                            pair.getLeft(),
                            ForgeRegistries.ITEMS.getEntries().stream()
                                    .filter(it -> it.getKey().getNamespace().equals(pair.getLeft()))
                                    .map(Map.Entry::getKey)
                                    .collect(Collectors.toList()),
                            pair.getRight())
                    )
                    .map(triple -> ImmutablePair.of(triple.getLeft(), triple.getMiddle().stream()
                            .map(it -> ImmutableTriple.of(
                                    triple.getLeft(), it, triple.getRight().stream()
                                            .filter(entry -> it.equals(entry.getReferredStack().getItem().getRegistryName()))
                                            .findFirst()
                            )).collect(Collectors.toList()))
                    )
                    .flatMap(pair -> pair.getRight().stream())
                    .filter(triple -> !triple.getRight().isPresent())
                    .map(triple -> ImmutablePair.of(triple.getLeft(), triple.getMiddle()))
                    .forEach(pair -> logger.warn("Found undocumented item '" + pair.getRight() + "' within the documented mod '" + pair.getLeft() + "'"));
        }
    }
}
