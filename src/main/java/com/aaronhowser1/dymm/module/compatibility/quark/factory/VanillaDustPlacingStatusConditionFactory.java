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
public final class VanillaDustPlacingStatusConditionFactory implements ConditionFactory {
    private enum Dust {
        GLOWSTONE("enableGlowstone"),
        GUNPOWDER("enableGunpowder");

        private final String fieldName;

        Dust(@Nonnull final String fieldName) {
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
        final String targetDust = JsonUtilities.getString(object, "dust");
        final Dust dust = Arrays.stream(Dust.values())
                .filter(it -> targetDust.equals(it.name().toLowerCase(Locale.ENGLISH)))
                .findFirst()
                .orElseThrow(() -> new JsonSyntaxException("Unrecognized dust '" + targetDust + "' for Placeable Dusts"));
        return new BasicCondition(this.reflectInto(dust));
    }

    private boolean reflectInto(@Nonnull final Dust dust) {
        try {
            final Class<?> target = Class.forName("vazkii.quark.misc.feature.PlaceVanillaDusts");
            final Field targetField = target.getDeclaredField(dust.getFieldName());
            targetField.setAccessible(true);
            return (boolean) targetField.get(null);
        } catch (@Nonnull final ReflectiveOperationException e) {
            Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState())
                    .getReporter().interrupt("Unable to determine dust " + dust + " for Placeable Dusts! Returning false to be safe!");
            return false;
        }
    }
}
