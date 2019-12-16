package com.aaronhowser1.dymm.common.consume;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public final class DocumentationConsumerDispatcher {
    private static final L LOG = L.create(Constants.MOD_NAME, "Consumer Dispatcher");

    private static IForgeRegistry<DocumentationEntry> registry;

    private DocumentationConsumerDispatcher() {}

    public static void bind(@Nonnull final IForgeRegistry<DocumentationEntry> data) {
        LOG.info("Binding documentation registry");
        registry = data;
        LOG.info("Registry successfully bound: scheduling unbinding");
    }

    public static void dispatch() {
        LOG.info("Beginning dispatch of documentation data to listeners");
        // TODO Perform sorting here: the registry doesn't care about it, but we do care when firing events
        registry.forEach(ConsumerRegistry.INSTANCE::fireAllFor);
    }

    public static void unbind() {
        LOG.info("Binding to registry removed");
        registry = null;
    }
}
