package com.aaronhowser1.dymm.api.documentation;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Target {
    @Nonnull ItemStack obtainTarget();
}
