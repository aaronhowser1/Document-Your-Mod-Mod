package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.preprocessor;

import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.CheckedException;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nothing;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.With;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.IfExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Preprocessor;

import javax.annotation.Nonnull;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

public final class CatchingPreprocessor<T, R> implements Preprocessor<T, R> {
    private static final class GoThroughException extends RuntimeException {
        private final Exception wrapped;

        private GoThroughException(@Nonnull final Exception wrapped) {
            this.wrapped = Objects.requireNonNull(wrapped);
        }
    }

    private static final class CustomMessageException extends RuntimeException {
        private final String msg;
        private final Exception wrapped;

        private CustomMessageException(@Nonnull final String msg, @Nonnull final Exception wrapped) {
            this.msg = msg;
            this.wrapped = wrapped;
        }
    }

    private final L logger;
    private final Preprocessor<T, R> preprocessor;

    private CatchingPreprocessor(@Nonnull final L logger, @Nonnull final Preprocessor<T, R> preprocessor) {
        this.logger = Objects.requireNonNull(logger);
        this.preprocessor = Objects.requireNonNull(preprocessor);
    }

    @Nonnull
    public static Nothing throwException(@Nonnull final Exception e) {
        try {
            throw new GoThroughException(e);
        } catch (@Nonnull final NullPointerException ne) {
            ne.initCause(e);
            throw new GoThroughException(ne);
        }
    }

    @Nonnull
    public static Nothing withCustomMessage(@Nonnull final String message, @Nonnull final Exception e) {
        try {
            throw new CustomMessageException(message, e);
        } catch (@Nonnull final NullPointerException ne) {
            try {
                ne.initCause(e);
                throw new CustomMessageException(message, ne);
            } catch (@Nonnull final NullPointerException nne) {
                nne.initCause(ne);
                throw new GoThroughException(nne);
            }
        }
    }

    @Nonnull
    public static <T, R> CatchingPreprocessor<T, R> create(@Nonnull final L logger, @Nonnull final Preprocessor<T, R> preprocessor) {
        return new CatchingPreprocessor<>(logger, preprocessor);
    }

    @Nonnull
    @Override
    @SuppressWarnings("ConstantConditions")
    public Nullable<R> preProcessData(@Nonnull final T content, @Nonnull final NameSpacedString identifier, @Nonnull final Nullable<Context> globalContext,
                                      @Nonnull final Nullable<Context> phaseContext) {
        // Not using TryExpression because we need some more flexibility
        try {
            return this.preprocessor.preProcessData(content, identifier, globalContext, phaseContext);
        } catch (@Nonnull final Exception e) {
            if (e instanceof GoThroughException) throw CheckedException.wrap(((GoThroughException) e).wrapped);
            return With.with(this.logger, $this$receiver -> {
                final Nullable<String> errorMessage = extractMessage(Nullable.get(IfExpression
                        .build(
                                e instanceof CustomMessageException,
                                () -> ((CustomMessageException) e).wrapped,
                                () -> e
                        ).invoke()));
                final Nullable<String> exceptionType = KClass.get(IfExpression.build(
                        e instanceof CustomMessageException,
                        () -> ((CustomMessageException) e).wrapped,
                        () -> e
                ).invoke().getClass()).getSimpleName();
                final String customMessage = IfExpression.build(
                        e instanceof CustomMessageException,
                        () -> "\n\n" + ((CustomMessageException) e).msg + "\n\n",
                        () -> ""
                ).invoke();
                final StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                final String msgFixedPart = "An error has occurred while attempting to pre-process the file '" + identifier + "'\n" +
                        "Error message: " + errorMessage + customMessage + "\n" +
                        "Exception type: " + exceptionType + "\n" +
                        "Name of the file that caused the error: " + identifier;
                $this$receiver.bigError(msgFixedPart + "\n\nThe full stacktrace is in the text that follows:\n" + stringWriter, L.DumpStackBehavior.DO_NOT_DUMP);
                return Nullable.get(null);
            });
        }
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static Nullable<String> extractMessage(@Nonnull final Nullable<Throwable> $this$receiver) {
        return IfExpression.build(
                Objects.isNull($this$receiver.unwrap()),
                () -> Nullable.<String>get(null),
                () -> IfExpression.build(
                        Objects.isNull($this$receiver.unwrap().getMessage()),
                        () -> Nullable.<String>get(null),
                        () -> Nullable.get($this$receiver.unwrap().getMessage())
                ).invoke()
        ).invoke();
    }
}
