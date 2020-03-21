package com.aaronhowser1.dymm.api.documentation;

import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Represents a set of data that is documented, along with the necessary
 * documentation and dependencies on other entries.
 *
 * <p>Each entry must be considered a singleton, meaning that you can't create
 * multiple entries that all have the same properties, since they will be
 * considered as two completely different entries.</p>
 *
 * @see IForgeRegistryEntry
 * @since 2.0.0
 */
public interface DocumentationEntry extends IForgeRegistryEntry<DocumentationEntry> {
    /**
     * Gets a {@link Set} of {@link Target}s for this entry.
     *
     * <p>The returned {@code Set} can be empty if this entry does not specify
     * {@code Target}s.</p>
     *
     * @return
     *      A {@link Set} of {@link Target}s for this entry. Must not be
     *      {@code null}. Can be empty.
     *
     * @see Target
     * @since 2.0.0
     */
    @Nonnull Set<Target> getTargets();

    /**
     * Gets a {@link Set} of {@link DocumentationData} for this entry.
     *
     * <p>The returned {@code Set} can be empty if this entry does not specify
     * {@code DocumentationData}.</p>
     *
     * @return
     *      A {@link Set} of {@link DocumentationData} for this entry. Must not
     *      be {@code null}. Can be empty.
     *
     * @see DocumentationData
     * @since 2.0.0
     */
    @Nonnull Set<DocumentationData> getDocumentationData();

    /**
     * Gets a {@link Set} of {@link Dependency} objects for this entry.
     *
     * <p>The returned {@code Set} can be empty if this entry does not specify
     * dependencies.</p>
     *
     * @return
     *      A {@link Set} of {@link Dependency} objects for this entry. Must
     *      not be {@code null}. Can be empty.
     *
     * @see Dependency
     * @since 2.0.0
     */
    @Nonnull Set<Dependency> getDependencies();
}
