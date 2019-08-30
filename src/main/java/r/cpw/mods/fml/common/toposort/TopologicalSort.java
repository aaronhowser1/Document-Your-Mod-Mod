/*
 * Minecraft Forge
 * Copyright (c) 2016-2018.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package r.cpw.mods.fml.common.toposort;

import com.aaronhowser1.documentmod.DocumentMod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Topological sort for mod loading
 *
 * Based on a variety of sources, including http://keithschwarz.com/interesting/code/?dir=topological-sort
 * @author cpw (edited by Silk)
 *
 */
final class TopologicalSort
{
    static class DirectedGraph<T> implements Iterable<T>
    {
        private final Map<T, SortedSet<T>> graph = Maps.newHashMap();
        private final List<T> orderedNodes = Lists.newArrayList();

        @SuppressWarnings("UnusedReturnValue")
        boolean addNode(@Nonnull final T node) {
            // Ignore nodes already added
            if (this.graph.containsKey(node)) {
                return false;
            }

            this.orderedNodes.add(node);
            this.graph.put(node, new TreeSet<>(Comparator.comparingInt(this.orderedNodes::indexOf)));
            return true;
        }

        void addEdge(@Nonnull final T from, @Nonnull final T to)
        {
            this.checkNodes(from, to);
            this.graph.get(from).add(to);
        }

        @SuppressWarnings("unused")
        void removeEdge(@Nonnull final T from, @Nonnull final T to)
        {
            this.checkNodes(from, to);
            this.graph.get(from).remove(to);
        }

        @SuppressWarnings("unused")
        boolean edgeExists(@Nonnull final T from, @Nonnull final T to)
        {
            this.checkNodes(from, to);
            return this.graph.get(from).contains(to);
        }

        @Nonnull
        Set<T> edgesFrom(@Nonnull final T from)
        {
            if (!this.graph.containsKey(from)) {
                throw new NoSuchElementException("Missing node from graph");
            }

            return Collections.unmodifiableSortedSet(this.graph.get(from));
        }

        @Nonnull
        @Override
        public Iterator<T> iterator()
        {
            return this.orderedNodes.iterator();
        }

        @SuppressWarnings("unused")
        int size()
        {
            return this.graph.size();
        }

        @SuppressWarnings("unused")
        boolean isEmpty()
        {
            return this.graph.isEmpty();
        }

        @Override
        public String toString()
        {
            return this.graph.toString();
        }

        private void checkNodes(@Nonnull T from, @Nonnull T to) {
            if (!(this.graph.containsKey(from) && this.graph.containsKey(to))) {
                throw new NoSuchElementException("Missing nodes from graph");
            }
        }
    }

    private TopologicalSort() {}

    /**
     * Sort the input graph into a topologically sorted list
     *
     * Uses the reverse depth first search as outlined in ...
     * @param graph The graph
     * @return The sorted documentation list.
     */
    @Nonnull
    static <T> List<T> topologicalSort(@Nonnull final DirectedGraph<T> graph) {
        final DirectedGraph<T> rGraph = reverse(graph);
        final List<T> sortedResult = Lists.newArrayList();
        final Set<T> visitedNodes = Sets.newHashSet();
        // A list of "fully explored" nodes. Leftovers in here indicate cycles in the graph
        final Set<T> expandedNodes = Sets.newHashSet();

        rGraph.forEach(it -> explore(it, rGraph, sortedResult, visitedNodes, expandedNodes));

        return sortedResult;
    }

    @Nonnull
    private static <T> DirectedGraph<T> reverse(@Nonnull final DirectedGraph<T> graph) {
        final DirectedGraph<T> result = new DirectedGraph<>();
        graph.forEach(result::addNode);
        graph.forEach(from -> graph.edgesFrom(from).forEach(to -> result.addEdge(to, from)));
        return result;
    }

    private static <T> void explore(@Nonnull final T node, @Nonnull final DirectedGraph<T> graph,
                                    @Nonnull final List<T> sortedResult, @Nonnull final Set<T> visitedNodes,
                                    @Nonnull final Set<T> expandedNodes) {
        // Have we been here before?
        if (visitedNodes.contains(node))
        {
            // And have completed this node before
            if (expandedNodes.contains(node))
            {
                // Then we're fine
                return;
            }

            DocumentMod.logger.fatal("Documentation Sorting failed.");
            DocumentMod.logger.fatal("Visiting node " + node);
            DocumentMod.logger.fatal("Current sorted list: " + sortedResult);
            DocumentMod.logger.fatal("Visited set for this node: " + visitedNodes);
            DocumentMod.logger.fatal("Explored node set: " + expandedNodes);
            final Sets.SetView<T> cycleList = Sets.difference(visitedNodes, expandedNodes);
            DocumentMod.logger.fatal("Likely cycle is in: " + cycleList);
            throw new DocumentationSortingException("There was a cycle detected in the input graph, sorting is not possible", node, cycleList);
        }

        // Visit this node
        visitedNodes.add(node);

        // Recursively explore inbound edges
        graph.edgesFrom(node).forEach(it -> explore(it, graph, sortedResult, visitedNodes, expandedNodes));

        // Add ourselves now
        sortedResult.add(node);
        // And mark ourselves as explored
        expandedNodes.add(node);
    }
}