package com.aaronhowser1.dymm.common.sort;

import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum TopologicalSort {
    INSTANCE;

    @Nonnull
    public List<DocumentationEntry> sort(@Nonnull final DirectedGraph<DocumentationEntry> data) {
        return this.doSort(data);
    }

    @Nonnull
    private <T> List<T> doSort(@Nonnull final DirectedGraph<T> data) {
        final DirectedGraph<T> reversed = this.reverse(data);
        final List<T> sortedResult = new ArrayList<>();
        final Set<T> visitedNodes = new HashSet<>();
        final Set<T> expandedNodes = new HashSet<>();

        reversed.forEach(it -> this.explore(it, reversed, sortedResult, visitedNodes, expandedNodes));

        return sortedResult;
    }

    @Nonnull
    private <T> DirectedGraph<T> reverse(@Nonnull final DirectedGraph<T> graph) {
        final DirectedGraph<T> result = new DirectedGraph<>();
        graph.forEach(result::addNode);
        graph.forEach(from -> graph.edgesFrom(from).forEach(to -> result.addEdge(to, from)));
        return result;
    }

    private <T> void explore(@Nonnull final T node, @Nonnull final DirectedGraph<T> graph,
                             @Nonnull final List<T> sortedResult, @Nonnull final Set<T> visitedNodes,
                             @Nonnull final Set<T> expandedNodes) {
        if (visitedNodes.contains(node)) {
            if (expandedNodes.contains(node)) {
                return;
            }

            throw new CyclingDependencyException("A cycle was identified in the dependency tree: unable to sort.\n" +
                    "The currently visiting node was '" + node + "'\n" +
                    "The currently sorted list is " + sortedResult + "\n" +
                    "The visited set for this node is " + visitedNodes + "\n" +
                    "The currently explored node set is " + expandedNodes + "\n" +
                    "Cycle may be in " + Sets.difference(visitedNodes, expandedNodes));
        }

        visitedNodes.add(node);
        graph.edgesFrom(node).forEach(it -> this.explore(it, graph, sortedResult, visitedNodes, expandedNodes));
        sortedResult.add(node);
        expandedNodes.add(node);
    }
}
