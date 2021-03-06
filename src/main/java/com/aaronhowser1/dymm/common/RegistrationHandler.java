package com.aaronhowser1.dymm.common;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.common.consume.DocumentationConsumerDispatcher;
import com.aaronhowser1.dymm.common.loading.LoaderRegistry;
import com.aaronhowser1.dymm.common.loading.LoadingHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public final class RegistrationHandler {
    static IForgeRegistry<DocumentationEntry> documentationRegistry;

    @SubscribeEvent
    public static void onNewRegistryEvent(@Nonnull final RegistryEvent.NewRegistry event) {
        documentationRegistry = new RegistryBuilder<DocumentationEntry>()
                .setName(new ResourceLocation(Constants.MOD_ID, "documentation"))
                .setType(DocumentationEntry.class)
                .setMaxID(Integer.MAX_VALUE >> 5)
                .disableSaving()
                .create();
    }

    @SubscribeEvent
    public static void onDocumentationRegistryEvent(@Nonnull final RegistryEvent.Register<DocumentationEntry> event) {
        LoadingHandler.bindRegistry(event.getRegistry());
        LoaderRegistry.INSTANCE.registerMetadataListeners();
        DocumentationConsumerDispatcher.bind(event.getRegistry());
        LoadingHandler.performLoading();
    }
}
