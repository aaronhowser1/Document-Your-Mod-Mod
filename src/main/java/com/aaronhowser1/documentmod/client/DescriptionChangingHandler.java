package com.aaronhowser1.documentmod.client;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.DocumentedMods;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid = DocumentMod.MODID, value = Side.CLIENT)
public class DescriptionChangingHandler {

    public static class ChangeDescriptionEvent extends Event {
        private final ModMetadata metadata;

        public ChangeDescriptionEvent(@Nonnull final ModMetadata metadata) {
            this.metadata = metadata;
        }

        @Nonnull
        public ModMetadata getMetadata() {
            return this.metadata;
        }
    }

    @SubscribeEvent
    public static void onDescriptionChange(@Nonnull final ChangeDescriptionEvent event) {
        final StringBuilder builder = new StringBuilder(event.getMetadata().description);
        builder.append("\n\nDocumented mods:\n");
        final List<DocumentedMods.DocumentedMod> documentedMods = DocumentedMods.INSTANCE.getAllDocumentedMods();
        documentedMods.sort((a, b) -> a.getId().compareToIgnoreCase(b.getId()));
        documentedMods.forEach(it -> builder.append(it.format(DocumentedMods.INSTANCE.findContainerFor(it).orElse(null))));
        event.getMetadata().description = builder.toString();
    }
}
