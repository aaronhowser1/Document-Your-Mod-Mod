package com.aaronhowser1.dymm.common.sort;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public final class DirectedGraph<T> implements Iterable<T> {
    private final Map<T, SortedSet<T>> graph = new LinkedHashMap<>();
    private final List<T> orderedNodes = new ArrayList<>();

    void addNode(@Nonnull final T node) {
        if (this.graph.containsKey(node)) return;
        this.orderedNodes.add(node);
        this.graph.put(node, new TreeSet<>(Comparator.comparingInt(this.orderedNodes::indexOf)));
    }

    void addEdge(@Nonnull final T from, @Nonnull final T to) {
        this.checkNodes(from, to);
        this.graph.get(from).add(to);
    }

    @Nonnull
    Set<T> edgesFrom(@Nonnull final T from) {
        if (!this.graph.containsKey(from)) throw new NoSuchElementException("Missing node from graph");
        return Collections.unmodifiableSortedSet(this.graph.get(from));
    }

    private void checkNodes(@Nonnull final T from, @Nonnull final T to) {
        if (!(this.graph.containsKey(from) && this.graph.containsKey(to))) throw new NoSuchElementException("Missing nodes from graph");
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return this.orderedNodes.iterator();
    }

    @Override
    public String toString() {
        return this.graph.toString();
    }
}
