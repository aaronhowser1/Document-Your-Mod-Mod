package com.aaronhowser1.documentmod.json;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.config.DYMMConfig;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public enum DocumentationRegistry {
    INSTANCE;

    private static IForgeRegistry<ModDocumentation> registry;

    public void setRegistry(@Nonnull final IForgeRegistry<ModDocumentation> attempt) throws IllegalAccessException {
        if (registry != null) throw new IllegalAccessException("Attempted to replace ModDocumentation registry!\nCurrent: " + registry + "\nAttempt: " + attempt);
        registry = attempt;
    }

    public IForgeRegistry<ModDocumentation> getRegistry() {
        return registry;
    }

    @Nonnull
    public List<ModDocumentation> getDocumentationForMod(@Nonnull final String modId) {
        return ImmutableList.copyOf(
                registry.getValuesCollection()
                        .stream()
                        .filter(it -> it.getRegistryName() != null && it.getRegistryName().getNamespace().equals(modId))
                        .collect(Collectors.toList())
        );
    }

    @Nonnull
    public List<ModDocumentation> getDocumentationForMod(@Nonnull final ModContainer modContainer) {
        return this.getDocumentationForMod(modContainer.getModId());
    }

    public void dump() {
        final Consumer<String> loggingMethod = DYMMConfig.debugModIsDocumented ? DocumentMod.logger::info : DocumentMod.logger::debug;
        loggingMethod.accept("Dumping registry: ");
        registry.getEntries().stream().map(it -> "    " + it.getKey() + " -> " + it.getValue()).forEach(loggingMethod);
    }
}
