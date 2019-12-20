package com.aaronhowser1.dymm.common.loading;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.documentation.Target;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class UndocumentedTargetsHandler {
    private static final L LOG = L.create(Constants.MOD_NAME, "Undocumented Targets");

    private UndocumentedTargetsHandler() {}

    public static void discoverAndLog() {
        final Configuration configuration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(Constants.CONFIGURATION_MAIN);
        final boolean shouldUseWarn = configuration.get(Constants.CONFIGURATION_MAIN_DEBUG_CATEGORY, "missing_entries", false).getBoolean();
        final Consumer<String> logFunction = shouldUseWarn? LOG::warn : LOG::debug;
        discoverAndLog(logFunction);
    }

    private static void discoverAndLog(@Nonnull final Consumer<String> logFun) {
        LOG.info("Discovering not documented targets");
        final Map<ResourceLocation, DocumentationEntry> documentedTargets = findDocumentedTargets();
        final Set<String> documentedNamespaces = documentedTargets.keySet().stream().map(ResourceLocation::getNamespace).collect(Collectors.toSet());
        final Map<String, List<ResourceLocation>> targetsForTargets = findAllPossibleTargetsFor();
        final Map<String, List<ResourceLocation>> missingTargets = mergeAndFind(documentedNamespaces, documentedTargets, targetsForTargets);
        logMissingTargets(missingTargets, logFun);
        LOG.info("Discovery complete");
    }

    @Nonnull
    private static Map<ResourceLocation, DocumentationEntry> findDocumentedTargets() {
        return ApiBindings.getMainApi()
                .getDocumentationRegistry()
                .getValuesCollection()
                .stream()
                .flatMap(it ->
                        it.getTargets()
                                .stream()
                                .map(Target::obtainTarget)
                                .map(ItemStack::getItem)
                                .map(Item::getRegistryName)
                                .map(target -> ImmutablePair.of(target, it))
                )
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    @Nonnull
    private static Map<String, List<ResourceLocation>> findAllPossibleTargetsFor() {
        return ForgeRegistries.ITEMS.getValuesCollection()
                .stream()
                .map(Item::getRegistryName)
                .peek(Objects::requireNonNull)
                .map(it -> ImmutablePair.of(it.getNamespace(), it))
                .collect(Collectors.groupingBy(Pair::getLeft))
                .entrySet()
                .stream()
                .map(it ->
                        ImmutablePair.of(it.getKey(), it.getValue()
                                .stream()
                                .map(Pair::getValue)
                                .collect(Collectors.toList()))
                )
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    @Nonnull
    private static Map<String, List<ResourceLocation>> mergeAndFind(@Nonnull final Set<String> documentedNamespaces,
                                                                    @Nonnull final Map<ResourceLocation, DocumentationEntry> documentedTargets,
                                                                    @Nonnull final Map<String, List<ResourceLocation>> targetsForTargets) {
        return targetsForTargets
                .entrySet()
                .stream()
                .filter(it -> documentedNamespaces.contains(it.getKey()))
                .flatMap(it -> it.getValue().stream())
                .filter(it -> !documentedTargets.containsKey(it))
                .collect(Collectors.groupingBy(ResourceLocation::getNamespace));
    }

    private static void logMissingTargets(@Nonnull final Map<String, List<ResourceLocation>> missingTargets, @Nonnull final Consumer<String> logFun) {
        logMissingTargets(
                missingTargets.entrySet()
                        .stream()
                        .flatMap(it -> it.getValue().stream().map(target -> ImmutablePair.of(it.getKey(), target)))
                        .collect(Collectors.toList()),
                logFun
        );
    }

    private static void logMissingTargets(@Nonnull final List<Pair<String, ResourceLocation>> missingTargets, @Nonnull final Consumer<String> logFun) {
        missingTargets.stream()
                .map(it -> "Found undocumented target '" + it.getRight() + "' for namespace '" + it.getLeft() + "'")
                .forEach(logFun);
    }
}
