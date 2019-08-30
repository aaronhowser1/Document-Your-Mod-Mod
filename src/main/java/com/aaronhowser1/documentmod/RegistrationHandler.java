package com.aaronhowser1.documentmod;

import com.aaronhowser1.documentmod.config.DYMMConfig;
import com.aaronhowser1.documentmod.event.LoadFinishedEvent;
import com.aaronhowser1.documentmod.event.ReloadModDocumentationEvent;
import com.aaronhowser1.documentmod.json.DocumentationLoader;
import com.aaronhowser1.documentmod.json.DocumentationRegistry;
import com.aaronhowser1.documentmod.json.ModDocumentation;
import com.aaronhowser1.documentmod.json.ReloadHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = DocumentMod.MOD_ID)
public final class RegistrationHandler {

    private RegistrationHandler() {}

    @SubscribeEvent
    public static void onNewRegistry(@Nonnull final RegistryEvent.NewRegistry event) {
        try {
            DocumentationRegistry.INSTANCE.setRegistry(new RegistryBuilder<ModDocumentation>()
                    .setName(new ResourceLocation(DocumentMod.MOD_ID, "documentation"))
                    .setType(ModDocumentation.class)
                    .setMaxID(Integer.MAX_VALUE >> 5)
                    .disableSaving()
                    .create());
            DocumentMod.logger.info("Successfully created documentation registry");
        } catch (@Nonnull final IllegalAccessException e) {
            throw new RuntimeException("The registry was already created! This is impossible", e);
        }
    }

    @SubscribeEvent
    public static void onDocumentationRegistry(@Nonnull final RegistryEvent.Register<ModDocumentation> event) {
        // Do not add a progress bar with the mod's name for Boson compatibility
        // Also, we are guaranteed to run after ForgeRegistries.BLOCKS and ForgeRegistries.ITEMS so this is
        // a non-issue.
        DocumentationLoader.INSTANCE.loadAndRegister(event.getRegistry());
        // Now let's dump the registry, just to make sure
        DocumentationRegistry.INSTANCE.dump();
    }

    @SubscribeEvent
    public static void onReloadDocumentation(@Nonnull final ReloadModDocumentationEvent event) {
        // And now let the hacks begin
        ReloadHandler.INSTANCE.reload();
        ReloadHandler.INSTANCE.dumpAndClear();
    }

    @SubscribeEvent
    public static void onLoadFinished(@Nonnull final LoadFinishedEvent event) {
        final Consumer<String> loggingMethod = (DYMMConfig.debugItemsNoEntry)? DocumentMod.logger::warn : DocumentMod.logger::trace;

        final ProgressManager.ProgressBar bar = ProgressManager.push("Detecting undocumented items", Loader.instance().getActiveModList().size());

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
