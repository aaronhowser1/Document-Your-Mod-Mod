package com.aaronhowser1.documentmod.quark;

import com.aaronhowser1.documentmod.json.ConditionFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;

import javax.annotation.Nonnull;
import java.util.function.BooleanSupplier;

public class QuarkFeatureCheckingConditionFactory implements ConditionFactory {

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public BooleanSupplier produce(@Nonnull final JsonObject object, @Nonnull final ModContainer modContainer) {
        if (!Loader.isModLoaded("quark")) return () -> false;
        final String className = JsonUtils.getString(object, "class");
        try {
            final Class<?> maybeClass = Class.forName(className);
            final Class<?> featureClass = Class.forName("vazkii.quark.base.module.Feature");
            if (!featureClass.isAssignableFrom(maybeClass)) throw new JsonParseException("Given class is not a feature class");
            return () -> ModuleLoader.isFeatureEnabled((Class<? extends Feature>) maybeClass);
        } catch (final ReflectiveOperationException e) {
            throw new JsonParseException("Given class name does not exist: " + className);
        }
    }
}
