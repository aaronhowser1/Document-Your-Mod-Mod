package com.aaronhowser1.documentmod.json.factory.condition;

import com.aaronhowser1.documentmod.json.factory.Factory;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface ConditionFactory extends Factory<ConditionFactory> {
    @Nonnull BooleanSupplier produce(@Nonnull final JsonObject object, @Nonnull final ModContainer modContainer);
}
