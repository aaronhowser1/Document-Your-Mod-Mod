package com.aaronhowser1.documentmod.api.event;

import com.aaronhowser1.documentmod.api.utility.DocumentedModEntry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * Event that gets fired whenever the documentation status needs to be populated.
 *
 * <p>Mods that subscribe to this event should append entries to the provided list
 * specifying which mod they are providing support for and which version they are
 * supporting. This will allow Document Your Mod Mod to add your entry to the list
 * shown in the Mod Information screen.</p>
 *
 * <p>This event is fired only once during startup. Your mod should react the same
 * regardless of when the event is fired. It is anyway guaranteed that all mod
 * containers are completely constructed before this event is fired.</p>
 *
 * <p>This event gets fired on the Minecraft Forge Event Bus.</p>
 *
 * @since 1.1.2
 */
public final class PopulateDocumentationStatusEvent extends Event {

    private final List<DocumentedModEntry> documentedMods;

    /**
     * Creates a new event instance with an empty list.
     *
     * @implNote
     *     Mods should never call this. Let Document Your Mod Mod
     *     handle the creation of this event.
     *
     * @since 1.1.2
     */
    public PopulateDocumentationStatusEvent() {
        this(Lists.newArrayList());
    }

    /**
     * Creates a new event instance with the given list.
     *
     * @param documentedMods
     *     The list to use when creating this event. It must
     *     not be null, but can be empty.
     *
     * @implNote
     *     Mods should never call this. Let Document Your Mod Mod
     *     handle the creation of this event.
     *
     * @implSpec
     *     The given list is automatically copied as a defensive
     *     measure. Creators of this event should not attempt to
     *     modify this list directly.
     *
     * @since 1.1.2
     */
    @SuppressWarnings("WeakerAccess")
    public PopulateDocumentationStatusEvent(@Nonnull final List<DocumentedModEntry> documentedMods) {
        this.documentedMods = Lists.newArrayList(documentedMods);
    }

    /**
     * Appends the given entry to the list of documented mods.
     *
     * @param entry
     *     The entry to append to the list. It must not be
     *     null.
     *
     * @since 1.1.2
     */
    @SuppressWarnings("WeakerAccess")
    public void appendModEntry(@Nonnull final DocumentedModEntry entry) {
        this.documentedMods.add(entry);
    }

    /**
     * Appends the given entries to the list of documented mods.
     *
     * @param entries
     *     The entries to append to the list. The array given
     *     must not be null and it must not contain null
     *     entries.
     *
     * @since 1.1.2
     */
    public void appendModEntries(@Nonnull final DocumentedModEntry... entries) {
        Arrays.stream(entries).forEach(this::appendModEntry);
    }

    /**
     * Appends the entries provided by the given {@link Iterable}
     * to the list of documented mods.
     *
     * @param entries
     *     An {@link Iterable} providing the items to append to
     *     the list. It must not be null or provide null entries.
     *
     * @since 1.1.2
     */
    @SuppressWarnings("unused")
    public void appendModEntries(@Nonnull final Iterable<DocumentedModEntry> entries) {
        entries.forEach(this::appendModEntry);
    }

    /**
     * Gets all the documented mods that have been added up until
     * this moment.
     *
     * <p>The returned list is not automatically updated when the
     * contents of the list contained by this event are.</p>
     *
     * @return
     *     A list containing all the documented mods that have
     *     been added up until this moment.
     *
     * @implNote
     *     Implementations are encouraged to be defensive and
     *     return a copy of the list to protect themselves from
     *     malicious programs.
     *
     * @since 1.1.2
     */
    @Nonnull
    public List<DocumentedModEntry> getDocumentedMods() {
        return ImmutableList.copyOf(this.documentedMods);
    }
}
