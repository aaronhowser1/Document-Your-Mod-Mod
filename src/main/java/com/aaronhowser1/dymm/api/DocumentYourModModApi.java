package com.aaronhowser1.dymm.api;

import com.aaronhowser1.dymm.api.configuration.ConfigurationManager;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DocumentYourModModApi {
    @Nonnull ConfigurationManager getConfigurationManager();
    @Nullable GlobalLoadingState getCurrentLoadingState();
    @Nonnull IForgeRegistry<DocumentationEntry> getDocumentationRegistry();
}
