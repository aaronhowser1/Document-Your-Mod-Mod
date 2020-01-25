package com.aaronhowser1.dymm.common.sort;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.api.documentation.Dependency;
import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.documentation.Target;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public final class SortingDocumentationEntry extends IForgeRegistryEntry.Impl<DocumentationEntry> implements DocumentationEntry {
    SortingDocumentationEntry(@Nonnull final String name) {
        this.setRegistryName(new ResourceLocation(Constants.MOD_ID, name));
    }

    @Nonnull
    @Override
    public Set<Target> getTargets() {
        return new HashSet<>();
    }

    @Nonnull
    @Override
    public Set<DocumentationData> getDocumentationData() {
        return new HashSet<>();
    }

    @Nonnull
    @Override
    public Set<Dependency> getDependencies() {
        return new HashSet<>();
    }
}
