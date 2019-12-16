package com.aaronhowser1.dymm.common.consume;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.consume.DocumentationDataConsumer;
import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.documentation.Target;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;

public enum ConsumerRegistry {
    INSTANCE;

    private static final L LOG = L.create(Constants.MOD_NAME, "Consumer Registry");

    private final Map<ResourceLocation, List<DocumentationDataConsumer>> consumers = new HashMap<>();

    public void discoverConsumersFromClasspath() {
        this.consumers.clear();
        LOG.info("Discovering consumers on the classpath");
        final ServiceLoader<DocumentationDataConsumer> serviceLoader = ServiceLoader.load(DocumentationDataConsumer.class);
        serviceLoader.iterator().forEachRemaining(it -> {
            final List<ResourceLocation> compatibleTypes = it.getCompatibleTypes();
            LOG.debug("Found consumer for types " + compatibleTypes + " with implementation '" + it + "' @ " + it.getClass().getName());
            compatibleTypes.forEach(type -> this.consumers.computeIfAbsent(type, key -> new ArrayList<>()).add(it));
            it.onCreation();
        });
        LOG.info("Consumer discovery completed: found a total of " + this.consumers.values().stream().mapToLong(Collection::size).sum() + " consumers");
        LOG.debug("Dumping consumers: ");
        this.consumers.forEach((k, v) -> {
            LOG.debug("    - " + k);
            v.forEach(it -> LOG.debug("        " + it.getClass().getName()));
        });
    }

    void fireAllFor(@Nonnull final DocumentationEntry entry) {
        final Set<Target> targets = new HashSet<>(entry.getTargets());
        entry.getDocumentationData().forEach(it -> this.fireEvent(it.getType(), Objects.requireNonNull(entry.getRegistryName()), it, targets));
    }

    private void fireEvent(@Nonnull final ResourceLocation type, @Nonnull final ResourceLocation id, @Nonnull final DocumentationData data, @Nonnull final Set<Target> targets) {
        if (!this.consumers.containsKey(type)) {
            this.signalMissingReceiver(type, id);
        }
        this.consumers.get(type).forEach(it -> it.consumeData(data, targets));
    }

    private void signalMissingReceiver(@Nonnull final ResourceLocation type, @Nonnull final ResourceLocation id) {
        LOG.warn("Documentation entry '" + id + "' specifies a documentation data of type '" + type + "' but no compatible receiver was found: events will not fire!");
    }
}
