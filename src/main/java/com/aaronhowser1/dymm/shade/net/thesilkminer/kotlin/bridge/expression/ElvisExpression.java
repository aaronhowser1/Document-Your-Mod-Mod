package com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction0;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class ElvisExpression<T> implements KFunction0<T> {
    private final Nullable<T> expression;
    private final KFunction0<T> otherwise;

    private ElvisExpression(@Nonnull final Nullable<T> expression, @Nonnull final KFunction0<T> otherwise) {
        this.expression = Objects.requireNonNull(expression);
        this.otherwise = Objects.requireNonNull(otherwise);
    }

    @Nonnull
    public static <T> ElvisExpression<T> create(@Nonnull final Nullable<T> expression, @Nonnull final KFunction0<T> otherwise) {
        return new ElvisExpression<>(expression, otherwise);
    }

    @Nonnull
    @Override
    public T invoke() {
        try {
            return AssertNotNullExpression.create(this.expression).invoke();
        } catch (@Nonnull final NullPointerException e) {
            return Objects.requireNonNull(this.otherwise.invoke());
        }
    }
}
