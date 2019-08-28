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

import com.aaronhowser1.documentmod.json.ModDocumentation;
import com.aaronhowser1.documentmod.json.Requirement;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author cpw (edited by Silk)
 *
 */
public final class DocumentationSorter {

    private static final class NonFinalBoolean {
        private boolean b;

        NonFinalBoolean(final boolean initial) {
            this.b = initial;
        }

        void setB(final boolean b) {
            this.b = b;
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        boolean getB() {
            return this.b;
        }
    }

    private TopologicalSort.DirectedGraph<ModDocumentation> documentationGraph;

    private final ModDocumentation beforeAll = ModDocumentation.createForSorting("before_all");
    private final ModDocumentation afterAll = ModDocumentation.createForSorting("after_all");
    private final Map<String, Pair<ModDocumentation, ModDocumentation>> particularDocumentations = Maps.newHashMap();

    public DocumentationSorter(@Nonnull final List<ModDocumentation> documentationList, @Nonnull final Map<ResourceLocation, ModDocumentation> nameLookup) {
        this.buildGraph(documentationList, nameLookup);
    }

    private void buildGraph(@Nonnull final List<ModDocumentation> documentationList, @Nonnull final Map<ResourceLocation, ModDocumentation> nameLookup) {
        this.documentationGraph = new TopologicalSort.DirectedGraph<>();
        this.documentationGraph.addNode(this.beforeAll);
        this.documentationGraph.addNode(this.afterAll);

        Loader.instance().getActiveModList().stream()
                .map(ModContainer::getModId)
                .forEach(id -> {
                    final ModDocumentation beforeMod = ModDocumentation.createForSorting("before_" + id);
                    final ModDocumentation afterMod = ModDocumentation.createForSorting("after_" + id);

                    this.particularDocumentations.put(id, ImmutablePair.of(beforeMod, afterMod));

                    this.documentationGraph.addNode(beforeMod);
                    this.documentationGraph.addNode(afterMod);
                    this.documentationGraph.addEdge(this.beforeAll, beforeMod);
                    this.documentationGraph.addEdge(beforeMod, afterMod);
                    this.documentationGraph.addEdge(afterMod, this.afterAll);
                });

        documentationList.forEach(this.documentationGraph::addNode);

        documentationList.forEach(documentation -> {
            final NonFinalBoolean preDepAdded = new NonFinalBoolean(false);
            final NonFinalBoolean postDepAdded = new NonFinalBoolean(false);

            documentation.getRequirements().stream()
                    .filter(requirement -> Requirement.Ordering.AFTER == requirement.getOrdering())
                    .forEach(requirement -> {
                        preDepAdded.setB(true);

                        final ResourceLocation target = requirement.getReferredRegistryName();
                        final String targetMod = target.getNamespace();
                        final String targetPath = target.getPath();

                        if ("*".equals(targetPath)) {
                            // After everything in this mod
                            this.documentationGraph.addEdge(documentation, this.afterAll);
                            this.documentationGraph.addEdge(this.particularDocumentations.get(targetMod).getValue(), documentation);
                            postDepAdded.setB(true);
                        } else {
                            this.documentationGraph.addEdge(this.particularDocumentations.get(targetMod).getKey(), documentation);
                            if (nameLookup.containsKey(target)) {
                                this.documentationGraph.addEdge(nameLookup.get(target), documentation);
                            }
                        }
                    });

            documentation.getRequirements().stream()
                    .filter(requirement -> Requirement.Ordering.BEFORE == requirement.getOrdering())
                    .forEach(requirement -> {
                        postDepAdded.setB(true);

                        final ResourceLocation target = requirement.getReferredRegistryName();
                        final String targetMod = target.getNamespace();
                        final String targetPath = target.getPath();

                        if ("*".equals(targetPath)) {
                            // Before everything in this mod
                            this.documentationGraph.addEdge(this.beforeAll, documentation);
                            this.documentationGraph.addEdge(documentation, this.particularDocumentations.get(targetMod).getKey());

                            preDepAdded.setB(true);
                        } else {
                            this.documentationGraph.addEdge(documentation, this.particularDocumentations.get(targetMod).getValue());
                            if (nameLookup.containsKey(target)) {
                                this.documentationGraph.addEdge(documentation, nameLookup.get(target));
                            }
                        }
                    });

            final ResourceLocation registryName = Objects.requireNonNull(documentation.getRegistryName());
            final String modNamespace = registryName.getNamespace();

            if (!preDepAdded.getB()) {
                this.documentationGraph.addEdge(this.particularDocumentations.get(modNamespace).getKey(), documentation);
            }

            if (!postDepAdded.getB()) {
                this.documentationGraph.addEdge(documentation, this.particularDocumentations.get(modNamespace).getValue());
            }
        });
    }

    @Nonnull
    public List<ModDocumentation> sort() {
        List<ModDocumentation> sortedList = TopologicalSort.topologicalSort(this.documentationGraph);
        sortedList.removeAll(Arrays.asList(this.beforeAll, this.afterAll));
        this.particularDocumentations.values().forEach(value -> {
            sortedList.remove(value.getKey());
            sortedList.remove(value.getValue());
        });
        return sortedList;
    }
}