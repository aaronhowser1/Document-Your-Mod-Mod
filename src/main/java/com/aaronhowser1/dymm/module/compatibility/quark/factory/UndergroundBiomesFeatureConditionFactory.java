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
public final class UndergroundBiomesFeatureConditionFactory implements ConditionFactory {
    private enum Feature {
        BIG_GLOWSHROOM("bigGlowshroomsEnabled"),
        COBBEDSTONE("cobbedstoneEnabled"),
        ELDER_PRISMARINE("elderPrismarineEnabled"),
        FIRESTONE("firestoneEnabled"),
        GLOWCELIUM("glowceliumEnabled"),
        PERMAFROST("icystoneEnabled");

        private final String fieldName;

        Feature(@Nonnull final String fieldName) {
            this.fieldName = fieldName;
        }

        @Nonnull
        private String getFieldName() {
            return this.fieldName;
        }
    }

    @Nonnull
    @Override
    public Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull JsonObject object) {
        final String targetFeature = JsonUtilities.getString(object, "feature");
        final Feature feature = Arrays.stream(Feature.values())
                .filter(it -> targetFeature.equals(it.name().toLowerCase(Locale.ENGLISH)))
                .findFirst()
                .orElseThrow(() -> new JsonSyntaxException("Unrecognized feature '" + targetFeature + "' for Underground Biomes"));
        return new BasicCondition(this.reflectInto(feature));
    }

    private boolean reflectInto(@Nonnull final Feature feature) {
        try {
            final Class<?> target = Class.forName("vazkii.quark.world.feature.UndergroundBiomes");
            final Field targetField = target.getDeclaredField(feature.getFieldName());
            targetField.setAccessible(true);
            return (boolean) targetField.get(null);
        } catch (@Nonnull final ReflectiveOperationException e) {
            Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState())
                    .getReporter().interrupt("Unable to determine feature " + feature + " for Underground Biomes! Returning false to be safe!");
            return false;
        }
    }
}
