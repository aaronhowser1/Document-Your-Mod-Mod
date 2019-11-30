package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.filter;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.With;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.expression.IfExpression;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction1;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.Filter;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.Location;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.Objects;

public final class SpecialFileFilter implements Filter {
    public enum Kind {
        FACTORIES(it -> it.getFileName().toString().startsWith("_factories")),
        JSON_SCHEMA(it -> "pattern.json".equals(it.getFileName().toString())),
        UNDERSCORE_PREFIX(it -> it.getFileName().toString().startsWith("_"));

        private final KFunction1<Path, Boolean> matcher;

        Kind(@Nonnull final KFunction1<Path, Boolean> matcher) {
            this.matcher = Objects.requireNonNull(matcher);
        }
    }

    private final Kind kind;
    private final boolean inverted;

    private SpecialFileFilter(@Nonnull final Kind kind, final boolean inverted) {
        this.kind = Objects.requireNonNull(kind);
        this.inverted = inverted;
    }

    @Nonnull
    public static SpecialFileFilter create(@Nonnull final Kind kind, final boolean inverted) {
        return new SpecialFileFilter(kind, inverted);
    }

    @Nonnull
    public static SpecialFileFilter create(@Nonnull final Kind kind) {
        return create(kind, false);
    }

    @Override
    public boolean canLoad(@Nonnull final Location location) {
        return With.with(this.kind.matcher.invoke(location.getPath()), $this$receiver -> IfExpression
                .build(this.inverted, () -> !$this$receiver, () -> $this$receiver)
                .invoke());
    }
}
