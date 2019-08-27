package com.aaronhowser1.documentmod.json.factory.condition;

import com.aaronhowser1.documentmod.config.DYMMConfig;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;

public class DocumentModConfigurationOption implements ConditionFactory {

    private static final Map<String, Boolean> CONFIGURATION_OPTION_CACHE = Maps.newHashMap();

    @Nonnull
    @Override
    public BooleanSupplier produce(@Nonnull final JsonObject object, @Nonnull final ModContainer modContainer) {
        final String name = JsonUtils.getString(object, "name");
        final boolean value = JsonUtils.getBoolean(object, "value");
        final boolean actualValue = CONFIGURATION_OPTION_CACHE.computeIfAbsent(name, s -> {
            try {
                final Class<?> configClass = DYMMConfig.class;
                Object grabbingObject;
                Field grabbingField;
                if (name.contains(".")) {
                    final String[] path = name.split(Pattern.quote("."));
                    if (path.length != 2) throw new JsonSyntaxException("Configuration option must be at most one level deep");
                    final Field tmpField = configClass.getDeclaredField(path[0]);
                    final Object tmpObject = tmpField.get(null);
                    final Class<?> tmpClass = tmpObject.getClass();
                    grabbingObject = tmpObject;
                    grabbingField = tmpClass.getDeclaredField(path[1]);
                } else {
                    grabbingObject = null;
                    grabbingField = configClass.getDeclaredField(name);
                }
                final Boolean configStatus = (Boolean) grabbingField.get(grabbingObject);
                if (configStatus == null) throw new ReflectiveOperationException();
                return configStatus;
            } catch (final ReflectiveOperationException e) {
                throw new JsonParseException("Given configuration option '" + name + "' does not exist", e);
            }
        });
        return () -> value == actualValue;
    }
}
