package com.aaronhowser1.dymm.api.consume;

import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import com.aaronhowser1.dymm.api.documentation.Target;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * Represents a consumer for a particular set of {@link DocumentationData}
 * types.
 *
 * <p>Implementations of this interface should be declared by clients through
 * the service pattern: they will be discovered by the main implementation as
 * needed through a {@link java.util.ServiceLoader}.</p>
 *
 * <p>Implementations should also not rely on intrinsic global state.</p>
 *
 * @since 2.0.0
 */
public interface DocumentationDataConsumer {
    /**
     * Gets the list of {@link DocumentationData} types this consumer is able
     * to consume, as a {@link List} of {@link ResourceLocation}s.
     *
     * <p>The returned list must not contain any duplicate elements and every
     * element shall be represented as a pair, as specified by
     * {@link ResourceLocation}.</p>
     *
     * <p>As an example, an implementation that is able to consume tooltip
     * documentation entries could use a {@link ResourceLocation} that
     * represents the name-spaced string {@code "minecraft:tooltip"}.</p>
     *
     * @return
     *      A non-{@code null} {@link List} of compatible
     *      {@link DocumentationData} types.
     *
     * @see DocumentationData
     * @since 2.0.0
     */
    @Nonnull List<ResourceLocation> getCompatibleTypes();

    /**
     * Consumes the given {@link DocumentationData} that targets the given
     * {@link Set} of {@link Target}s.
     *
     * <p>Implementations are free to do whatever they want with the given
     * data, except from modifying it. The main implementation is free to
     * make a defensive copy of this data in case such operations are performed
     * by malicious implementations.</p>
     *
     * <p>The given {@code DocumentationData} type is guaranteed to be one of
     * the ones specified in {@link #getCompatibleTypes()}. Implementations can
     * leverage the specifics of {@code DocumentationData} to discover which
     * specific type the data is for.</p>
     *
     * @param data
     *      The {@link DocumentationData} to consume. Guaranteed not to be
     *      {@code null} and of one of the types specified in
     *      {@link #getCompatibleTypes()}.
     * @param targets
     *      A {@link Set} of {@link Target}s to which the given {@code data}
     *      applies to. Guaranteed not to be {@code null}, but it could be
     *      empty if the data does not apply to any {@code Target}.
     *
     * @see DocumentationData
     * @see Target
     * @since 2.0.0
     */
    void consumeData(@Nonnull final DocumentationData data, @Nonnull final Set<Target> targets);

    /**
     * Called when this consumer is first created and loaded.
     *
     * <p>Implementations are free to do whatever operation they deem necessary
     * to load the necessary data they need in order to consume the given
     * {@link DocumentationData}.</p>
     *
     * @implNote
     *      By default, this method does nothing.
     *
     * @since 2.0.0
     */
    default void onCreation() {}
}
