package com.aaronhowser1.documentmod;

import com.aaronhowser1.documentmod.json.DocumentationLoader;
import com.aaronhowser1.documentmod.json.DocumentationRegistry;
import com.aaronhowser1.documentmod.json.ModDocumentation;
import com.aaronhowser1.documentmod.json.ReloadHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = DocumentMod.MODID)
public final class RegistrationHandler {

    @SuppressWarnings("WeakerAccess") // Fuck you Forge, needing to have events public
    public static class ReloadModDocumentationEvent extends Event {
        public ReloadModDocumentationEvent() {}
    }

    private RegistrationHandler() {}

    @SubscribeEvent
    public static void onNewRegistry(@Nonnull final RegistryEvent.NewRegistry event) {
        try {
            DocumentationRegistry.INSTANCE.setRegistry(new RegistryBuilder<ModDocumentation>()
                    .setName(new ResourceLocation(DocumentMod.MODID, "documentation"))
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
}
