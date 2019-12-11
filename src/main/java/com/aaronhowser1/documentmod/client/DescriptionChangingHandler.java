package com.aaronhowser1.documentmod.client;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.ModId;
import com.aaronhowser1.documentmod.api.event.PopulateDocumentationStatusEvent;
import com.aaronhowser1.documentmod.api.utility.DocumentedModEntry;
import com.aaronhowser1.documentmod.event.ChangeMetadataEvent;
import com.google.common.collect.Lists;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.List;

//@Mod.EventBusSubscriber(modid = DocumentMod.MOD_ID, value = Side.CLIENT)
public final class DescriptionChangingHandler {

    @SubscribeEvent
    public static void onDescriptionChange(@Nonnull final ChangeMetadataEvent event) {
        final StringBuilder builder = new StringBuilder(event.getMetadata().description);
        builder.append("\n\nDocumented mods:\n");
        final PopulateDocumentationStatusEvent fired = new PopulateDocumentationStatusEvent();
        MinecraftForge.EVENT_BUS.post(fired);
        final List<DocumentedModEntry> entries = Lists.newArrayList(fired.getDocumentedMods());
        entries.sort((documentYour, modMod) -> documentYour.getId().compareToIgnoreCase(modMod.getId()));
        entries.forEach(entry -> builder.append(entry.getDisplayableString()));
        event.getMetadata().description = builder.toString();
    }

    @SubscribeEvent
    @SuppressWarnings("SpellCheckingInspection")
    public static void onPopulateStatusEvent(@Nonnull final PopulateDocumentationStatusEvent event) {
        event.appendModEntries(
                DocumentedModEntry.of(ModId.QUARK, "Quark", "r1.6"),
                DocumentedModEntry.of(ModId.IRONCHEST, "Iron Chests", "7.0.72"),
                DocumentedModEntry.of(ModId.NATURES_COMPASS, "Nature's Compass", "1.5.1"),
                DocumentedModEntry.of(ModId.TINKERS_CONSTRUCT, "Tinkers' Construct", "2.12.0"),
                DocumentedModEntry.of(ModId.WAYSTONES, "Waystones", "4.0.72"),
                DocumentedModEntry.of(ModId.TWILIGHT_FOREST, "Twilight Forest", "3.8"),
                DocumentedModEntry.of(ModId.REFINED_STORAGE, "Refined Storage", "1.6"),
                DocumentedModEntry.of(ModId.REFINED_STORAGE_ADDONS, "Refined Storage Addons", "0.4")
        );
    }
}
