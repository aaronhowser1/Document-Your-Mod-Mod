package com.aaronhowser1.dymm;

@SuppressWarnings("WeakerAccess")
public final class Constants {
    public static final class ConfigurationMain {
        public static final String NAME = "_main";

        public static final String CATEGORY_DEBUG = "debug";
        public static final String CATEGORY_DEBUG_COMMENT = "Set of options useful for developers: you generally want these to be set to false";

        public static final String PROPERTY_DEBUG_TARGET_DOCUMENTED = "target_documented";
        public static final String PROPERTY_DEBUG_TARGET_DOCUMENTED_COMMENT = "Shows in the game console which targets have been documented";
        public static final String PROPERTY_DEBUG_MISSING_ENTRIES = "missing_entries";
        public static final String PROPERTY_DEBUG_MISSING_ENTRIES_COMMENT = "Automatically scans the targets and displays in the console which items haven't been documented yet. Only works with targets that have at least one documented entry";
        public static final String PROPERTY_DEBUG_TARGET_ENTRIES = "target_entries";
        public static final String PROPERTY_DEBUG_TARGET_ENTRIES_COMMENT = "Shows on the target tooltip which entries are responsible for its documentation";

        public static final String CATEGORY_FOOLERY = "foolery";
        public static final String CATEGORY_FOOLERY_COMMENT = "Some random features that aren't part of the mod, but may be fun to enable and play around with nonetheless";

        public static final String PROPERTY_FOOLERY_BRANDING_TIME = "branding_time";
        public static final String PROPERTY_FOOLERY_BRANDING_TIME_COMMENT = "Mods loaded... mods active... but how many are documented?\n" +
                "Adds the amount of documented mods to the mod count in the bottom left of the main menu.\n" +
                "Example: \"15 mods loaded, 15 mods active, 10 mods documented\"";

        public static final String CATEGORY_PERFORMANCE = "performance";
        public static final String CATEGORY_PERFORMANCE_COMMENT = "Allows you to configure some mod behavior, allowing you to prioritize speed over efficiency or vice versa";

        public static final String PROPERTY_PERFORMANCE_RAM_SAVING = "ram_saving";
        public static final String PROPERTY_PERFORMANCE_RAM_SAVING_COMMENT = "Makes the mod run in RAM-light mode, making it allocate as few resources as possible.\n" +
                "WARNING! This WILL exponentially decrease performance up to 70%! Use this option if you REALLY need to spare as much RAM as possible!\n" +
                "In most cases this mod can automatically regulate itself, so turn this on ONLY if you've already tried everything! Do NOT come to us for huge slowdowns";

        private ConfigurationMain() {}
    }

    public static final class ConfigurationTargets {
        public static final String NAME = "_targets";

        public static final String CATEGORY_TARGETS = "targets";
        public static final String CATEGORY_TARGETS_COMMENT = "Set here which targets should be enabled or not.\nNote that not all targets present here may also have entries in game.\n" +
                "NOTE: Certain targets may require additional configuration";

        private ConfigurationTargets() {}
    }

    public static final String MOD_ID = "dymm";
    public static final String MOD_NAME = "Document Your Mod Mod";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_DEPENDENCIES = "required-after:jei@[1.12.2-4.15.0.268,)";
    public static final String MOD_GUI_FACTORY = "com.aaronhowser1.dymm.client.ConfigurationGuiFactory";
    public static final String MOD_UPDATE_URL = "https://raw.githubusercontent.com/aaronhowser1/Document-Your-Mod-Mod/master/promo-slim.json";
    @SuppressWarnings("unused") public static final String JAR_FINGERPRINT = "@FINGERPRINT@";

    private Constants() {}
}
