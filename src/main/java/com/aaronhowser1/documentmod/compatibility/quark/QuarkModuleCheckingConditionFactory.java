package com.aaronhowser1.documentmod.compatibility.quark;

import com.aaronhowser1.documentmod.json.factory.condition.ConditionFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.function.BooleanSupplier;

public class QuarkModuleCheckingConditionFactory implements ConditionFactory {

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public BooleanSupplier produce(@Nonnull final JsonObject object, @Nonnull final ModContainer modContainer) {
        if (!Loader.isModLoaded("quark")) return () -> false;
        final String className = JsonUtils.getString(object, "class");
        try {
            final Class<?> maybeClass = Class.forName(className);
            final Class<?> moduleClass = Class.forName("vazkii.quark.base.module.Module");
            if (!moduleClass.isAssignableFrom(maybeClass)) throw new JsonParseException("Given class is not a feature class");
            final Class<?> moduleLoader = Class.forName("vazkii.quark.base.module.ModuleLoader");
            final Method isModuleEnabled = moduleLoader.getDeclaredMethod("isModuleEnabled", Class.class);
            return () -> {
                try {
                    return (boolean) isModuleEnabled.invoke(null, maybeClass);
                } catch (final ReflectiveOperationException e) {
                    return false;
                }
            };
        } catch (final ReflectiveOperationException e) {
            throw new JsonParseException("Given class name does not exist: " + className);
        }
    }
}
