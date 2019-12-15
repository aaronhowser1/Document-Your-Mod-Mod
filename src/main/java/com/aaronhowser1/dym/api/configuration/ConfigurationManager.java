package com.aaronhowser1.dym.api.configuration;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

public interface ConfigurationManager {
    @Nonnull Configuration getVersionedConfigurationFor(@Nonnull final String name, @Nonnull final String version);
    @Nonnull Configuration getConfigurationFor(@Nonnull final String name);
}
