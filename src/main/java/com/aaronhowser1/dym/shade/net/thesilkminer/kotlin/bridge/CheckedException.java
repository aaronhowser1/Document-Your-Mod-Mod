package com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Objects;

public final class CheckedException extends RuntimeException {
    private final Exception e;

    private CheckedException(@Nonnull final Exception e) {
        this.e = Objects.requireNonNull(e);
    }

    @Nonnull
    public static CheckedException wrap(@Nonnull final Exception e) {
        return new CheckedException(e);
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.e.getMessage();
    }

    @Nullable
    @Override
    public String getLocalizedMessage() {
        return this.e.getLocalizedMessage();
    }

    @Nonnull
    @Override
    public synchronized Throwable getCause() {
        return this.e.getCause();
    }

    @Override
    public synchronized Throwable initCause(@Nullable final Throwable cause) {
        this.e.initCause(cause);
        return this;
    }

    @Nonnull
    @Override
    public String toString() {
        return this.e.toString();
    }

    @Override
    public void printStackTrace() {
        this.e.printStackTrace();
    }

    @Override
    public void printStackTrace(@Nonnull final PrintStream s) {
        this.e.printStackTrace(s);
    }

    @Override
    public void printStackTrace(@Nonnull final PrintWriter s) {
        this.e.printStackTrace(s);
    }

    @Nonnull
    @Override
    public synchronized Throwable fillInStackTrace() {
        this.e.fillInStackTrace();
        return this;
    }

    @Nonnull
    @Override
    public StackTraceElement[] getStackTrace() {
        return this.e.getStackTrace();
    }

    @Override
    public void setStackTrace(@Nonnull final StackTraceElement[] stackTrace) {
        this.e.setStackTrace(stackTrace);
    }
}
