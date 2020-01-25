package com.aaronhowser1.dymm.api.configuration;

import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;

public interface ConfigurationManager {
    @Nonnull Configuration getVersionedConfigurationFor(@Nonnull final String name, @Nonnull final String version);
    @Nonnull Configuration getConfigurationFor(@Nonnull final String name);
}
