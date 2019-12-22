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

public final class TargetsHandler {
    private static final L DOCUMENTED_LOG = L.create(Constants.MOD_NAME, "Documented Targets");
    private static final L UNDOCUMENTED_LOG = L.create(Constants.MOD_NAME, "Undocumented Targets");

    private TargetsHandler() {}

    public static void discoverAndLog() {
        discoverAndLogDocumented();
        discoverAndLogUndocumented();
    }

    private static void discoverAndLogDocumented() {
        final Configuration configuration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(Constants.ConfigurationMain.NAME);
        final boolean shouldPrint = configuration.getBoolean(Constants.ConfigurationMain.PROPERTY_DEBUG_TARGET_DOCUMENTED, Constants.ConfigurationMain.CATEGORY_DEBUG,
                false, Constants.ConfigurationMain.PROPERTY_DEBUG_TARGET_DOCUMENTED_COMMENT);
        if (!shouldPrint) return;
        discoverAndLogDocumented(DOCUMENTED_LOG::info);
    }

    private static void discoverAndLogDocumented(@Nonnull final Consumer<String> consumer) {
        ApiBindings.getMainApi()
                .getDocumentationRegistry()
                .getValuesCollection()
                .stream()
                .map(DocumentationEntry::getRegistryName)
                .peek(Objects::requireNonNull)
                .map(ResourceLocation::getNamespace)
                .distinct()
                .map(it -> "Target '" + it + "' has been documented")
                .forEach(consumer);
    }

    private static void discoverAndLogUndocumented() {
        final Configuration configuration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(Constants.ConfigurationMain.NAME);
        final boolean shouldUseWarn = configuration.getBoolean(Constants.ConfigurationMain.PROPERTY_DEBUG_MISSING_ENTRIES, Constants.ConfigurationMain.CATEGORY_DEBUG,
                false, Constants.ConfigurationMain.PROPERTY_DEBUG_MISSING_ENTRIES_COMMENT);
        final Consumer<String> logFunction = shouldUseWarn? UNDOCUMENTED_LOG::warn : UNDOCUMENTED_LOG::debug;
        discoverAndLogUndocumented(logFunction);
    }

    private static void discoverAndLogUndocumented(@Nonnull final Consumer<String> logFun) {
        UNDOCUMENTED_LOG.info("Discovering not documented targets");
        final Map<ResourceLocation, DocumentationEntry> documentedTargets = findDocumentedTargets();
        final Set<String> documentedNamespaces = documentedTargets.keySet().stream().map(ResourceLocation::getNamespace).collect(Collectors.toSet());
        final Map<String, List<ResourceLocation>> targetsForTargets = findAllPossibleTargetsFor();
        final Map<String, List<ResourceLocation>> missingTargets = mergeAndFind(documentedNamespaces, documentedTargets, targetsForTargets);
        logMissingTargets(missingTargets, logFun);
        UNDOCUMENTED_LOG.info("Discovery complete");
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
