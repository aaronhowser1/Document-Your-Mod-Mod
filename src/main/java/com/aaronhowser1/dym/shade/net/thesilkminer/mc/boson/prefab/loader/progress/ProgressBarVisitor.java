package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.progress;

import com.aaronhowser1.dym.L;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Unit;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.expression.AssertNotNullExpression;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.expression.ElvisExpression;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.LoadingPhase;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.Location;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.ProgressVisitor;
import net.minecraftforge.fml.common.ProgressManager;

import javax.annotation.Nonnull;

public final class ProgressBarVisitor implements ProgressVisitor {
    private final L l = L.create("[DYMM Shade] Boson", "Progress Bar Visitor");

    private /*mutable*/ Nullable<ProgressManager.ProgressBar> bar = Nullable.get(null);
    private /*mutable*/ Nullable<String> nextPhase = Nullable.get(null);

    @Nonnull
    @Override
    public Unit beginVisit() {
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit visitPhases(final int total) {
        this.l.debug(total + " phases to go through");
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Unit visitPhase(@Nonnull final LoadingPhase<?> phase) {
        this.endVisit();
        this.l.debug("Preparing to push new bar for phase '" + phase.getName() + "'");
        this.nextPhase = Nullable.get(phase.getName());
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit visitItemsTotal(final int total) {
        this.l.debug("Pushing bar");
        this.bar = Nullable.get(ProgressManager.push(this.nextPhase.unwrap(), total));
        this.nextPhase = Nullable.get(null);
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit visitLocation(@Nonnull final Location location, final boolean isDirectory) {
        this.l.debug("Step for path " + location.getFriendlyName());
        return AssertNotNullExpression.create(this.bar.ifPresent(it -> {
            it.step(location.getFriendlyName().unwrap());
            return Unit.UNIT;
        })).invoke();
    }

    @Nonnull
    @Override
    public Unit visitItems(final int amount) {
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit visitItem(@Nonnull final NameSpacedString name) {
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit endVisit() {
        return ElvisExpression.create(this.bar.ifPresent(it -> {
            this.l.debug("Popping bar '" + it + "'");
            ProgressManager.pop(it);
            return Unit.UNIT;
        }), () -> Unit.UNIT).invoke();
    }
}
