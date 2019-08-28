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
import net.minecraftforge.fml.common.EnhancedRuntimeException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

final class DocumentationSortingException extends EnhancedRuntimeException {

    class SortingExceptionData<T> {

        private final T firstBadNode;
        private final Set<T> visitedNodes;

        SortingExceptionData(@Nonnull final T node, @Nonnull final Set<T> visitedNodes) {
            this.firstBadNode = node;
            this.visitedNodes = visitedNodes;
        }

        @Nonnull
        T getFirstBadNode() {
            return this.firstBadNode;
        }
        @Nonnull
        Set<T> getVisitedNodes() {
            return this.visitedNodes;
        }
    }

    private final SortingExceptionData<?> sortingExceptionData;

    <T> DocumentationSortingException(@Nonnull final String string, @Nonnull final T node, @Nonnull final Set<T> visitedNodes) {
        super(string);
        this.sortingExceptionData = new SortingExceptionData<>(node, visitedNodes);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    private <T> SortingExceptionData<T> getExceptionData() {
        return (SortingExceptionData<T>) this.sortingExceptionData;
    }

    @Override
    protected void printStackTrace(@Nonnull final WrappedPrintStream stream) {
        final SortingExceptionData<ModDocumentation> exceptionData = this.getExceptionData();
        stream.println("A dependency cycle was detected in the input documentation set so an ordering cannot be determined");
        stream.println("The first documentation in the cycle is " + exceptionData.getFirstBadNode());
        stream.println("The documentation cycle involves:");
        for (@Nonnull final ModDocumentation mc : exceptionData.getVisitedNodes())
        {
            final List<Requirement> before = mc.getRequirements().stream().filter(it -> Requirement.Ordering.BEFORE == it.getOrdering()).collect(Collectors.toList());
            final List<Requirement> after = mc.getRequirements().stream().filter(it -> Requirement.Ordering.AFTER == it.getOrdering()).collect(Collectors.toList());
            stream.println(String.format("\t%s -> before: %s, after: %s", mc.toString(), before, after));
        }
    }
}