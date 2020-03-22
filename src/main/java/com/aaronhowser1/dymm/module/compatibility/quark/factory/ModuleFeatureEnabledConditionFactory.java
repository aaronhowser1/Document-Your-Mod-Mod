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
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unused")
public final class ModuleFeatureEnabledConditionFactory implements ConditionFactory {
    private static final Map<String, Boolean> MODULE_CACHE = new HashMap<>();
    private static final Map<String, Condition> FEATURE_CACHE = new HashMap<>();
    private static final String EMPTY_FEATURE = "";

    @Nonnull
    @Override
    public Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String module = JsonUtilities.getString(object, "module");
        if (!module.toLowerCase(Locale.ENGLISH).equals(module)) throw new JsonSyntaxException("Module name '" + module + "' is invalid: must be lowercase");
        final String feature = JsonUtilities.getStringOrElse(object, "feature", () -> EMPTY_FEATURE);
        if (!feature.toLowerCase(Locale.ENGLISH).equals(feature)) throw new JsonSyntaxException("Feature name '" + feature + "' is invalid: must be lowercase");

        final boolean isEnabled = MODULE_CACHE.computeIfAbsent(module, it -> {
            //state.getReporter().notify("No cache present for module '" + it + "': computing value now");
            try {
                return this.isModuleEnabled(it);
            } catch (@Nonnull final ReflectiveOperationException e) {
                throw new JsonParseException("Given module '" + it + "' couldn't be found", e);
            }
        });

        if (!isEnabled) {
            state.getReporter().notify("Skipping condition checking for feature '" + feature + "' since parent module '" + module + "' isn't enabled");
            return new BasicCondition(false);
        }

        if (EMPTY_FEATURE.equals(feature)) return new BasicCondition(true);

        return FEATURE_CACHE.computeIfAbsent(feature, it -> {
            //state.getReporter().notify("No cache present for feature '" + it + "': computing value now");
            try {
                return new BasicCondition(this.isFeatureEnabled(module, it));
            } catch (@Nonnull final ReflectiveOperationException e) {
                throw new JsonParseException("Given feature '" + it + "' couldn't be found in module '" + module + "'", e);
            }
        });
    }

    private boolean isModuleEnabled(@Nonnull final String packageName) throws ReflectiveOperationException {
        final String className = "Quark" + StringUtils.capitalize(packageName);
        final Class<?> targetClass = Class.forName(String.format("vazkii.quark.%s.%s", packageName, className));

        // Not using the API class because that's how Quark was written
        // Vazkii has the APIs, but then hardcodes to implementation
        final Class<?> moduleClass = Class.forName("vazkii.quark.base.module.Module");
        if (!moduleClass.isAssignableFrom(targetClass)) throw new JsonParseException("The given module '" + packageName + "' is not a valid Quark module");

        final Class<?> moduleLoader = Class.forName("vazkii.quark.base.module.ModuleLoader");
        final Method isModuleEnabled = moduleLoader.getDeclaredMethod("isModuleEnabled", Class.class);
        isModuleEnabled.setAccessible(true);
        final Object result = isModuleEnabled.invoke(moduleLoader, targetClass);

        if (!(result instanceof Boolean)) throw new ReflectiveOperationException("Result was not a boolean: maybe another method?");

        return (boolean) result;
    }

    private boolean isFeatureEnabled(@Nonnull final String packageName, @Nonnull final String feature) throws ReflectiveOperationException {
        final String className = this.toClassName(feature);
        final Class<?> targetClass = Class.forName(String.format("vazkii.quark.%s.feature.%s", packageName, className));

        // Again, as above: no API because Vazkii
        final Class<?> featureClass = Class.forName("vazkii.quark.base.module.Feature");
        if (!featureClass.isAssignableFrom(targetClass)) throw new JsonParseException("The given feature '" + feature + "' in module '" + packageName + "' is not a valid Quark feature");

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
