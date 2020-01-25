package com.aaronhowser1.dymm.module.compatibility.quark.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dymm.module.base.BasicCondition;
import com.google.common.base.CaseFormat;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unused")
public final class ExperimentalFeatureEnabledConditionFactory implements ConditionFactory {
    // Vazkii, you're an idiot!
    private static final Map<String, Condition> FEATURE_CACHE = new HashMap<>();

    @Nonnull
    @Override
    public Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String feature = JsonUtilities.getString(object, "feature");
        if (!feature.toLowerCase(Locale.ENGLISH).equals(feature)) throw new JsonSyntaxException("Feature name '" + feature + "' is invalid: must be lowercase");

        try {
            if (!this.isModuleEnabled()) {
                state.getReporter().notify("Skipping condition checking for feature '" + feature + "' since parent module 'experimental' isn't enabled");
                return new BasicCondition(false);
            }
        } catch (@Nonnull final ReflectiveOperationException e) {
            throw new JsonParseException("Given module 'experimental' couldn't be found", e);
        }

        return FEATURE_CACHE.computeIfAbsent(feature, it -> {
            state.getReporter().notify("No cache present for feature '" + it + "': computing value now");
            try {
                return new BasicCondition(this.isFeatureEnabled(it));
            } catch (@Nonnull final ReflectiveOperationException e) {
                throw new JsonParseException("Given feature '" + it + "' couldn't be found in module 'experimental'", e);
            }
        });
    }

    private boolean isModuleEnabled() throws ReflectiveOperationException {
        final Class<?> targetClass = Class.forName("vazkii.quark.experimental.QuarkExperimental");

        // Not using the API class because that's how Quark was written
        // Vazkii has the APIs, but then hardcodes to implementation
        final Class<?> moduleClass = Class.forName("vazkii.quark.base.module.Module");
        if (!moduleClass.isAssignableFrom(targetClass)) throw new JsonParseException("The given module '" + "experimental" + "' is not a valid Quark module");

        final Class<?> moduleLoader = Class.forName("vazkii.quark.base.module.ModuleLoader");
        final Method isModuleEnabled = moduleLoader.getDeclaredMethod("isModuleEnabled", Class.class);
        isModuleEnabled.setAccessible(true);
        final Object result = isModuleEnabled.invoke(moduleLoader, targetClass);

        if (!(result instanceof Boolean)) throw new ReflectiveOperationException("Result was not a boolean: maybe another method?");

        return (boolean) result;
    }

    private boolean isFeatureEnabled(@Nonnull final String feature) throws ReflectiveOperationException {
        final String className = this.toClassName(feature);
        final Class<?> targetClass = Class.forName(String.format("vazkii.quark.experimental.features.%s", className));

        // Again, as above: no API because Vazkii
        final Class<?> featureClass = Class.forName("vazkii.quark.base.module.Feature");
        if (!featureClass.isAssignableFrom(targetClass)) throw new JsonParseException("The given feature '" + feature + "' in module 'experimental' is not a valid Quark feature");

        final Class<?> moduleLoader = Class.forName("vazkii.quark.base.module.ModuleLoader");
        final Method isFeatureEnabled = moduleLoader.getDeclaredMethod("isFeatureEnabled", Class.class);
        isFeatureEnabled.setAccessible(true);
        final Object result = isFeatureEnabled.invoke(moduleLoader, targetClass);

        if (!(result instanceof Boolean)) throw new ReflectiveOperationException("Result was not a boolean: maybe another method?");

        return (boolean) result;
    }

    @Nonnull
    private String toClassName(@Nonnull final String feature) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, feature);
    }
}
