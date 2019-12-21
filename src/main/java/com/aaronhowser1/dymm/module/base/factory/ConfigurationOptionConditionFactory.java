package com.aaronhowser1.dymm.module.base.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.documentation.Condition;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory;
import com.aaronhowser1.dymm.module.base.BasicCondition;
import com.google.gson.JsonObject;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class ConfigurationOptionConditionFactory implements ConditionFactory {
    @Nonnull
    @Override
    public Condition fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String configuration = JsonUtilities.getString(object, "configuration");
        final String category = JsonUtilities.getString(object, "category");
        final String name = JsonUtilities.getString(object, "name");
        final boolean value = JsonUtilities.getBooleanOrElse(object, "value", () -> true);

        final Configuration targetConfiguration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(configuration);
        final boolean configurationValue = targetConfiguration.get(category, name, value).getBoolean();

        return new BasicCondition(configurationValue == value);
    }
}
