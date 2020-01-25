package com.aaronhowser1.dymm.common.loading;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.loading.DocumentationLoader;
import com.aaronhowser1.dymm.api.loading.metadata.MetadataListener;
import com.aaronhowser1.dymm.api.loading.metadata.MetadataListenerRegistry;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public enum LoaderRegistry implements MetadataListenerRegistry {
    INSTANCE;

    private static final L LOG = L.create(Constants.MOD_NAME, "Loader Registry");

    private final Map<ResourceLocation, DocumentationLoader> loaders = new HashMap<>();
    private final Map<String, List<MetadataListener>> listenersMap = new HashMap<>();

    public void discoverLoadersFromClasspath() {
        this.loaders.clear();
        LOG.info("Discovering loaders on the classpath");
        final ServiceLoader<DocumentationLoader> serviceLoader = ServiceLoader.load(DocumentationLoader.class);
        serviceLoader.iterator().forEachRemaining(it -> {
            final ResourceLocation id = it.getIdentifier();
            LOG.debug("Found loader '" + id + "' with implementation '" + it + "' @ " + it.getClass().getName());
            if (this.loaders.get(id) != null) {
                if (this.loaders.get(id) == it) {
                    LOG.bigWarn("The loader '" + id + "' has been found twice on the classpath, with the same implementation!\n" +
                            "While this is not an issue, it will not be registered again to prevent overrides.\n" +
                            "This most of the times indicates a broken mod installation or environment setup: please check it");
                    return;
                } else {
                    LOG.bigError("Two loaders with the same identifier '" + id + "' were found on the classpath\n" +
                            "Loader overriding is not supported and this attempt will be blocked immediately!\n" +
                            "The game loading process cannot continue!\n\nCurrent loader class: " + this.loaders.get(id).getClass().getName() + "\n" +
                            "Override-attempting loader class: " + it.getClass().getName());
                    throw new IllegalStateException("Found two loaders with the same id '" + id + "': game cannot continue loading");
                }
            }
            this.loaders.put(id, it);
            it.onLoad();
        });
        LOG.info("Loader discovery completed: found a total of " + this.loaders.size() + " loaders");
        LOG.debug("Dumping loaders: ");
        this.loaders.forEach((k, v) -> LOG.debug("    " + k + " -> " + v.getClass().getName()));
    }

    public void registerMetadataListeners() {
        this.listenersMap.clear();
        LOG.info("Attempting to discover and register metadata listeners for all loaders");
        this.loaders.values().forEach(it -> it.registerMetadataListeners(this));
        LOG.info("Metadata listener registration completed: a total of " + this.listenersMap.values().stream().mapToLong(Collection::size).sum() + " listeners were registered");
        LOG.debug("Dumping metadata listeners: ");
        this.listenersMap.forEach((k, v) -> {
            LOG.debug("    - " + k);
            v.forEach(it -> LOG.debug("        " + it.getClass().getName()));
        });
    }

    @Override
    public void register(@Nonnull final String metadataIdentifier, @Nonnull final MetadataListener listener) {
        Preconditions.checkNotNull(metadataIdentifier, "Unable to register a null metadata identifier");
        Preconditions.checkNotNull(listener, "Unable to register a null listener");
        Preconditions.checkArgument(!metadataIdentifier.isEmpty(), "Metadata identifier cannot be empty");
        this.listenersMap.computeIfAbsent(metadataIdentifier, it -> new ArrayList<>()).add(listener);
        LOG.debug("Registered " + metadataIdentifier + " -> " + listener);
    }

    @Nonnull
    @Override
    public List<MetadataListener> findAllFor(@Nonnull final String metadataIdentifier) {
        Preconditions.checkNotNull(metadataIdentifier, "Unable to register a null metadata identifier");
        Preconditions.checkArgument(!metadataIdentifier.isEmpty(), "Metadata identifier cannot be empty");
        return this.listenersMap.computeIfAbsent(metadataIdentifier, it -> new ArrayList<>());
    }

    void fireEvent() {
        this.loaders.values().forEach(DocumentationLoader::onGlobalLoadingStateUnbinding);
    }

    void fireEvent(@Nonnull final JsonObject object, @Nonnull final String identifier, @Nonnull final String namespace) {
        this.findAllFor(identifier).forEach(it -> it.processMetadata(object, namespace));
    }

    @Nullable
    DocumentationLoader findLoader(@Nonnull final ResourceLocation name) {
        return this.loaders.get(name);
    }
}
