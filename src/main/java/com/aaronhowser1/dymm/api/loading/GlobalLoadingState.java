package com.aaronhowser1.dymm.api.loading;

import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dymm.api.loading.factory.TargetFactory;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Represents the current global loading state for all the loaders and all the
 * documentation entries.
 *
 * <p>This class is mainly used as an interface and should not be implemented
 * by clients, but only used to refer to those specific parts of the actual
 * implementation.</p>
 *
 * @since 2.0.0
 */
public interface GlobalLoadingState {
    /**
     * Obtains the currently loaded {@link Reporter} instance, which can be
     * used to report messages that {@link DocumentationLoader}s may want to
     * send.
     *
     * @return
     *      The currently loaded {@link Reporter}. Guaranteed not to be
     *      {@code null}.
     *
     * @see Reporter
     * @since 2.0.0
     */
    @Nonnull Reporter getReporter();

    /**
     * Gets the {@link ConditionFactory} that is identified by the given
     * name-spaced string, represented as a {@link ResourceLocation}.
     *
     * <p>If no {@code ConditionFactory} with that name can be found, an
     * {@link IllegalArgumentException} is thrown, rather than returning
     * {@code null}.</p>
     *
     * @param location
     *      The name-spaced string that identifies the condition factory to
     *      obtain, wrapped in a {@link ResourceLocation}. It must not be
     *      {@code null}.
     * @return
     *      The {@link ConditionFactory} that is identified by the given
     *      name. Guaranteed not to be {@code null}.
     *
     * @see com.aaronhowser1.dymm.api.documentation.Condition
     * @since 2.0.0
     */
    @Nonnull ConditionFactory getConditionFactory(@Nonnull final ResourceLocation location);

    /**
     * Gets the {@link TargetFactory} that is identified by the given
     * name-spaced string, represented as a {@link ResourceLocation}.
     *
     * <p>If no {@code TargetFactory} with that name can be found, an
     * {@link IllegalArgumentException} is thrown, rather than returning
     * {@code null}.</p>
     *
     * @param location
     *      The name-spaced string that identifies the target factory to
     *      obtain, wrapped in a {@link ResourceLocation}. It must not be
     *      {@code null}.
     * @return
     *      The {@link TargetFactory} that is identified by the given
     *      name. Guaranteed not to be {@code null}.
     *
     * @see com.aaronhowser1.dymm.api.documentation.Target
     * @since 2.0.0
     */
    @Nonnull TargetFactory getTargetFactory(@Nonnull final ResourceLocation location);
}
