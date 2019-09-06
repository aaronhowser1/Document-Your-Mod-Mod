package com.aaronhowser1.documentmod.api.event;

import com.aaronhowser1.documentmod.api.utility.DocumentedModEntry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public final class PopulateDocumentationStatusEvent extends Event {

    private final List<DocumentedModEntry> documentedMods;

    public PopulateDocumentationStatusEvent() {
        this(Lists.newArrayList());
    }

    @SuppressWarnings("WeakerAccess")
    public PopulateDocumentationStatusEvent(@Nonnull final List<DocumentedModEntry> documentedMods) {
        this.documentedMods = documentedMods;
    }

    public void appendModEntry(@Nonnull final DocumentedModEntry entry) {
        this.documentedMods.add(entry);
    }

    public void appendModEntries(@Nonnull final DocumentedModEntry... entries) {
        Arrays.stream(entries).forEach(this::appendModEntry);
    }

    @Nonnull
    public List<DocumentedModEntry> getDocumentedMods() {
        return ImmutableList.copyOf(this.documentedMods);
    }
}
