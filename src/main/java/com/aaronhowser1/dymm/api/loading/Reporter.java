package com.aaronhowser1.dymm.api.loading;

import javax.annotation.Nonnull;

/**
 * Identifies a way that {@link DocumentationLoader}s can use to report
 * messages to the main loading system.
 *
 * <p>This class is mainly used as an interface and should not be implemented
 * by clients, but only used to refer to those specific parts of the actual
 * implementation.</p>
 *
 * @since 2.0.0
 */
public interface Reporter {
    /**
     * Notifies the given message to the main implementation.
     *
     * <p>Thinking in terms of logging, this method is similar to an
     * {@code INFO} logging level.</p>
     *
     * @param message
     *      The message to report, must not be {@code null}. It can contain
     *      formatting codes as per {@link String#format(String, Object...)}.
     * @param arguments
     *      A set of arguments that will be added to the message. It must not
     *      be {@code null}, though it can be empty.
     *
     * @since 2.0.0
     */
    void notify(@Nonnull final String message, @Nonnull final Object... arguments);

    /**
     * Reports the given message to the main implementation.
     *
     * <p>Thinking in terms of logging, this method is similar to an
     * {@code WARN} logging level.</p>
     *
     * @param message
     *      The message to report, must not be {@code null}. It can contain
     *      formatting codes as per {@link String#format(String, Object...)}.
     * @param arguments
     *      A set of arguments that will be added to the message. It must not
     *      be {@code null}, though it can be empty.
     *
     * @since 2.0.0
     */
    void report(@Nonnull final String message, @Nonnull final Object... arguments);

    /**
     * Interrupts the main implementation with the given message.
     *
     * <p>Thinking in terms of logging, this method is similar to an
     * {@code ERROR} logging level.</p>
     *
     * @param message
     *      The message to report, must not be {@code null}. It can contain
     *      formatting codes as per {@link String#format(String, Object...)}.
     * @param arguments
     *      A set of arguments that will be added to the message. It must not
     *      be {@code null}, though it can be empty.
     *
     * @since 2.0.0
     */
    void interrupt(@Nonnull final String message, @Nonnull final Object... arguments);
}
