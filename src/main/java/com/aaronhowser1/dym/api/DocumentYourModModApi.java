package com.aaronhowser1.dym.api;

import com.aaronhowser1.dym.api.configuration.ConfigurationManager;
import com.aaronhowser1.dym.api.loading.GlobalLoadingState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DocumentYourModModApi {
    @Nonnull ConfigurationManager getConfigurationManager();
    @Nullable GlobalLoadingState getCurrentLoadingState();
}
