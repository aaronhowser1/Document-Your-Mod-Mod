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
public final class TreeVariantsGenerationStatusConditionFactory implements ConditionFactory {
    private enum Variant {
        SWAMP("enableSwamp"),
        SAKURA("enableSakura");

        private final String fieldName;

        Variant(@Nonnull final String fieldName) {
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
        final String targetVariant = JsonUtilities.getString(object, "variant");
        final Variant variant = Arrays.stream(Variant.values())
                .filter(it -> targetVariant.equals(it.name().toLowerCase(Locale.ENGLISH)))
                .findFirst()
                .orElseThrow(() -> new JsonSyntaxException("Unrecognized variant '" + targetVariant + "'"));

        return new BasicCondition(this.reflectInto(variant));
    }

    private boolean reflectInto(@Nonnull final Variant variant) {
        try {
            final Class<?> target = Class.forName("vazkii.quark.world.feature.TreeVariants");
            final Field targetField = target.getDeclaredField(variant.getFieldName());
            targetField.setAccessible(true);
            return (boolean) targetField.get(null);
        } catch (@Nonnull final ReflectiveOperationException e) {
            Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState())
                    .getReporter().interrupt("Unable to identify variant '" + variant + "' for Tree Variants! Returning false to be safe!");
            return false;
        }
    }
}
