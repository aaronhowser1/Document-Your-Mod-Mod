package com.aaronhowser1.dymm.api.documentation;

import net.minecraft.util.ResourceLocation;

import java.util.List;
import javax.annotation.Nonnull;

/**
 * Identifies a set of data that is present in a {@link DocumentationEntry}.
 *
 * <p>This set of data is represented as a <em>type</em> and a set of strings,
 * that hold the actual data of this documentation.</p>
 *
 * <p>The type of the documentation data is a name-spaced string represented
 * through a {@link ResourceLocation} whereas the data is a {@link List},
 * allowing it to be indexed e.g. to identify a specific position in the
 * data stream.</p>
 *
 * <p>There are no restrictions about how the data is structured, as long as
 * the type can be fully represented as a string. It is then the job of a
 * {@link com.aaronhowser1.dymm.api.consume.DocumentationDataConsumer} to
 * deserialize back the data.</p>
 *
 * @since 2.0.0
 */
public interface DocumentationData {
    /**
     * Gets the type of this documentation data, represented as a
     * {@link ResourceLocation}.
     *
     * @return
     *      The type of this documentation data. Must not be {@code null}.
     *
     * @since 2.0.0
     */
    @Nonnull ResourceLocation getType();

    /**
     * Gets a {@link List} of data of the given {@link #getType()}, in the
     * string representation that is most suitable.
     *
     * @return
     *      The actual data of this {@link DocumentationData}. Must not be
     *      {@code null}.
     *
     * @since 2.0.0
     */
    @Nonnull List<String> getData();
}
