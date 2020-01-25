package com.aaronhowser1.dymm.common.configuration;

import com.aaronhowser1.dymm.Constants;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class ConfigurationManager implements com.aaronhowser1.dymm.api.configuration.ConfigurationManager {
    private static final class InstanceHolder {
        private static final ConfigurationManager INSTANCE = new ConfigurationManager();
    }

    private static final Map<String, Configuration> CONFIGURATION_CACHE = new HashMap<>();

    private ConfigurationManager() {}

    @Nonnull
    public static ConfigurationManager get() {
        return InstanceHolder.INSTANCE;
    }

    @Nonnull
    @Override
    public final Configuration getVersionedConfigurationFor(@Nonnull final String name, @Nonnull final String version) {
        return CONFIGURATION_CACHE.computeIfAbsent(name, it -> {
            final Configuration configuration = new Configuration(this.getFileFor(name), version);
            configuration.load();
            configuration.save();
            return configuration;
        });
    }

    @Nonnull
    @Override
    public final Configuration getConfigurationFor(@Nonnull final String name) {
        return this.getVersionedConfigurationFor(name, "1");
    }

    @Nonnull
    private File getFileFor(@Nonnull final String name) {
        try {
            return this.getPathFor(name).toFile().getCanonicalFile();
        } catch (@Nonnull final IOException e) {
            throw new UnsupportedOperationException("An error has occurred while attempting to construct the configuration for '" + name + "'", e);
        }
    }

    @Nonnull
    private Path getPathFor(@Nonnull final String name) {
        final Path mainConfigurationDirectory = Loader.instance().getConfigDir().toPath();
        final Path documentConfigurationDirectory = mainConfigurationDirectory.resolve("./" + Constants.MOD_ID);
        return documentConfigurationDirectory.resolve("./" + name + ".cfg").normalize().toAbsolutePath();
    }
}
