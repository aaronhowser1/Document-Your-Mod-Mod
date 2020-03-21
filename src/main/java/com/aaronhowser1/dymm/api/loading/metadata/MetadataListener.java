package com.aaronhowser1.dymm.api.loading.metadata;

import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

/**
 * Identifies a listener for a specific metadata.
 *
 * <p>Metadata is defined as a JSON file named {@code _metadata.json}, where
 * {@code metadata} identifies the name of the metadata.</p>
 *
 * <p>Listeners are then given free access to the {@link JsonObject} that is
 * defined in that file for their own processing, allowing for encoding of
 * custom data that can't fit inside a documentation entry.</p>
 *
 * <p>This is a {@link FunctionalInterface} and its functional method is
 * {@link #processMetadata(JsonObject, String)}.</p>
 *
 * @since 2.0.0
 */
@FunctionalInterface
public interface MetadataListener {
    /**
     * Processes the contents of a metadata file.
     *
     * @param object
     *      The {@link JsonObject} that represents the contents of the metadata
     *      file. It is guaranteed not to be {@code null}.
     * @param nameSpace
     *      The namespace to which this metadata type is a part of. In other
     *      words, assuming a metadata file is identified by a name-spaced
     *      string like {@code "nameSpace:_metadata"}, this parameter hosts
     *      the {@code "nameSpace"} string of that name. It is guaranteed
     *      not to be {@code null} and not to be empty.
     * @throws com.google.gson.JsonSyntaxException
     *      If the contents of the JSON object do not respect the structure for
     *      the deserialization of this metadata type or present some syntax
     *      errors (such as invalid values for enum-like parameters).
     * @throws com.google.gson.JsonParseException
     *      If the contents of the JSON object could not be parsed for any
     *      other reason.
     *
     * @since 2.0.0
     */
    void processMetadata(@Nonnull final JsonObject object, @Nonnull final String nameSpace);
}
