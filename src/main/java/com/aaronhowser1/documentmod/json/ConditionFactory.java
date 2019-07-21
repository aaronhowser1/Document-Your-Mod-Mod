package com.aaronhowser1.documentmod.json;

import com.google.gson.JsonObject;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface ConditionFactory {
    @Nonnull BooleanSupplier produce(@Nonnull final JsonObject object, @Nonnull final ModContainer modContainer);
}
