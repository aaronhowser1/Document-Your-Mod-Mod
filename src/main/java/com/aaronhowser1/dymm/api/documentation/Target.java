package com.aaronhowser1.dymm.api.documentation;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Represents a target for a given {@link DocumentationEntry}, which has to be
 * represented as an {@link ItemStack}.
 *
 * <p>There is no specification about the origin of the value that is returned,
 * but such value must be consistent between calls, meaning a singular
 * {@code Target} must only ever represent a singular {@code ItemStack}.</p>
 *
 * <p>It is suggested, though not mandatory, for implementations to return a
 * copy of the given {@link ItemStack} to ensure manipulation cannot happen. It
 * is also suggested for the main implementation to be defensive and copy the
 * returned stack to defy malicious implementations of this interface.</p>
 *
 * <p>This is a {@link FunctionalInterface} and its functional interface method
 * is {@link #obtainTarget()} ()}. Nevertheless, implementations are suggested
 * not to implement this interface functionally, e.g. with a lambda or a method
 * reference.</p>
 *
 * @since 2.0.0
 */
@FunctionalInterface
public interface Target {
    /**
     * Gets the {@link ItemStack} that acts as one of the {@link Target}s for
     * a specific {@link DocumentationEntry}.
     *
     * @return
     *      An {@link ItemStack} that acts as one of the {@link Target}s for a
     *      given {@link DocumentationEntry}. Must not be {@code null}.
     *
     * @since 2.0.0
     */
    @Nonnull ItemStack obtainTarget();
}
