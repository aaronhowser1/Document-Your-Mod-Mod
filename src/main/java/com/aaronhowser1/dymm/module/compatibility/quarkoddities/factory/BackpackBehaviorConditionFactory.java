package com.aaronhowser1.dymm.module.compatibility.quarkoddities.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dymm.module.base.BasicCondition;
import com.aaronhowser1.dymm.module.compatibility.quark.factory.ArchaeologistHatBehaviorConditionFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("unused")
public final class BackpackBehaviorConditionFactory implements ConditionFactory {
    private enum Behavior {
        TRADE("enableTrades"),
        OP("superOpMode");

        private final String fieldName;

        Behavior(final String fieldName) {
            this.fieldName = fieldName;
        }

        @Nonnull
        private String getFieldName() {
            return this.fieldName;
        }
    }

    @Nonnull
    @Override
    public Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String targetBehavior = JsonUtilities.getString(object, "behavior");
        final Behavior behavior = Arrays.stream(Behavior.values())
                .filter(it -> targetBehavior.equals(it.name().toLowerCase(Locale.ENGLISH)))
                .findFirst()
                .orElseThrow(() -> new JsonSyntaxException("Unrecognized behavior '" + targetBehavior + "' for Backpack"));
        final boolean targetValue = JsonUtilities.getBooleanOrElse(object,"value", () -> true);
        return new BasicCondition(this.reflectInto(behavior) == targetValue);
    }

    private boolean reflectInto(@Nonnull final Behavior behavior) {
        try {
            final Class<?> target = Class.forName("vazkii.quark.oddities.feature.Backpacks");
            final Field targetField = target.getDeclaredField(behavior.getFieldName());
            targetField.setAccessible(true);
            return (boolean) targetField.get(null);
        } catch (@Nonnull final ReflectiveOperationException e) {
            Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState())
                    .getReporter().interrupt("Unable to determine behavior " + behavior + " for backpack! Returning false to be safe!");
            return false;
        }
    }
}
