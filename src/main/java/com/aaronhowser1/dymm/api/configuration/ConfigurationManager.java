package com.aaronhowser1.dymm.api.configuration;

import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;

/**
 * Handles the loading and the retrieval of the {@link Configuration} objects
 * that the implementation or entries may specify.
 *
 * @since 2.0.0
 */
public interface ConfigurationManager {
    /**
     * Obtains a {@link Configuration} with the specified {@code name} and
     * {@code version}.
     *
     * <p>The {@code Configuration} object is guaranteed to exist and to be
     * saved and loaded as needed. The client shouldn't need to care about the
     * internal mechanics of the saving and loading mechanism.</p>
     *
     * <p>Implementations are free to create configurations on-the-fly or cache
     * them in case of performance concerns. By contract, though, every change
     * made to the {@code Configuration} object must persist across restarts of
     * the game.</p>
     *
     * @param name
     *      The name of the configuration to load: must be unique and
     *      non-{@code null}.
     * @param version
     *      The version of the configuration to load: must not be {@code null}.
     *      Clients should use a version string that respects the specifics of
     *      Semantic Versioning, but no checks are performed in this regard
     *      by the implementation.
     * @return
     *      A {@link Configuration} object that can be freely manipulated. The
     *      configuration object may have been created on-the-fly or loaded
     *      from disk. It is anyway guaranteed not to be {@code null} and to
     *      persist changes made to it.
     *
     * @since 2.0.0
     */
    @Nonnull Configuration getVersionedConfigurationFor(@Nonnull final String name, @Nonnull final String version);

    /**
     * Obtains a {@link Configuration} with the specified {@code name}.
     *
     * <p>The {@code Configuration} object is guaranteed to exist and to be
     * saved and loaded as needed. The client shouldn't need to care about the
     * internal mechanics of the saving and loading mechanism.</p>
     *
     * <p>Implementations are free to create configurations on-the-fly or cache
     * them in case of performance concerns. By contract, though, every change
     * made to the {@code Configuration} object must persist across restarts of
     * the game.</p>
     *
     * <p>Differently from
     * {@link #getVersionedConfigurationFor(String, String)}, the returned
     * configuration is version-agnostic. Nevertheless, implementations can
     * use a versioned config file with a default version. Clients should not
     * assume either behavior to occur.</p>
     *
     * @param name
     *      The name of the configuration to load: must be unique and
     *      non-{@code null}.
     * @return
     *      A {@link Configuration} object that can be freely manipulated. The
     *      configuration object may have been created on-the-fly or loaded
     *      from disk. It is anyway guaranteed not to be {@code null} and to
     *      persist changes made to it.
     *
     * @since 2.0.0
     */
    @Nonnull Configuration getConfigurationFor(@Nonnull final String name);
}
