package com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.expression;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Unit;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction0;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction1;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public final class TryExpression<T> implements KFunction0<T> {
    public static final class CatchClause<E extends Throwable, T> implements KFunction1<Throwable, T> {
        private final KClass<E> exceptionClass;
        private final KFunction1<E, T> exceptionBlock;

        private CatchClause(@Nonnull final KClass<E> exceptionClass, @Nonnull final KFunction1<E, T> exceptionBlock) {
            this.exceptionClass = Objects.requireNonNull(exceptionClass);
            this.exceptionBlock = Objects.requireNonNull(exceptionBlock);
        }

        @Nonnull
        public static <E extends Throwable, T> CatchClause<E, T> create(@Nonnull final KClass<E> exceptionClass, @Nonnull final KFunction1<E, T> exceptionBlock) {
            return new CatchClause<>(exceptionClass, exceptionBlock);
        }

        private boolean matches(@Nonnull final KClass<?> otherExceptionClass) {
            return this.exceptionClass.equals(otherExceptionClass);
        }

        @Nonnull
        @Override
        public T invoke(@Nonnull final Throwable e) {
            return this.exceptionBlock.invoke(this.exceptionClass.cast(Nullable.get(e)));
        }
    }

    private final KFunction0<T> tryBlock;
    private final List<CatchClause<? extends Throwable, T>> catchClauses;
    private final KFunction0<Unit> finallyBlock;

    private TryExpression(@Nonnull final KFunction0<T> tryBlock, @Nonnull final List<CatchClause<? extends Throwable, T>> catchClauses, @Nonnull final KFunction0<Unit> finallyBlock) {
        this.tryBlock = Objects.requireNonNull(tryBlock);
        this.catchClauses = Objects.requireNonNull(catchClauses);
        this.finallyBlock = Objects.requireNonNull(finallyBlock);
    }

    @Nonnull
    public static <T> TryExpression<T> create(@Nonnull final KFunction0<T> tryBlock, @Nonnull final List<CatchClause<? extends Throwable, T>> catchClauses,
                                              @Nonnull final KFunction0<Unit> finallyBlock) {
        return new TryExpression<>(tryBlock, catchClauses, finallyBlock);
    }

    @Nonnull
    public static <T> TryExpression<T> create(@Nonnull final KFunction0<T> tryBlock, @Nonnull final List<CatchClause<? extends Throwable, T>> catchClauses) {
        return create(tryBlock, catchClauses, () -> Unit.UNIT);
    }

    @Nonnull
    @Override
    public T invoke() {
        try {
            return this.tryBlock.invoke();
        } catch (@Nonnull final Throwable t) {
            final KClass<?> throwableClass = KClass.get(t.getClass());
            return this.catchClauses.stream()
                    .filter(it -> it.matches(throwableClass))
                    .findFirst()
                    .map(it -> it.exceptionBlock)
                    .orElse(it -> { throw t; })
                    .invoke(this.cast(t));
        } finally {
            this.finallyBlock.invoke();
        }
    }

    @SuppressWarnings("unchecked")
    private <K> K cast(@Nonnull final Throwable t) {
        return (K) t;
    }
}
