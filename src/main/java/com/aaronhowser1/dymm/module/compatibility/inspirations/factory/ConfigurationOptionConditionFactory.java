package com.aaronhowser1.dymm.module.compatibility.inspirations.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dymm.module.base.BasicCondition;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

@SuppressWarnings("unused")
public final class ConfigurationOptionConditionFactory implements ConditionFactory {
    @Nonnull
    @Override
    public Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String value = JsonUtilities.getString(object, "value");
        try {
            return new BasicCondition(this.reflectInto(value));
        } catch (@Nonnull final ReflectiveOperationException e) {
            throw new JsonParseException("Unable to reflectively load value '" + value + "' from the configuration file");
        }
    }

    private boolean reflectInto(@Nonnull final String fieldName) throws ReflectiveOperationException {
        final Class<?> configurationClass = Class.forName("knightminer.inspirations.common.Config");
        final Field targetField = configurationClass.getDeclaredField(fieldName);
        if (targetField.isAccessible()) throw new JsonParseException("Target configuration field '" + fieldName + "'is not accessible");
        final Object value = targetField.get(configurationClass);
        if (!(value instanceof Boolean)) throw new JsonParseException("Target field '" + fieldName + "' did not store a boolean value: this is invalid");
        return (Boolean) value;
    }
}
