package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.progress;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Unit;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.LoadingPhase;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Location;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.ProgressVisitor;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;

public final class ActiveModContainerVisitor implements ProgressVisitor {
    private /*mutable*/ Nullable<ModContainer> activeModContainer = Nullable.get(null);

    private ActiveModContainerVisitor() {}

    @Nonnull
    public static ActiveModContainerVisitor create() {
        return new ActiveModContainerVisitor();
    }

    @Nonnull
    @Override
    public Unit beginVisit() {
        this.activeModContainer = Nullable.get(Loader.instance().activeModContainer());
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit visitPhases(final int total) {
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit visitPhase(@Nonnull final LoadingPhase<?> phase) {
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit visitItemsTotal(final int total) {
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit visitLocation(@Nonnull final Location location, final boolean isDirectory) {
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit visitItems(final int amount) {
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit visitItem(@Nonnull final NameSpacedString name) {
        Loader.instance().setActiveModContainer(
                Loader.instance()
                        .getModList()
                        .stream()
                        .filter(it -> it.getModId().equals(name.getNameSpace()))
                        .findFirst()
                        .orElseGet(() -> this.activeModContainer.unwrap())
        );
        return Unit.UNIT;
    }

    @Nonnull
    @Override
    public Unit endVisit() {
        Loader.instance().setActiveModContainer(this.activeModContainer.unwrap());
        return Unit.UNIT;
    }
}
