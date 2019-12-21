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
        final Configuration configuration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(Constants.CONFIGURATION_MAIN);
        final ConfigCategory debug = configuration.getCategory(Constants.CONFIGURATION_MAIN_DEBUG_CATEGORY);
        debug.setComment("Set of options useful for developers: you generally want these to be set to false");
        configuration.get(Constants.CONFIGURATION_MAIN_DEBUG_CATEGORY, "target_documented", false, "Shows in the game console which targets have been documented");
        configuration.get(Constants.CONFIGURATION_MAIN_DEBUG_CATEGORY, "missing_entries", false,
                "Automatically scans the targets and displays in the console which items haven't been documented yet. Only works with targets that have at least one documented entry");
        configuration.get(Constants.CONFIGURATION_MAIN_DEBUG_CATEGORY, "target_entries", false,
                "Shows on the target tooltip which entries are responsible for its documentation");
        final ConfigCategory foolery = configuration.getCategory(Constants.CONFIGURATION_MAIN_FOOLERY_CATEGORY);
        foolery.setComment("Some random features that aren't part of the mod, but may be fun to enable nonetheless");
        configuration.get(Constants.CONFIGURATION_MAIN_FOOLERY_CATEGORY, "branding_time", false, "Mods loaded... mods active... but how many are documented?");
        configuration.save();
    }

    private static void initializeModsConfiguration() {
        final Configuration configuration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(Constants.CONFIGURATION_TARGETS);
        final ConfigCategory targets = configuration.getCategory(Constants.CONFIGURATION_TARGETS_MAIN_CATEGORY);
        targets.setComment("Set here which targets should be enabled or not.\nNote that not all targets present here may also have entries in game.\n" +
                "NOTE: Certain targets may require additional configuration");
        Loader.instance().getModList().forEach(it -> configuration.get(Constants.CONFIGURATION_TARGETS_MAIN_CATEGORY, it.getModId(), true, it.getName()));
        configuration.save();
    }
}
