package com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.expression;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.CheckedException;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction0;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class IfExpression<T> implements KFunction0<T> {
    private final boolean condition;
    private final KFunction0<T> ifBlock;
    private final KFunction0<T> elseBlock;

    private IfExpression(final boolean condition, @Nonnull final KFunction0<T> ifBlock, @Nonnull final KFunction0<T> elseBlock) {
        this.condition = condition;
        this.ifBlock = Objects.requireNonNull(ifBlock);
        this.elseBlock = Objects.requireNonNull(elseBlock);
    }

    @Nonnull
    public static <T> IfExpression<T> build(final boolean condition, @Nonnull final KFunction0<T> ifBlock, @Nonnull final KFunction0<T> elseBlock) {
        return new IfExpression<>(condition, ifBlock, elseBlock);
    }

    @Override
    @Nonnull
    public T invoke() {
        try {
            return this.condition? this.ifBlock.invoke() : this.elseBlock.invoke();
        } catch (@Nonnull final Exception e) {
            throw CheckedException.wrap(e);
        }
    }
}
