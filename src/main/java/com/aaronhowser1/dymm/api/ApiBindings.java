package com.aaronhowser1.dymm.api;

import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.configuration.ConfigurationManager;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * Main entry point for the API, responsible for binding it to the
 * implementation.
 *
 * @apiNote
 *      While this is to be considered public API at all effects, it is
 *      suggested for clients not to depend on this class but rather use
 *      alternatives provided by the various implementations. This will avoid
 *      potential API mismatches between versions.
 *
 * @since 2.0.0
 */
public final class ApiBindings {
    private static final class MainApiBindingHolder {
        private static final DocumentYourModModApi API = loadThroughService(DocumentYourModModApi.class, () -> new DocumentYourModModApi() {
            @Nonnull
            @Override
            public ConfigurationManager getConfigurationManager() {
                return new ConfigurationManager() {
                    @Nonnull
                    @Override
                    public Configuration getVersionedConfigurationFor(@Nonnull final String name, @Nonnull final String version) {
                        return new Configuration(new File(name), version);
                    }

                    @Nonnull
                    @Override
                    public Configuration getConfigurationFor(@Nonnull final String name) {
                        return this.getVersionedConfigurationFor(name, "0");
                    }
                };
            }

            @Nullable
            @Override
            public GlobalLoadingState getCurrentLoadingState() {
                return null;
            }

            @Nonnull
            @Override
            public IForgeRegistry<DocumentationEntry> getDocumentationRegistry() {
                throw new IllegalStateException("Registry wasn't created yet");
            }
        });

        private MainApiBindingHolder() {}
    }

    private static final L LOG = L.create("DYMM API", "Api Bindings");

    private ApiBindings() {}

    /**
     * Gets the implementation of the main API entry points, represented by
     * an instance of {@link DocumentYourModModApi}.
     *
     * @return
     *      The implementation for this API, guaranteed not to be {@code null}.
     *
     * @since 2.0.0
     */
    @Nonnull
    public static DocumentYourModModApi getMainApi() {
        return MainApiBindingHolder.API;
    }

    @Nonnull
    @SuppressWarnings("SameParameterValue")
    private static <T> T loadThroughService(@Nonnull final Class<T> apiClass, @Nonnull final Supplier<T> defaultObjectCreator) {
        final ServiceLoader<T> loader = ServiceLoader.load(apiClass);
        final Iterator<T> foundImplementations = loader.iterator();

        T targetApi = null;
        while (foundImplementations.hasNext()) {
            final T next = foundImplementations.next();

            if (targetApi == null) {
                targetApi = next;
                continue;
            }
            if (next.getClass().getName().contains("aaronhowser1.dym.common") && !targetApi.getClass().getName().contains("aaronhowser1.dym.common")) {
                LOG.warn("Another implementation for the API class " + apiClass.getName() + " was found, but the official one is available on the classpath");
                LOG.warn("The old implementation (" + targetApi.getClass().getName() + ") will be replaced with our own");
                targetApi = next;
            }
        }

        if (targetApi == null) {
            LOG.bigError("No API Binding found for class " + apiClass.getName() + ": replacing with a dummy implementation!\nNote that this will cause errors later on!");
            targetApi = Objects.requireNonNull(defaultObjectCreator.get());
        }

        LOG.info("Using " + targetApi.getClass().getName() + " as binding for API class " + apiClass.getName());
        return targetApi;
    }
}
