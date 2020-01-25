package com.aaronhowser1.dymm.common.sort;

import com.aaronhowser1.dymm.api.documentation.Dependency;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public final class DocumentationSorter {
    private final Map<ResourceLocation, DocumentationEntry> entries;

    public DocumentationSorter(@Nonnull final Set<Map.Entry<ResourceLocation, DocumentationEntry>> entries) {
        this.entries = entries.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Nonnull
    public List<DocumentationEntry> sort() {
        final DirectedGraph<DocumentationEntry> graph = new DirectedGraph<>();

        final DocumentationEntry beforeAll = new SortingDocumentationEntry("before_all");
        final DocumentationEntry afterAll = new SortingDocumentationEntry("after_all");

        graph.addNode(beforeAll);
        graph.addNode(afterAll);

        final Map<String, Pair<DocumentationEntry, DocumentationEntry>> targetEntries = this.buildTargetEntries();
        targetEntries.values().forEach(it -> {
            graph.addNode(it.getLeft());
            graph.addNode(it.getRight());
            graph.addEdge(beforeAll, it.getLeft());
            graph.addEdge(it.getLeft(), it.getRight());
            graph.addEdge(it.getRight(), afterAll);
        });

        this.entries.values().forEach(graph::addNode);

        this.entries.values()
                .forEach(entry -> {
                    final AtomicBoolean preAdded = new AtomicBoolean(false);
                    final AtomicBoolean postAdded = new AtomicBoolean(false);

                    entry.getDependencies().stream()
                            .filter(it -> it.getOrdering() == Dependency.Ordering.AFTER)
                            .forEach(it -> {
                                preAdded.set(true);

                                final ResourceLocation target = it.getTarget();
                                final String targetNamespace = target.getNamespace();

                                graph.addEdge(targetEntries.get(targetNamespace).getLeft(), entry);

                                final DocumentationEntry targetEntry = this.entries.get(target);

                                if (targetEntry != null) {
                                    graph.addEdge(this.entries.get(target), entry);
                                } else if (it.getRequirement() == Dependency.Requirement.REQUIRED) {
                                    throw new UnsatisfiedDependencyException("Entry '" + entry.getRegistryName() +
                                            "' is specifying a required dependency on '" + target + "', but that entry does not exist");
                                }
                            });

                    entry.getDependencies().stream()
                            .filter(it -> it.getOrdering() == Dependency.Ordering.BEFORE)
                            .forEach(it -> {
                                postAdded.set(true);

                                final ResourceLocation target = it.getTarget();
                                final String targetNamespace = target.getNamespace();

                                graph.addEdge(entry, targetEntries.get(targetNamespace).getRight());

                                final DocumentationEntry targetEntry = this.entries.get(target);

                                if (targetEntry != null) {
                                    graph.addEdge(entry, this.entries.get(target));
                                } else if (it.getRequirement() == Dependency.Requirement.REQUIRED) {
                                    throw new UnsatisfiedDependencyException("Entry '" + entry.getRegistryName() +
                                            "' is specifying a required dependency on '" + target + "', but that entry does not exist");
                                }
                            });

                    final ResourceLocation registryName = Objects.requireNonNull(entry.getRegistryName());
                    final String modNamespace = registryName.getNamespace();

                    if (!preAdded.get()) {
                        graph.addEdge(targetEntries.get(modNamespace).getKey(), entry);
                    }

                    if (!postAdded.get()) {
                        graph.addEdge(entry, targetEntries.get(modNamespace).getValue());
                    }
                });

        final List<DocumentationEntry> sortedData = TopologicalSort.INSTANCE.sort(graph);
        sortedData.remove(beforeAll);
        sortedData.remove(afterAll);
        targetEntries.values().forEach(it -> {
            sortedData.remove(it.getLeft());
            sortedData.remove(it.getRight());
        });
        return sortedData;
    }

    @Nonnull
    private Map<String, Pair<DocumentationEntry, DocumentationEntry>> buildTargetEntries() {
        final Map<String, Pair<DocumentationEntry, DocumentationEntry>> targetEntries = new HashMap<>();

        Loader.instance()
                .getModList()
                .stream()
                .map(ModContainer::getModId)
                .map(it -> ImmutablePair.of(it,
                        ImmutablePair.of(
                                (DocumentationEntry) new SortingDocumentationEntry("before_" + it),
                                (DocumentationEntry) new SortingDocumentationEntry("after_" + it)
                        ))
                )
                .forEach(it -> targetEntries.put(it.getLeft(), it.getRight()));

        return targetEntries;
    }
}
