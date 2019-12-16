package com.aaronhowser1.dymm.common.configuration;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.ApiBindings;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class OldConfigurationMigrationTool {
    private static final class MigrationMapping {
        private final String oldCategory;
        private final String oldName;
        private final String newFileName;
        private final String newCategory;
        private final String newName;

        private MigrationMapping(@Nonnull final String oldCategory, @Nonnull final String oldName, @Nonnull final String newFileName,
                                 @Nonnull final String newCategory, @Nonnull final String newName) {
            this.oldCategory = oldCategory;
            this.oldName = oldName;
            this.newFileName = newFileName;
            this.newCategory = newCategory;
            this.newName = newName;
        }
    }

    private static final List<MigrationMapping> MIGRATION_MAPPINGS = new ArrayList<>();
    private static final L LOG = L.create(Constants.MOD_NAME, "Configuration Migration");

    private OldConfigurationMigrationTool() {}

    static {
        final String mFile = Constants.CONFIGURATION_MAIN;
        final String tFile = Constants.CONFIGURATION_TARGETS;

        final String gCat = "general";
        final String dCat = Constants.CONFIGURATION_MAIN_DEBUG_CATEGORY;
        final String tCat = Constants.CONFIGURATION_TARGETS_MAIN_CATEGORY;

        MIGRATION_MAPPINGS.add(new MigrationMapping(gCat, "Debug - Mod Documented", mFile, dCat, "target_documented"));
        MIGRATION_MAPPINGS.add(new MigrationMapping(gCat, "Debug - Not Documented Items", mFile, dCat, "missing_entries"));
        MIGRATION_MAPPINGS.add(new MigrationMapping(gCat, "Iron Chest info", tFile, tCat, "ironchest"));
        MIGRATION_MAPPINGS.add(new MigrationMapping(gCat, "Nature's Compass info", tFile, tCat, "naturescompass"));
        MIGRATION_MAPPINGS.add(new MigrationMapping(gCat, "Refined Storage info", tFile, tCat, "refinedstorage"));
        MIGRATION_MAPPINGS.add(new MigrationMapping(gCat, "Tinkers' Construct info", tFile, tCat, "tconstruct"));
        MIGRATION_MAPPINGS.add(new MigrationMapping(gCat, "Twilight Forest info", tFile, tCat, "twilightforest"));
        MIGRATION_MAPPINGS.add(new MigrationMapping(gCat, "Waystones info", tFile, tCat, "waystones"));
    }

    public static void attemptToMigrateConfiguration() {
        final Path oldConfigurationPath = Loader.instance().getConfigDir().toPath().resolve("./dym.cfg").normalize().toAbsolutePath();
        if (!Files.exists(oldConfigurationPath)) {
            LOG.info("No old configuration to migrate was found");
        }
        doConfigurationMigration(oldConfigurationPath);
    }

    private static void doConfigurationMigration(@Nonnull final Path oldConfiguration) {
        LOG.bigWarn("An old configuration file was found inside your configuration directory!\n" +
                "This file will now be migrated to the new location and all settings (if possible), will be carried over\n" +
                "Note that this process will only be done once and then the old configuration will be deleted!\n\n" +
                "The new configuration has been migrated to its own directory and split for ease of editing");

        final Configuration old = new Configuration(oldConfiguration.toFile());
        old.load();

        old.getCategoryNames()
                .stream()
                .map(old::getCategory)
                .forEach(category -> {
                    final String categoryName = category.getQualifiedName();
                    category.forEach((name, property) -> {
                        final Optional<MigrationMapping> mapping = MIGRATION_MAPPINGS.stream()
                                .filter(it -> it.oldCategory.equals(categoryName) && it.oldName.equals(name))
                                .findFirst();
                        if (!mapping.isPresent()) {
                            LOG.warn("Unable to find a mapping for the entry '" + categoryName + '.' + name + ": this entry will not be migrated!");
                            return;
                        }
                        final MigrationMapping map = mapping.get();
                        final Configuration newConfiguration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(map.newFileName);
                        final ConfigCategory newCategory = newConfiguration.getCategory(map.newCategory);
                        final Property newProperty = newCategory.get(map.newName);
                        if (newProperty != null) {
                            newProperty.set(property.getBoolean());
                        } else {
                            newConfiguration.get(map.newCategory, map.newName, property.getBoolean(), property.getComment());
                        }
                        newConfiguration.save();
                    });
                });

        LOG.info("Migration process completed successfully: the old file will now be deleted");
        // not really: we're just going to move it
        if (true) return; // TODO Remove
        final Path newLocation = Loader.instance().getConfigDir().toPath().resolve("./" + Constants.MOD_ID + "/_migration.backup");
        if (Files.exists(newLocation)) {
            try {
                Files.delete(newLocation);
            } catch (@Nonnull final IOException ignored) {}
        }
        try {
            Files.move(oldConfiguration, newLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (@Nonnull final IOException e) {
            LOG.warn("An error has occurred while operating on the old file", e);
        }
    }
}
