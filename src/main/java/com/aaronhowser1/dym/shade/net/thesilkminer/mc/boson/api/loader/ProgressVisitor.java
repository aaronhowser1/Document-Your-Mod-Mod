package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Unit;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public interface ProgressVisitor {
    @Nonnull Unit beginVisit();
    @Nonnull Unit visitPhases(final int total);
    @Nonnull Unit visitPhase(@Nonnull final LoadingPhase<?> phase);
    @Nonnull Unit visitItemsTotal(final int total);
    @Nonnull Unit visitLocation(@Nonnull final Location location, final boolean isDirectory);
    @Nonnull Unit visitItems(final int amount);
    @Nonnull Unit visitItem(@Nonnull final NameSpacedString name);
    @Nonnull Unit endVisit();

    @Nonnull
    default ProgressVisitor chain(@Nonnull final ProgressVisitor other) {
        final class ChainingProgressVisitor implements ProgressVisitor {
            private final List<ProgressVisitor> visitors = new ArrayList<>();

            private ChainingProgressVisitor(@Nonnull final ProgressVisitor first) {
                this.visitors.add(first);
            }

            @Nonnull
            @Override
            public Unit beginVisit() {
                this.visitors.forEach(ProgressVisitor::beginVisit);
                return Unit.UNIT;
            }

            @Nonnull
            @Override
            public Unit visitPhases(final int total) {
                this.visitors.forEach(it -> it.visitPhases(total));
                return Unit.UNIT;
            }

            @Nonnull
            @Override
            public Unit visitPhase(@Nonnull final LoadingPhase<?> phase) {
                this.visitors.forEach(it -> it.visitPhase(phase));
                return Unit.UNIT;
            }

            @Nonnull
            @Override
            public Unit visitItemsTotal(final int total) {
                this.visitors.forEach(it -> it.visitItemsTotal(total));
                return Unit.UNIT;
            }

            @Nonnull
            @Override
            public Unit visitLocation(@Nonnull final Location location, final boolean isDirectory) {
                this.visitors.forEach(it -> it.visitLocation(location, isDirectory));
                return Unit.UNIT;
            }

            @Nonnull
            @Override
            public Unit visitItems(final int amount) {
                this.visitors.forEach(it -> it.visitItems(amount));
                return Unit.UNIT;
            }

            @Nonnull
            @Override
            public Unit visitItem(@Nonnull final NameSpacedString name) {
                this.visitors.forEach(it -> it.visitItem(name));
                return Unit.UNIT;
            }

            @Nonnull
            @Override
            public Unit endVisit() {
                this.visitors.forEach(ProgressVisitor::endVisit);
                return Unit.UNIT;
            }

            @Nonnull
            @Override
            public ProgressVisitor chain(@Nonnull final ProgressVisitor other) {
                this.visitors.add(other);
                return this;
            }
        }

        return new ChainingProgressVisitor(this).chain(other);
    }
}
