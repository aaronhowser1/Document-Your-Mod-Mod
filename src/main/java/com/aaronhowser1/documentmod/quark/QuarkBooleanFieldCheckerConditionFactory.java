package com.aaronhowser1.documentmod.quark;

import com.aaronhowser1.documentmod.json.ConditionFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BooleanSupplier;

public class QuarkBooleanFieldCheckerConditionFactory implements ConditionFactory {

    @Nonnull
    @Override
    public BooleanSupplier produce(@Nonnull final JsonObject object, @Nonnull final ModContainer modContainer) {
        if (!Loader.isModLoaded("quark")) return () -> false;
        final String className = JsonUtils.getString(object, "class");
        final String fieldName = JsonUtils.getString(object, "field");
        final boolean value = JsonUtils.getBoolean(object, "value");

        try {
            final Class<?> clazz = Class.forName(className);
            final Field field = clazz.getDeclaredField(fieldName);
            if ((field.getModifiers() & Modifier.PUBLIC) == 0) {
                throw new JsonSyntaxException("Given field '" + fieldName + "' in class '" + className + "' is not accessible");
            }
            if ((field.getModifiers() & Modifier.STATIC) == 0) {
                throw new JsonSyntaxException("Given field '" + fieldName + "' must be static in class '" + className + "'");
            }
            final boolean fieldValue = (boolean) field.get(null);
            return () -> value == fieldValue;
        } catch (@Nonnull final ReflectiveOperationException e) {
            throw new JsonParseException("An error has occurred while attempting to retrieve the configuration option", e);
        }
    }
}
