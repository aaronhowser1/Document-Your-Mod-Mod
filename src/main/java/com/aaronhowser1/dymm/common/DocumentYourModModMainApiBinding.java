package com.aaronhowser1.dymm.common;

import com.aaronhowser1.dymm.api.DocumentYourModModApi;
import com.aaronhowser1.dymm.api.configuration.ConfigurationManager;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.common.loading.LoadingState;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DocumentYourModModMainApiBinding implements DocumentYourModModApi {
    @Nonnull
    @Override
    public ConfigurationManager getConfigurationManager() {
        return com.aaronhowser1.dymm.common.configuration.ConfigurationManager.get();
    }

    @Nullable
    @Override
    public GlobalLoadingState getCurrentLoadingState() {
        return LoadingState.obtainState();
    }

    @Nonnull
    @Override
    public IForgeRegistry<DocumentationEntry> getDocumentationRegistry() {
        final IForgeRegistry<DocumentationEntry> target = RegistrationHandler.documentationRegistry;
        if (target == null) throw new IllegalStateException("Registry wasn't created yet");
        return target;
    }
}
