package com.aaronhowser1.dymm.api.loading;

import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.loading.metadata.MetadataListenerRegistry;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a loader that is able to turn a {@link JsonObject} of a given
 * type into a {@link DocumentationEntry}.
 *
 * <p>Implementations of this interface should be declared by clients through
 * the service pattern: they will be discovered by the main implementation as
 * needed through a {@link java.util.ServiceLoader}.</p>
 *
 * <p>A documentation loader should, though it's not mandatory, produce always
 * the same kind of {@code DocumentationEntry}, meaning relying always on a
 * single implementation of that interface. This is encouraged as a matter of
 * style and separation of logic. In fact, that's the reason why multiple
 * loaders can be declared per client: each loader should be responsible only
 * for a single kind of entry, identified by the loader identifier.</p>
 *
 * <p>Every documentation loader can also register one or more
 * {@link com.aaronhowser1.dymm.api.loading.metadata.MetadataListener}s.</p>
 *
 * @see DocumentationEntry
 * @see com.aaronhowser1.dymm.api.loading.metadata.MetadataListener
 * @see MetadataListenerRegistry
 * @since 2.0.0
 */
public interface DocumentationLoader {
    /**
     * Gets the identifier of the loader that the JSON file can specify to
     * invoke this loader.
     *
     * <p>The identifier is represented as a name-spaced string wrapped in a
     * {@link ResourceLocation}. E.g., a type of loader that loads entries
     * lazily could be named {@code "my_loader:lazy_loader"}.</p>
     *
     * @return
     *      The identifier of the loader that the JSON file can use to invoke
     *      this loader. It cannot be {@code null}.
     *
     * @since 2.0.0
     */
    @Nonnull ResourceLocation getIdentifier();

    /**
     * Reads the given {@link JsonObject} and deserializes it back into the
     * corresponding type of {@link DocumentationEntry}.
     *
     * <p>It is suggested for a single loader to rely only on a single
     * implementation.</p>
     *
     * <p>It is also possible for a loader to return a {@code null} entry: if
     * this is the case, it means that the loader deemed that the entry doesn't
     * need to be loaded and registered. It is illegal for a loader not to
     * throw syntax errors in case the JSON file could not be deserialized due
     * to syntax or parsing errors: {@code null} <em>must not</em> be used in
     * these cases.</p>
     *
     * @param object
     *      The {@link JsonObject} to deserialize back into a
     *      {@link DocumentationEntry}. It is guaranteed not to be
     *      {@code null}.
     * @return
     *      A {@link DocumentationEntry} that represents the actual entry
     *      counterpart of the JSON file, or {@code null} if the loader deemed
     *      that no such entry should be produced, e.g. due to configuration.
     * @throws com.google.gson.JsonSyntaxException
     *      If the contents of the JSON object do not respect the structure for
     *      the deserialization of a valid {@link DocumentationEntry} as
     *      specified by this loader or present some syntax errors (such as
     *      invalid values for enum-like parameters).
     * @throws com.google.gson.JsonParseException
     *      If the contents of the JSON object could not be parsed for any
     *      other reason.
     *
     * @see DocumentationEntry
     * @since 2.0.0
     */
    @Nullable DocumentationEntry loadFromJson(@Nonnull final JsonObject object);

    /**
     * Called by the main implementation in order to provide a way for a loader
     * to register a set of
     * {@link com.aaronhowser1.dymm.api.loading.metadata.MetadataListener}s.
     *
     * <p>The entries should be registered directly and immediately to the
     * given {@link MetadataListenerRegistry}.</p>
     *
     * @implNote
     *      By default, this implementation does nothing.
     *
     * @param registry
     *      The registry where the metadata listeners should be registered on.
     *      Guaranteed not to be {@code null}.
     *
     * @see MetadataListenerRegistry
     * @since 2.0.0
     */
    default void registerMetadataListeners(@Nonnull final MetadataListenerRegistry registry) {}

    /**
     * Called when this loader has been identified and loaded through the
     * service locator.
     *
     * @implNote
     *      By default, this implementation does nothing.
     *
     * @since 2.0.0
     */
    default void onLoad() {}

    /**
     * Called when the {@link GlobalLoadingState} is about to be removed from
     * the current loading process.
     *
     * <p>Loaders should listen to this event in case they need to perform some
     * tasks that require the global loading state to be present, but after the
     * entire loading process, such as cleanup.</p>
     *
     * <p>The {@code GlobalLoadingState} is still bound when this event is
     * fired, but it will be unbound immediately after.</p>
     *
     * @implNote
     *      By default, this implementation does nothing.
     *
     * @since 2.0.0
     */
    default void onGlobalLoadingStateUnbinding() {}
}
