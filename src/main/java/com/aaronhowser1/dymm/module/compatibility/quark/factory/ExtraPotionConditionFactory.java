package com.aaronhowser1.dymm.module.compatibility.quark.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dymm.module.base.BasicCondition;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("unused")
public final class ExtraPotionConditionFactory implements ConditionFactory {
    private enum Potion {
        HASTE("enableHaste"),
        RESISTANCE("enableResistance"),
        DANGER_SIGHT("enableDangerSight");

        private final String fieldName;

        Potion(@Nonnull final String fieldName) {
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
        final String targetPotion = JsonUtilities.getString(object, "potion");
        final Potion potion = Arrays.stream(Potion.values())
                .filter(it -> targetPotion.equals(it.name().toLowerCase(Locale.ENGLISH)))
                .findFirst()
                .orElseThrow(() -> new JsonSyntaxException("Unrecognized potion '" + targetPotion + "'"));

        return new BasicCondition(this.reflectInto(potion));
    }

    private boolean reflectInto(@Nonnull final Potion potion) {
        try {
            final Class<?> target = Class.forName("vazkii.quark.misc.feature.ExtraPotions");
            final Field targetField = target.getDeclaredField(potion.getFieldName());
            targetField.setAccessible(true);
            return (boolean) targetField.get(null);
        } catch (@Nonnull final ReflectiveOperationException e) {
            Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState())
                    .getReporter().interrupt("Unable to determine presence of Extra Potion '" + potion + "'! Returning false to be safe!");
            return false;
        }
    }
}
