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
        public static final String PROPERTY_FOOLERY_BRANDING_TIME_COMMENT = "Mods loaded... mods active... but how many are documented?";

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
    @SuppressWarnings("unused") public static final String JAR_FINGERPRINT = "@FINGERPRINT@";

    private Constants() {}
}
