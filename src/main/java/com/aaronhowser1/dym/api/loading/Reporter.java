package com.aaronhowser1.dym.api.loading;

import javax.annotation.Nonnull;

public interface Reporter {
    void notify(@Nonnull final String message, @Nonnull final Object... arguments); // INFO
    void report(@Nonnull final String message, @Nonnull final Object... arguments); // WARN
    void interrupt(@Nonnull final String message, @Nonnull final Object... arguments); // ERROR
}
