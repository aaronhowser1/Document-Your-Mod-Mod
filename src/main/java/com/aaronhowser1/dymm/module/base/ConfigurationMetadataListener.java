package com.aaronhowser1.dymm.module.base;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.loading.metadata.MetadataListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;

public final class ConfigurationMetadataListener implements MetadataListener {

    /*
     * Example JSON:
     *
     * {
     *   "version": "1",
     *   "categories": {
     *     "category_name": {
     *       "comment": "This is just a comment",
     *       "language_key": "category.language.key",
     *       "properties": {
     *         "property_one": {
     *           "default": true,
     *           "comment": "A comment",
     *           "language_key": "a.language.key",
     *           "requires_restart": false
     *         },
     *         "property_two": {
     *           "default": false,
     *           "comment": "Another comment",
     *           "language_key": "something.else"
     *         }
     *       }
     *     },
     *     "category_two": {
     *       "comment": "This comment is empty"
     *     }
     *   },
     *   "migration_instructions": {
     *     "not_yet_supported": {}
     *   }
     * }
     */

    private static final L LOG = L.create(Constants.MOD_NAME, "Configuration Metadata Processor");

    ConfigurationMetadataListener() {}

    @Override
    public void processMetadata(@Nonnull final JsonObject object, @Nonnull final String nameSpace) {
        LOG.info("Attempting to process _configuration metadata file for namespace '" + nameSpace + "'");
        try {
            final String version = JsonUtilities.getStringOrElse(object, "version", () -> "1");
            LOG.debug("Attempting to load and/or create configuration file with version '" + version + "'");
            final Configuration configuration = ApiBindings.getMainApi().getConfigurationManager().getVersionedConfigurationFor(nameSpace, version);
            if (!this.versionMatches(configuration.getLoadedConfigVersion(), version)) {
                LOG.warn("Attempted to load a configuration version that does not match: found '" + configuration.getLoadedConfigVersion() + "', but expected '" + version + "'");
                this.migrateConfiguration(configuration, object);
            }
            this.populateConfigurationObjectWith(configuration, object);
            configuration.save();
            LOG.debug("Configuration loaded, created, and saved correctly");
        } catch (@Nonnull final Exception e) {
            throw new JsonSyntaxException("The given _configuration.json file for namespace '" + nameSpace + "' does not respect the correct structure", e);
        }
    }

    private boolean versionMatches(@Nonnull final String found, @Nonnull final String expected) {
        return expected.equals(found);
    }

    private void migrateConfiguration(@Nonnull @SuppressWarnings("unused") final Configuration configuration, @Nonnull @SuppressWarnings("unused") final JsonObject object) {
        if (!object.has("migration_instructions")) {
            LOG.bigError("No migration instruction provided: the configuration will not be converted to the new version!", L.DumpStackBehavior.DO_NOT_DUMP);
            return;
        }
        LOG.bigWarn("Configuration migration not yet supported", L.DumpStackBehavior.DO_NOT_DUMP);
    }

    private void populateConfigurationObjectWith(@Nonnull final Configuration configuration, @Nonnull final JsonObject object) {
        if (!object.has("categories")) return; // There isn't a lot to do if there are no categories to add
        JsonUtilities.getJsonObject(object, "categories").entrySet().forEach(it -> {
            final String categoryName = it.getKey();
            final JsonObject categoryDescription = JsonUtilities.asJsonObject(it.getValue(), it.getKey());
            this.setupCategory(categoryName, categoryDescription, configuration);
            this.setupProperties(categoryName, configuration, JsonUtilities.getJsonObjectOrElse(categoryDescription, "properties", JsonObject::new));
        });
    }

    private void setupCategory(@Nonnull final String name, @Nonnull final JsonObject description, @Nonnull final Configuration configuration) {
        final ConfigCategory category = configuration.getCategory(name);
        if (description.has("comment")) category.setComment(JsonUtilities.getString(description, "comment"));
        if (description.has("language_key")) category.setLanguageKey(JsonUtilities.getString(description, "language_key"));
    }

    private void setupProperties(@Nonnull final String mainCategory, @Nonnull final Configuration configuration, @Nonnull final JsonObject properties) {
        if (properties.entrySet().isEmpty()) {
            LOG.warn("Category '" + mainCategory + "' has no properties specified: while not illegal this is generally useless; the entry will be skipped");
            return;
        }
        properties.entrySet().forEach(it -> {
            final String propertyName = it.getKey();
            final JsonObject propertyDescription = JsonUtilities.asJsonObject(it.getValue(), it.getKey());
            this.setupProperty(mainCategory, propertyName, propertyDescription, configuration);
        });
    }

    private void setupProperty(@Nonnull final String category, @Nonnull final String name, @Nonnull final JsonObject description, @Nonnull final Configuration configuration) {
        final boolean def = JsonUtilities.getBooleanOrElse(description, "default", () -> { throw new JsonParseException("Only boolean values are supported in the metadata file"); });
        final Property property = configuration.get(category, name, def);
        if (description.has("comment")) property.setComment(JsonUtilities.getString(description, "comment"));
        if (description.has("language_key")) property.setLanguageKey(JsonUtilities.getString(description, "language_key"));
        property.setRequiresMcRestart(JsonUtilities.getBooleanOrElse(description, "requires_restart", () -> true));
    }
}
