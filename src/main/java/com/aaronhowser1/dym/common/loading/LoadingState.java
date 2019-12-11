package com.aaronhowser1.dym.common.loading;

import com.aaronhowser1.dym.L;
import com.aaronhowser1.dym.api.loading.DocumentationLoader;
import com.aaronhowser1.dym.api.loading.GlobalLoadingState;
import com.aaronhowser1.dym.api.loading.Reporter;
import com.aaronhowser1.dym.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dym.api.loading.factory.TargetFactory;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.Context;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class LoadingState implements GlobalLoadingState, Reporter {
    private final ResourceLocation targetIdentifier;
    private final L l;
    private final DocumentationLoader loader;
    private final Context globalContext;

    private LoadingState(@Nonnull final ResourceLocation targetIdentifier, @Nonnull final L log, @Nonnull final DocumentationLoader loader, @Nonnull final Context globalContext) {
        this.targetIdentifier = Objects.requireNonNull(targetIdentifier);
        this.l = Objects.requireNonNull(log);
        this.loader = Objects.requireNonNull(loader);
        this.globalContext = Objects.requireNonNull(globalContext);
    }

    @Nonnull
    @SuppressWarnings("SameParameterValue")
    static LoadingState build(@Nonnull final ResourceLocation targetIdentifier, @Nonnull final L log, @Nonnull final DocumentationLoader loader, @Nonnull final Context globalContext) {
        return new LoadingState(targetIdentifier, log, loader, globalContext);
    }

    @Override
    public void notify(@Nonnull final String message, @Nonnull final Object... arguments) {
        this.l.info("In '" + this.targetIdentifier + "': " + message, arguments);
    }

    @Override
    public void report(@Nonnull final String message, @Nonnull final Object... arguments) {
        this.l.warn("In '" + this.targetIdentifier + "': " + message, arguments);
    }

    @Override
    public void interrupt(@Nonnull final String message, @Nonnull final Object... arguments) {
        this.l.error("An error has occurred in loader '" + this.loader.getIdentifier() + "' while attempting to load '" + this.targetIdentifier + "'");
        this.l.error(message, arguments);
    }

    @Nonnull
    @Override
    public Reporter getReporter() {
        return this;
    }

    @Nonnull
    @Override
    public ConditionFactory getConditionFactory(@Nonnull final ResourceLocation location) {
        final ConditionFactory factory = DocumentationLoadingProcessor.<ConditionFactory>get(this.globalContext, "condition").get(location);
        if (factory == null) {
            throw new IllegalArgumentException("The given identifier '" + location + "' does not represent a known condition factory");
        }
        return factory;
    }

    @Nonnull
    @Override
    public TargetFactory getTargetFactory(@Nonnull final ResourceLocation location) {
        final TargetFactory factory = DocumentationLoadingProcessor.<TargetFactory>get(this.globalContext, "target").get(location);
        if (factory == null) {
            throw new IllegalArgumentException("The given identifier '" + location + "' does not represent a known target factory");
        }
        return factory;
    }
}
