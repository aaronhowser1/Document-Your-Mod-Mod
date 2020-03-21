package com.aaronhowser1.dymm.api.loading.metadata;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Represents a registry for {@link MetadataListener}s, used to register and
 * find all implementations for a specific identifier.
 *
 * <p>This class is mainly used as an interface and should not be implemented
 * by clients, but only used to refer to those specific parts of the actual
 * implementation.</p>
 *
 * @see MetadataListener
 * @since 2.0.0
 */
public interface MetadataListenerRegistry {
    /**
     * Registers a new {@link MetadataListener} for the given identifier.
     *
     * <p>It is possible for multiple {@code MetadataListener}s to be
     * registered under one identifier, much like the opposite, with multiple
     * identifiers being registered under one listener. The latter is
     * discouraged, though, as a matter of style and code clarity.</p>
     *
     * @apiNote
     *      Implementations are encouraged to reject registrations for the
     *      {@code factories} identifier, though this is not mandatory. In any
     *      case, the main implementation must NOT invoke any metadata listener
     *      when identifying a file named {@code _factories.json}.
     *
     * @param metadataIdentifier
     *      The identifier of the metadata to which the listener should listen
     *      to. This metadata cannot be {@code null} or empty. The identifier
     *      is defined as the part that follows the {@code _} character in the
     *      file name (e.g., in {@code _metadata.json}, {@code metadata} is the
     *      identifier.
     * @param listener
     *      The {@link MetadataListener} that should be invoked when a file
     *      representing the target metadata is found. It must not be
     *      {@code null}.
     *
     * @since 2.0.0
     */
    void register(@Nonnull final String metadataIdentifier, @Nonnull final MetadataListener listener);

    /**
     * Finds all the registered {@link MetadataListener}s for a specific
     * identifier.
     *
     * <p>The metadata listeners are grouped into a list, but the order does
     * not reflect the registration order nor any other order in particular.
     * Different calls for this method with the same parameters may differ in
     * the ordering used in the returned list.</p>
     *
     * @param metadataIdentifier
     *      The identifier of the metadata to lookup for listeners. The
     *      identifier cannot be {@code null} or empty.
     * @return
     *      A {@link List} of {@link MetadataListener}s that are registered
     *      under the given identifier. It will not be {@code null}, but it
     *      could be empty.
     *
     * @since 2.0.0
     */
    @Nonnull List<MetadataListener> findAllFor(@Nonnull final String metadataIdentifier);
}
