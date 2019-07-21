package com.aaronhowser1.documentmod.json;

import com.aaronhowser1.documentmod.DocumentMod;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public enum DocumentationRegistry {
    INSTANCE;

    private static final Map<String, List<ModDocumentation>> REGISTRY = Maps.newHashMap();

    public void registerForMod(@Nonnull final String modId, @Nonnull final ModDocumentation modDocumentation) {
        final ResourceLocation resourceLocation = modDocumentation.getRegistryName();
        if (!modId.equals(resourceLocation.getNamespace())) {
            DocumentMod.logger.warn("Attempted to register mod documentation for mod ID " + modId + ", but mod documentation reports mod ID " + resourceLocation.getNamespace());
            DocumentMod.logger.warn("This may be intended, but it is usually the result of an error! Please check your code");
        }
        final List<ModDocumentation> mod = REGISTRY.computeIfAbsent(modId, s -> Lists.newArrayList());
        final Optional<ModDocumentation> previous = mod.stream().filter(i -> i.getRegistryName().equals(modDocumentation.getRegistryName())).findFirst();
        if (previous.isPresent()) {
            DocumentMod.logger.warn("Attempted to register mod documentation with registry name " + resourceLocation + " but it exists already");
            DocumentMod.logger.warn("This may be an intended override but it usually the result of an error!");
            DocumentMod.logger.warn("The entry will now be replaced: was " + previous + ", will be " + modDocumentation);
            mod.remove(previous.get());
        }
        mod.add(modDocumentation);
    }

    @Nonnull
    public List<ModDocumentation> getDocumentationForMod(@Nonnull final String modId) {
        return ImmutableList.copyOf(REGISTRY.getOrDefault(modId, Lists.newArrayList()));
    }

    @Nonnull
    public List<ModDocumentation> getDocumentationForMod(@Nonnull final ModContainer modContainer) {
        return this.getDocumentationForMod(modContainer.getModId());
    }

    void dump() {
        DocumentMod.logger.debug("Dumping registry: ");
        REGISTRY.values().forEach(l -> l.forEach(e -> DocumentMod.logger.debug("    " + e.getRegistryName() + " -> " + e)));
    }

    void wipe() {
        REGISTRY.clear();
        DocumentMod.logger.info("Documentation registry wiped");
    }
}
