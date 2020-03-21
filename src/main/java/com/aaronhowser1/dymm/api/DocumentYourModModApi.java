package com.aaronhowser1.dymm.api;

import com.aaronhowser1.dymm.api.configuration.ConfigurationManager;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents the main interface between API and implementation, allowing
 * clients to interface with the implementation without a hard dependency on
 * it.
 *
 * @implNote
 *      Clients are not expected to implement this interface, rather use the
 *      instance that is defined through {@link ApiBindings#getMainApi()}.
 *
 * @since 2.0.0
 */
public interface DocumentYourModModApi {
    /**
     * Gets the {@link ConfigurationManager} that has been loaded by the
     * current implementation binding.
     *
     * @return
     *      The {@link ConfigurationManager} loaded by the implementation.
     *      Guaranteed not to be {@code null}.
     *
     * @see ConfigurationManager
     * @since 2.0.0
     */
    @Nonnull ConfigurationManager getConfigurationManager();

    /**
     * Gets the current {@link GlobalLoadingState} as defined by the
     * implementation.
     *
     * <p>This value can be {@code null} in case the implementation hasn't
     * begun the loading process as described in the documentation for
     * {@code GlobalLoadingState} itself.</p>
     *
     * @return
     *      The {@link GlobalLoadingState} defined by the implementation.
     *      Could be {@code null}.
     *
     * @see GlobalLoadingState
     * @since 2.0.0
     */
    @Nullable GlobalLoadingState getCurrentLoadingState();

    /**
     * Gets the {@link IForgeRegistry} that is responsible for holding all
     * the {@linkplain DocumentationEntry documentation entries} together.
     *
     * @return
     *      The {@link IForgeRegistry} that holds the documentation entries.
     *      Guaranteed not to be {@code null}.
     *
     * @since 2.0.0
     */
    @Nonnull IForgeRegistry<DocumentationEntry> getDocumentationRegistry();
}
