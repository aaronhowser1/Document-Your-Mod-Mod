package com.aaronhowser1.dymm.module.base;

import com.aaronhowser1.dymm.api.documentation.Target;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class BasicItemTarget implements Target {
    private final ItemStack stack;

    public BasicItemTarget(@Nonnull final ItemStack stack) {
        this.stack = Objects.requireNonNull(stack).copy();
    }

    @Nonnull
    @Override
    public ItemStack obtainTarget() {
        return this.stack.copy();
    }
}
