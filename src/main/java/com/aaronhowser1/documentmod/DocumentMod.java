package com.aaronhowser1.documentmod;

import com.aaronhowser1.documentmod.client.DescriptionChangingHandler;
import com.aaronhowser1.documentmod.config.DYMMConfig;
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
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Mod(
        modid = DocumentMod.MODID,
        name = DocumentMod.NAME,
        version = DocumentMod.VERSION,
        dependencies = "required-after:jei@[1.12.2-4.15.0.268,);",
        clientSideOnly = true
)
public class DocumentMod
{
    public static final String MODID = "dym";
    static final String NAME = "Document Your Mod Mod";
    static final String VERSION = "@VERSION@";

    @Mod.Instance(MODID)
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
        MinecraftForge.EVENT_BUS.post(new RegistrationHandler.ReloadModDocumentationEvent());
        MinecraftForge.EVENT_BUS.post(new DescriptionChangingHandler.ChangeDescriptionEvent(Objects.requireNonNull(Loader.instance().activeModContainer()).getMetadata()));
    }

    @Mod.EventHandler
    public void loadComplete(@Nonnull final FMLLoadCompleteEvent event) {
        if (this.hasChecked) return;
        this.hasChecked = true;

        final Consumer<String> loggingMethod = (DYMMConfig.debugItemsNoEntry)? logger::warn : logger::trace;
        final ProgressManager.ProgressBar bar = ProgressManager.push("Detecting undocumented items", Loader.instance().getActiveModList().size());
        // Okay, ready for the spam?
        Loader.instance().getActiveModList().stream()
                .map(ModContainer::getModId)
                .peek(bar::step)
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
                .map(triple -> triple.getMiddle().stream()
                        .map(it -> ImmutableTriple.of(
                                triple.getLeft(), it, triple.getRight().stream()
                                        .map(ModDocumentation::getReferredStacks)
                                        .flatMap(Collection::stream)
                                        .map(stack -> stack.getItem().getRegistryName())
                                        .filter(it::equals)
                                        .findFirst()
                        )).collect(Collectors.toList())
                )
                .flatMap(Collection::stream)
                .filter(triple -> !triple.getRight().isPresent())
                .map(triple -> "Found undocumented item '" + triple.getMiddle() + "' within the documented mod '" + triple.getLeft() + "'")
                .forEach(loggingMethod);
        ProgressManager.pop(bar);
    }
}
