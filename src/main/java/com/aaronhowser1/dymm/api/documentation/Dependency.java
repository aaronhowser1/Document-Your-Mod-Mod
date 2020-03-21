package com.aaronhowser1.dymm.api.documentation;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Represents a {@link Dependency} of a {@link DocumentationEntry} on another
 * entry identified via a given {@linkplain #getTarget() name},
 * {@linkplain #getOrdering() ordering}, and
 * {@linkplain #getRequirement() requirement}.
 *
 * <p>Each dependency implementation must be immutable for the whole lifetime
 * of the dependency itself, and it can only point to a single
 * {@link DocumentationEntry} other than the one it is bound to.</p>
 *
 * @since 2.0.0
 */
public interface Dependency {
    /**
     * Represents the order in which the bound {@link DocumentationEntry}
     * should load in comparison to the one targeted by this {@link Dependency}
     * instance.
     *
     * @since 2.0.0
     */
    enum Ordering {
        /**
         * Indicates that the entry must be loaded after the one specified by
         * the dependency.
         *
         * @since 2.0.0
         */
        AFTER,
        /**
         * Indicates that the entry must be loaded before the one specified by
         * the dependency.
         *
         * @since 2.0.0
         */
        BEFORE,
        /**
         * Indicates that the loading order between the two dependencies does
         * not matter.
         *
         * <p>Implementations are free to specify additional constraints on
         * when {@code EITHER} can be used as a {@link Ordering} for a
         * {@link Dependency}, e.g. by requiring the {@link Requirement} to be
         * {@link Requirement#REQUIRED}.</p>
         *
         * @since 2.0.0
         */
        EITHER
    }

    /**
     * Represents whether the {@link DocumentationEntry} bound to this
     * {@link Dependency} requires the entry targeted by the dep to be present
     * or not to guarantee successful loading.
     *
     * @since 2.0.0
     */
    enum Requirement {
        /**
         * Indicates that the entry targeted by this {@link Dependency} must
         * be loaded in order for the entry bound to this {@code Dependency} to
         * load.
         *
         * <p>Implementations are free to report errors in case this kind of
         * dependency isn't satisfied.</p>
         *
         * @since 2.0.0
         */
        REQUIRED,
        /**
         * Indicates that the entry targeted by this {@link Dependency} can
         * either exist or not and this shouldn't impact the entry bound to
         * this {@code Dependency}.
         *
         * <p>Implementations are free to specify additional constraints on
         * when {@code OPTIONAL} can be used as a {@link Requirement} for a
         * {@code Dependency}, e.g. by requiring the {@link Ordering} not to be
         * {@link Ordering#EITHER}.</p>
         *
         * @since 2.0.0
         */
        OPTIONAL
    }

    /**
     * Gets the {@link Ordering} represented by this {@link Dependency}.
     *
     * @return
     *      The {@link Ordering}, Guaranteed not to be {@code null}.
     *
     * @see Ordering
     * @since 2.0.0
     */
    @Nonnull Ordering getOrdering();

    /**
     * Gets the {@link Requirement} represented by this {@link Dependency}.
     *
     * @return
     *      The {@link Requirement}, Guaranteed not to be {@code null}.
     *
     * @see Requirement
     * @since 2.0.0
     */
    @Nonnull Requirement getRequirement();

    /**
     * Gets the name of the target that this {@link Dependency} identifies.
     *
     * <p>In other words, the {@link ResourceLocation} returned by this method
     * represents the name of the {@link DocumentationEntry} that the entry
     * bound to this {@code Dependency} instance depends on.</p>
     *
     * <p>The target name can represent an existing entry or a missing one.
     * Behavior is then dependent on the {@link Requirement} and
     * {@link Ordering} that are specified by the {@code Dependency}.</p>
     *
     * @return
     *      The name of the target {@link DocumentationEntry} that is
     *      identified by this {@link Dependency}.
     *
     * @since 2.0.0
     */
    @Nonnull ResourceLocation getTarget();
}
