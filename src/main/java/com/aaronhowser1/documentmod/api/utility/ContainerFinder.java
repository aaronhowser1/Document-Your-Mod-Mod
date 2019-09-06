package com.aaronhowser1.documentmod.api.utility;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Utility class used to find {@link ModContainer}s from the
 * given ID.
 *
 * <p>This utility is implemented using the Singleton pattern,
 * so users are encouraged to rely on the provided
 * {@link #INSTANCE} field instead of attempting to call the
 * methods statically.</p>
 *
 * @since 1.1.2
 */
public enum ContainerFinder {
    /**
     * The unique instance of this utility class.
     *
     * @since 1.1.2
     */
    INSTANCE;

    /**
     * Attempts to find the container from the given ID, by querying
     * Forge's load manager.
     *
     * <p>Since Mod Containers do not change during the same run of
     * the game, users are actively encouraged to cache results of
     * this method.</p>
     *
     * <p>The implementation is also not the most efficient, since it
     * performs a lookup every time a container is requested. For this
     * reason, repeated calls to this method in a hotspot or otherwise
     * highly performing code may impact its run speed.</p>
     *
     * @param id
     *     The mod ID for which the container should be found. It can
     *     be null.
     * @return
     *     The container that was found, if one was found. Null
     *     otherwise. If the id passed in is null, the container
     *     will also be null.
     *
     * @since 1.1.2
     */
    @Nullable
    public ModContainer findContainerFromId(@Nullable final String id) {
        if (id == null) return null;
        return Loader.instance().getActiveModList().stream()
                .filter(it -> it.getModId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Attempts to find the container from the given ID, by querying
     * Forge's load manager, and throws an exception if none is found.
     *
     * <p>Since Mod Containers do not change during the same run of
     * the game, users are actively encouraged to cache results of
     * this method.</p>
     *
     * <p>The implementation is also not the most efficient, since it
     * performs a lookup every time a container is requested. For this
     * reason, repeated calls to this method in a hotspot or otherwise
     * highly performing code may impact its run speed.</p>
     *
     * @param id
     *     The mod ID for which the container should be found. It must
     *     not be null.
     * @return
     *     The container that was found. If none was found, a
     *     {@link NullPointerException} is raised instead.
     *
     * @since 1.1.2
     */
    @Nonnull
    public ModContainer ensureContainerFromId(@Nonnull final String id) {
        return Objects.requireNonNull(this.findContainerFromId(id));
    }
}
