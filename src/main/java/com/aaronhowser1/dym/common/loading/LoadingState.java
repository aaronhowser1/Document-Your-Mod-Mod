package com.aaronhowser1.dym.common.loading;

import com.aaronhowser1.dym.L;
import com.aaronhowser1.dym.api.loading.DocumentationLoader;
import com.aaronhowser1.dym.api.loading.GlobalLoadingState;
import com.aaronhowser1.dym.api.loading.Reporter;
import com.aaronhowser1.dym.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dym.api.loading.factory.TargetFactory;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.google.common.base.Preconditions;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class LoadingState implements GlobalLoadingState, Reporter {

    private static LoadingState GLOBAL = null;

    private ResourceLocation targetId;
    private L l;
    private DocumentationLoader loader;
    private Context globalContext;

    private LoadingState() {}

    @Nullable
    public static LoadingState obtainState() {
        return GLOBAL;
    }

    @SuppressWarnings("SameParameterValue")
    static void rebuild(@Nonnull final ResourceLocation targetId, @Nonnull final L l, @Nonnull final DocumentationLoader loader, @Nonnull final Context globalContext) {
        if (GLOBAL == null) GLOBAL = new LoadingState();
        GLOBAL.apply(targetId, l, loader, globalContext);
    }

    static void destroyCurrent() {
        GLOBAL = null;
    }

    private void apply(@Nonnull final ResourceLocation targetId, @Nonnull final L l, @Nonnull final DocumentationLoader loader, @Nonnull final Context globalContext) {
        this.targetId = Objects.requireNonNull(targetId);
        this.l = Objects.requireNonNull(l);
        this.loader = Objects.requireNonNull(loader);
        this.globalContext = Objects.requireNonNull(globalContext);
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

    @Override
    public void notify(@Nonnull final String message, @Nonnull final Object... arguments) {
        this.l.info("In '" + this.targetId + "': " + message, arguments);
    }

    @Override
    public void report(@Nonnull final String message, @Nonnull final Object... arguments) {
        this.l.warn("In '" + this.targetId + "': " + message, arguments);
    }

    @Override
    public void interrupt(@Nonnull final String message, @Nonnull final Object... arguments) {
        this.l.error("An error has occurred in loader '" + this.loader.getIdentifier() + "' while attempting to load '" + this.targetId + "'");
        this.l.error(message, arguments);
    }
}
