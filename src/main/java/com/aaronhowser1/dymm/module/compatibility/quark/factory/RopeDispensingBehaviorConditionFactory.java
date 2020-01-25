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
public final class RopeDispensingBehaviorConditionFactory implements ConditionFactory {
    @Nonnull
    @Override
    public Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final boolean targetValue = JsonUtilities.getBooleanOrElse(object, "value", () -> true);
        return new BasicCondition(this.reflectInto() == targetValue);
    }

    private boolean reflectInto() {
        try {
            final Class<?> target = Class.forName("vazkii.quark.decoration.feature.Rope");
            final Field targetField = target.getDeclaredField("enableDispenser");
            targetField.setAccessible(true);
            return (boolean) targetField.get(null);
        } catch (@Nonnull final ReflectiveOperationException e) {
            Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState())
                    .getReporter().interrupt("Unable to determine interactions between ropes and dispensers! Returning false to be safe!");
            return false;
        }
    }
}
