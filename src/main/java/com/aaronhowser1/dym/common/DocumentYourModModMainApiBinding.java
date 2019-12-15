package com.aaronhowser1.dym.common;

import com.aaronhowser1.dym.api.DocumentYourModModApi;
import com.aaronhowser1.dym.api.configuration.ConfigurationManager;
import com.aaronhowser1.dym.api.loading.GlobalLoadingState;
import com.aaronhowser1.dym.common.loading.LoadingState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DocumentYourModModMainApiBinding implements DocumentYourModModApi {
    @Nonnull
    @Override
    public ConfigurationManager getConfigurationManager() {
        return com.aaronhowser1.dym.common.configuration.ConfigurationManager.get();
    }

    @Nullable
    @Override
    public GlobalLoadingState getCurrentLoadingState() {
        return LoadingState.obtainState();
    }
}
