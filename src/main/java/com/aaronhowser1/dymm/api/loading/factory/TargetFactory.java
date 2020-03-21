package com.aaronhowser1.dymm.api.loading.factory;

import com.aaronhowser1.dymm.api.documentation.Target;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Represents a factory that can read data from a {@link JsonObject} and
 * convert it to a {@link List} of {@link Target}s.
 *
 * <p>The returned {@code Target}s must always be new: factories are not
 * allowed to reuse the same {@code Target} instances for multiple return
 * values, even if the lists are the same.</p>
 *
 * <p>More-over, the returned {@code List} can be empty, have a single element,
 * or multiple ones. The latter case is especially useful for factories to
 * return a set of {@code Target}s from a larger pool that match a specific
 * predicate.</p>
 *
 * <p>This is a {@link FunctionalInterface} and its functional interface method
 * is {@link #fromJson(GlobalLoadingState, JsonObject)}. Nevertheless,
 * implementations are suggested not to implement this interface functionally,
 * since this wouldn't allow them to be loaded from the JSON {@code _factories}
 * specification.</p>
 *
 * @since 2.0.0
 */
@FunctionalInterface
public interface TargetFactory {
    /**
     * Converts a deserialized {@link JsonObject} into a {@link List} of
     * {@link Target}s for usage in a
     * {@link com.aaronhowser1.dymm.api.documentation.DocumentationEntry}.
     *
     * @param state
     *      The {@link GlobalLoadingState} that represents the current status
     *      of the loading process when this factory is called. Guaranteed not
     *      to be {@code null}.
     * @param object
     *      The {@link JsonObject} that identifies the deserialized form of
     *      this set of targets. Guaranteed not to be {@code null}.
     * @return
     *      A {@link List} of {@link Target}s that represents the serialized
     *      form of the JSON object. It must not be {@code null}, but it can
     *      be empty or host any number of instances.
     * @throws com.google.gson.JsonSyntaxException
     *      If the contents of the JSON object do not respect the structure for
     *      the deserialization of a valid {@link Target} or present some
     *      syntax errors (such as invalid values for enum-like parameters).
     * @throws com.google.gson.JsonParseException
     *      If the contents of the JSON object could not be parsed for any
     *      other reason.
     *
     * @see GlobalLoadingState
     * @see Target
     * @since 2.0.0
     */
    @Nonnull List<Target> fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object);
}
