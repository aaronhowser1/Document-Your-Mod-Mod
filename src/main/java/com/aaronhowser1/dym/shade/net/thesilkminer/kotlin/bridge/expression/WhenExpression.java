package com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.expression;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction0;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public final class WhenExpression<T> implements KFunction0<T> {
    public static final class Case<T> implements KFunction0<T> {
        private final boolean test;
        private final KFunction0<T> function;

        private Case(final boolean test, @Nonnull final KFunction0<T> function) {
            this.test = test;
            this.function = Objects.requireNonNull(function);
        }

        @Nonnull
        public static <T> Case<T> create(final boolean test, @Nonnull final KFunction0<T> function) {
            return new Case<>(test, function);
        }

        private boolean matches() {
            return this.test;
        }

        @Nonnull
        @Override
        public T invoke() {
            return this.function.invoke();
        }
    }

    private final List<Case<T>> cases;
    private final KFunction0<T> elseClause;

    private WhenExpression(@Nonnull final List<Case<T>> cases, @Nonnull final KFunction0<T> elseClause) {
        this.cases = Objects.requireNonNull(cases);
        this.elseClause = Objects.requireNonNull(elseClause);
    }

    @Nonnull
    public static <T> WhenExpression<T> create(@Nonnull final List<Case<T>> cases, @Nonnull final KFunction0<T> elseClause) {
        return new WhenExpression<>(cases, elseClause);
    }

    @Nonnull
    @Override
    public T invoke() {
        return this.cases.stream()
                .filter(Case::matches)
                .findFirst()
                .map(it -> it.function)
                .orElse(elseClause)
                .invoke();
    }
}
