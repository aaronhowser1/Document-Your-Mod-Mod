package com.aaronhowser1.dymm.api.documentation;

/**
 * Represents a condition under which a given {@link DocumentationEntry} can be
 * loaded or not.
 *
 * <p>There is no specification about the origin of the value that is returned,
 * but such value must be consistent between calls, meaning that a singular
 * {@code Condition} instance can only ever represents a {@code true} or a
 * {@code false} state for the entire lifetime of the condition.</p>
 *
 * <p>This is a {@link FunctionalInterface} and its functional interface method
 * is {@link #canParse()}. Nevertheless, implementations are suggested not to
 * implement this interface functionally, e.g. with a lambda or a method
 * reference.</p>
 */
@FunctionalInterface
public interface Condition {
    /**
     * Gets whether the {@link DocumentationEntry} this {@link Condition} is
     * bound to can be parsed or not.
     *
     * @return
     *      A boolean representing whether the target
     *      {@link DocumentationEntry} can be parsed or not. The result must
     *      respect the constraints given in the documentation for the
     *      {@link Condition} itself.
     *
     * @since 2.0.0
     */
    boolean canParse();
}
