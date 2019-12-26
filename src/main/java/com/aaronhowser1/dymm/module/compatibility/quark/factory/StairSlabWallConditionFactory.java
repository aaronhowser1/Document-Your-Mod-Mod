package com.aaronhowser1.dymm.module.compatibility.quark.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dymm.module.base.BasicCondition;
import com.google.common.base.CaseFormat;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public final class StairSlabWallConditionFactory implements ConditionFactory {
    private enum Type {
        STAIR("enableStairs"),
        SLAB("enableSlabs"),
        STAIR_SLAB("enableStairsAndSlabs"),
        WALL("enableWalls");

        private final String fieldName;

        Type(@Nonnull final String fieldName) {
            this.fieldName = fieldName;
        }

        @Nonnull
        private String getFieldName() {
            return this.fieldName;
        }
    }

    private static final Map<String, Map<Type, Boolean>> CACHE = new HashMap<>();

    @Nonnull
    @Override
    public Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String module = JsonUtilities.getString(object, "module");
        if (!module.toLowerCase(Locale.ENGLISH).equals(module)) throw new JsonSyntaxException("Module name '" + module + "' is invalid: must be lowercase");
        final String feature = JsonUtilities.getString(object, "feature");
        if (!feature.toLowerCase(Locale.ENGLISH).equals(feature)) throw new JsonSyntaxException("Feature name '" + feature + "' is invalid: must be lowercase");
        final String targetType = JsonUtilities.getString(object, "target");
        final Type type = Arrays.stream(Type.values())
                .filter(it -> targetType.equals(it.name().toLowerCase(Locale.ENGLISH)))
                .findFirst()
                .orElseThrow(() -> new JsonSyntaxException("Unrecognized target '" + targetType + "'"));

        final String key = module + "@" + feature;

        return new BasicCondition(this.toBoolean(module, feature, type, CACHE.computeIfAbsent(key, it -> this.reflectInto(module, feature)).get(type)));
    }

    @Nonnull
    private Map<Type, Boolean> reflectInto(@Nonnull final String module, @Nonnull final String feature) {
        try {
            final String className = this.toClassName(feature);
            final Class<?> targetClass = Class.forName(String.format("vazkii.quark.%s.feature.%s", module, className));

            final Map<Type, Boolean> map = new HashMap<>();

            Arrays.stream(Type.values()).forEach(type -> {
                try {
                    final Field target = targetClass.getDeclaredField(type.getFieldName());
                    target.setAccessible(true);
                    map.put(type, (boolean) target.get(targetClass));
                } catch (@Nonnull final ReflectiveOperationException e) {
                    // I guess the field doesn't exist: not a problem
                }
            });

            return map;
        } catch (@Nonnull final ReflectiveOperationException e) {
            throw new JsonParseException("Unable to find feature '" + feature + "' in module '" + module + "'");
        }
    }

    @Nonnull
    private String toClassName(@Nonnull final String feature) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, feature);
    }

    private boolean toBoolean(@Nonnull final String module, @Nonnull final String feature, @Nonnull final Type type,
                              @Nullable final Boolean wrapped) {
        if (wrapped == null) {
            final GlobalLoadingState state = Objects.requireNonNull(ApiBindings.getMainApi().getCurrentLoadingState());
            state.getReporter().report("No type binding found in feature '" + feature +
                    "' (module '" + module + "') for type '" + type + "': taking a safe approach and returning false");
            return false;
        }
        return wrapped;
    }
}
