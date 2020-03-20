package com.aaronhowser1.dymm.common.configuration;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.api.ApiBindings;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public final class MainConfigurationHandler {
    private MainConfigurationHandler() {}

    public static void initializeMainModConfiguration() {
        initializeMainConfiguration();
        initializeModsConfiguration();
    }

    private static void initializeMainConfiguration() {
        final Configuration configuration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(Constants.ConfigurationMain.NAME);

        final ConfigCategory debug = configuration.getCategory(Constants.ConfigurationMain.CATEGORY_DEBUG);
        debug.setComment(Constants.ConfigurationMain.CATEGORY_DEBUG_COMMENT);

        configuration.get(Constants.ConfigurationMain.CATEGORY_DEBUG, Constants.ConfigurationMain.PROPERTY_DEBUG_TARGET_DOCUMENTED,
                false, Constants.ConfigurationMain.PROPERTY_DEBUG_TARGET_DOCUMENTED_COMMENT);
        configuration.get(Constants.ConfigurationMain.CATEGORY_DEBUG, Constants.ConfigurationMain.PROPERTY_DEBUG_MISSING_ENTRIES,
                false,Constants.ConfigurationMain.PROPERTY_DEBUG_MISSING_ENTRIES_COMMENT);
        configuration.get(Constants.ConfigurationMain.CATEGORY_DEBUG, Constants.ConfigurationMain.PROPERTY_DEBUG_TARGET_ENTRIES,
                false, Constants.ConfigurationMain.PROPERTY_DEBUG_TARGET_ENTRIES_COMMENT);

        final ConfigCategory foolery = configuration.getCategory(Constants.ConfigurationMain.CATEGORY_FOOLERY);
        foolery.setComment(Constants.ConfigurationMain.CATEGORY_FOOLERY_COMMENT);

        configuration.get(Constants.ConfigurationMain.CATEGORY_FOOLERY, Constants.ConfigurationMain.PROPERTY_FOOLERY_BRANDING_TIME,
                false, Constants.ConfigurationMain.PROPERTY_FOOLERY_BRANDING_TIME_COMMENT);

        final ConfigCategory performance = configuration.getCategory(Constants.ConfigurationMain.CATEGORY_PERFORMANCE);
        performance.setComment(Constants.ConfigurationMain.CATEGORY_PERFORMANCE_COMMENT);

        configuration.get(Constants.ConfigurationMain.CATEGORY_PERFORMANCE, Constants.ConfigurationMain.PROPERTY_PERFORMANCE_RAM_SAVING,
                false, Constants.ConfigurationMain.PROPERTY_PERFORMANCE_RAM_SAVING_COMMENT).setRequiresMcRestart(true);

        configuration.save();
    }

    private static void initializeModsConfiguration() {
        final Configuration configuration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(Constants.ConfigurationTargets.NAME);

        final ConfigCategory targets = configuration.getCategory(Constants.ConfigurationTargets.CATEGORY_TARGETS);
        targets.setComment(Constants.ConfigurationTargets.CATEGORY_TARGETS_COMMENT);

        Loader.instance().getModList().forEach(it -> configuration.get(Constants.ConfigurationTargets.CATEGORY_TARGETS, it.getModId(), true, it.getName()));
        configuration.save();
    }
}
