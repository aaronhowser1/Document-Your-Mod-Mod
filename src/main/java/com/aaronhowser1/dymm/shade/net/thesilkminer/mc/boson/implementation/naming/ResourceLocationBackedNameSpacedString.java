package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.implementation.naming;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.ElvisExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.WhenExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class ResourceLocationBackedNameSpacedString implements NameSpacedString {
    private final ResourceLocation backend;

    private ResourceLocationBackedNameSpacedString(@Nonnull final Nullable<String> domain, @Nonnull final String path) {
        this.backend = new ResourceLocation(ElvisExpression.<String>create(domain, () -> "").invoke(), path);
    }

    @Nonnull
    public static ResourceLocationBackedNameSpacedString create(@Nonnull final Nullable<String> domain, @Nonnull final String path) {
        return new ResourceLocationBackedNameSpacedString(domain, path);
    }

    @Nonnull
    @Override
    public String getNameSpace() {
        return this.backend.getNamespace();
    }

    @Nonnull
    @Override
    public String getPath() {
        return this.backend.getPath();
    }

    @Override
    public int compareTo(@Nonnull final NameSpacedString o) {
        return this.backend.compareTo(new ResourceLocation(o.getNameSpace(), o.getPath()));
    }

    @Override
    public int hashCode() {
        return this.backend.hashCode();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean equals(@javax.annotation.Nullable final Object other) {
        return WhenExpression
                .create(ImmutableList.of(
                        WhenExpression.Case.create(this == other, () -> true),
                        WhenExpression.Case.create(other == null, () -> false),
                        WhenExpression.Case.create(other instanceof ResourceLocationBackedNameSpacedString, () -> {
                            final ResourceLocationBackedNameSpacedString casted = (ResourceLocationBackedNameSpacedString) other;
                            return this.backend.equals(casted.backend);
                        }),
                        WhenExpression.Case.create(other instanceof NameSpacedString, () -> {
                            final NameSpacedString casted = (NameSpacedString) other;
                            return this.equals(new ResourceLocationBackedNameSpacedString(Nullable.get(casted.getNameSpace()), casted.getPath()));
                        })
                ), () -> this.backend.equals(other))
                .invoke();
    }

    @Override
    public String toString() {
        return this.backend.toString();
    }
}
