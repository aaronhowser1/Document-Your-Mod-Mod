package com.aaronhowser1.dymm.module.compatibility.quark.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dymm.module.base.BasicCondition;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Objects;

@SuppressWarnings("unused")
public final class MagicalHorseWhistleConditionFactory implements ConditionFactory {
    @Nonnull
    @Override
    public Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final boolean targetValue = JsonUtilities.getBoolean(object, "value");
        return new BasicCondition(this.reflectInto() == targetValue);
    }

    private boolean reflectInto() {
        try {
            final Class<?> target = Class.forName("vazkii.quark.misc.feature.HorseWhistle");
            final Field targetField = target.getDeclaredField("horsesAreMagical");
            targetField.setAccessible(true);
            return (boolean) targetField.get(null);
        } catch (@Nonnull final ReflectiveOperationException e) {
            Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState())
                    .getReporter().interrupt("Unable to know whether horses are magical! Returning false to be safe!");
            return false;
        }
    }
}
