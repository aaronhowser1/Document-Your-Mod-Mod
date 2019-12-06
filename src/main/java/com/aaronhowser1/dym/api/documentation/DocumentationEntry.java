package com.aaronhowser1.dym.api.documentation;

import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Set;

public interface DocumentationEntry extends IForgeRegistryEntry<DocumentationEntry> {
    @Nonnull Set<Target> getTargets();
    @Nonnull Set<DocumentationData> getDocumentationData();
    @Nonnull Set<Dependency> getDependencies();
}
