package com.aaronhowser1.dymm.api.loading.factory;

import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

/**
 * Represents a factory that can read data from a {@link JsonObject} and
 * convert it to a {@link Condition}.
 *
 * <p>There is no contract on whether the returned {@code Condition} must be
 * new or cached, as long as it represents the correct value that would be
 * returned by a new condition.</p>
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
public interface ConditionFactory {
    /**
     * Converts a deserialized {@link JsonObject} into a {@link Condition} for
     * usage in a
     * {@link com.aaronhowser1.dymm.api.documentation.DocumentationEntry}.
     *
     * @param state
     *      The {@link GlobalLoadingState} that represents the current status
     *      of the loading process when this factory is called. Guaranteed not
     *      to be {@code null}.
     * @param object
     *      The {@link JsonObject} that identifies the deserialized form of
     *      this condition. Guaranteed not to be {@code null}.
     * @return
     *      A {@link Condition} object representing the serialized form of
     *      the JSON Object. Must not be {@code null}
     * @throws com.google.gson.JsonSyntaxException
     *      If the contents of the JSON object do not respect the structure for
     *      the deserialization of a valid {@link Condition} or present some
     *      syntax errors (such as invalid values for enum-like parameters).
     * @throws com.google.gson.JsonParseException
     *      If the contents of the JSON object could not be parsed for any
     *      other reason.
     *
     * @see GlobalLoadingState
     * @see Condition
     * @since 2.0.0
     */
    @Nonnull Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object);
}
