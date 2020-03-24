package com.aaronhowser1.dymm.module.compatibility.inspirations.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dymm.module.base.BasicCondition;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unused")
public final class PulseLoadedConditionFactory implements ConditionFactory {
    private static final Condition FALSE = new BasicCondition(false);
    private static final Condition TRUE = new BasicCondition(true);
    private static final Map<String, Condition> PULSE_CACHE = new HashMap<>();

    @Nonnull
    @Override
    public Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String pulseName = JsonUtilities.getString(object, "pulse");
        if (!pulseName.toLowerCase(Locale.ENGLISH).equals(pulseName)) throw new JsonSyntaxException("Pulse name '" + pulseName + "' is invalid: must be lowercase");

        return PULSE_CACHE.computeIfAbsent(pulseName, this::findConditionForPulse);
    }

    @Nonnull
    private Condition findConditionForPulse(@Nonnull final String pulseName) {
        try {
            return this.calculateConditionForPulse(pulseName);
        } catch (@Nonnull final ReflectiveOperationException e) {
            throw new JsonParseException("The given pulse '" + pulseName + "' does not exist!");
        }
    }

    @Nonnull
    private Condition calculateConditionForPulse(@Nonnull final String pulseName) throws ReflectiveOperationException {
        final String className = "Inspirations" + StringUtils.capitalize(pulseName);
        final Class<?> pulseClass = Class.forName(String.format("knightminer.inspirations.%s.%s", pulseName, className));

        final Class<?> inspirationsMainClass = Class.forName("knightminer.inspirations.Inspirations");
        final Field pulseManagerField = inspirationsMainClass.getDeclaredField("pulseManager");
        pulseManagerField.setAccessible(true);
        final Object pulseManager = pulseManagerField.get(inspirationsMainClass);

        final Class<?> pulseManagerClass = Class.forName("slimeknights.mantle.pulsar.control.PulseManager");

        final Field pulsesField = pulseManagerClass.getDeclaredField("pulses");
        pulsesField.setAccessible(true);

        @SuppressWarnings("unchecked")
        final Map<Object, Object> pulses = (Map<Object, Object>) pulsesField.get(pulseManager);

        for (@Nonnull final Map.Entry<Object, Object> pulse : pulses.entrySet()) {
            if (this.checkIfTargetPulse(pulseClass, pulse.getKey()) && this.isPulseEnabled(pulse.getValue())) {
                return TRUE;
            }
        }

        return FALSE;
    }

    private boolean checkIfTargetPulse(@Nonnull final Class<?> pulseClass, @Nonnull final Object pulse) {
        return pulse.getClass().equals(pulseClass);
    }

    private boolean isPulseEnabled(@Nonnull final Object pulseMetadata) throws ReflectiveOperationException {
        final Class<?> pulseMetadataClass = pulseMetadata.getClass();
        final Method isEnabled = pulseMetadataClass.getDeclaredMethod("isEnabled");
        isEnabled.setAccessible(true);
        return (Boolean) isEnabled.invoke(pulseMetadata);
    }
}
