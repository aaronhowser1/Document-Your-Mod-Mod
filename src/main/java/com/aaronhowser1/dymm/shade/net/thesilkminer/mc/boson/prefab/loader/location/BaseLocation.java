package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.location;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.WhenExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Location;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.Objects;

public final class BaseLocation implements Location {
    private final Path path;
    private final Nullable<String> friendlyName;
    private final Nullable<Context> additionalContext;

    private BaseLocation(@Nonnull final Path path, @Nonnull final Nullable<String> friendlyName, @Nonnull final Nullable<Context> additionalContext) {
        this.path = Objects.requireNonNull(path);
        this.friendlyName = Objects.requireNonNull(friendlyName);
        this.additionalContext = Objects.requireNonNull(additionalContext);
    }

    @Nonnull
    public static BaseLocation create(@Nonnull final Path path, @Nonnull final Nullable<String> friendlyName, @Nonnull final Nullable<Context> additionalContext) {
        return new BaseLocation(path, friendlyName, additionalContext);
    }

    @Nonnull
    @Override
    public Path getPath() {
        return this.path;
    }

    @Nonnull
    @Override
    public Nullable<String> getFriendlyName() {
        return this.friendlyName;
    }

    @Nonnull
    @Override
    public Nullable<Context> getAdditionalContext() {
        return this.additionalContext;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean equals(@javax.annotation.Nullable final Object o) {
        return WhenExpression
                .create(ImmutableList.of(
                        WhenExpression.Case.create(this == o, () -> true),
                        WhenExpression.Case.create(o == null, () -> false),
                        WhenExpression.Case.create(o instanceof BaseLocation, () -> {
                            final BaseLocation other = (BaseLocation) o;
                            return Objects.equals(this.path, other.path)
                                    && Objects.equals(this.friendlyName, other.friendlyName)
                                    && Objects.equals(this.additionalContext, other.additionalContext);
                        })
                ), () -> false)
                .invoke();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path, this.friendlyName, this.additionalContext);
    }

    @Nonnull
    @Override
    public String toString() {
        return "BaseLocation{" +
                "path=" + this.path +
                ", friendlyName=" + this.friendlyName +
                ", additionalContext=" + this.additionalContext +
                '}';
    }
}
