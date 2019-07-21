package com.aaronhowser1.documentmod.json;

import com.aaronhowser1.documentmod.DocumentMod;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public enum DocumentationRegistry {
    INSTANCE;

    private static final Map<String, List<ModDocumentation>> REGISTRY = Maps.newHashMap();

    public void registerForMod(@Nonnull final String modId, @Nonnull final ModDocumentation modDocumentation) {
        REGISTRY.computeIfAbsent(modId, s -> Lists.newArrayList()).add(modDocumentation);
    }

    @Nonnull
    public List<ModDocumentation> getDocumentationForMod(@Nonnull final String modId) {
        return ImmutableList.copyOf(REGISTRY.getOrDefault(modId, Lists.newArrayList()));
    }

    @Nonnull
    public List<ModDocumentation> getDocumentationForMod(@Nonnull final ModContainer modContainer) {
        return this.getDocumentationForMod(modContainer.getModId());
    }

    void wipe() {
        REGISTRY.clear();
        DocumentMod.logger.info("Documentation registry wiped");
    }
}
