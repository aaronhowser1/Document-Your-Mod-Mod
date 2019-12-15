package com.aaronhowser1.dym.common.loading;

import com.aaronhowser1.dym.Constants;
import com.aaronhowser1.dym.L;
import com.aaronhowser1.dym.api.documentation.DocumentationEntry;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Unit;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.Loader;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.LoaderBuilder;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.Processor;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.context.BaseContextBuilder;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.filter.JsonFileFilter;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.filter.RegularFileFilter;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.filter.SpecialFileFilter;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.locator.ModContainerLocator;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.locator.OneForAllModContainerLocator;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.locator.ResourcesDirectoryLocator;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.naming.DefaultIdentifierBuilder;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.preprocessor.CatchingPreprocessor;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.preprocessor.JsonConverterPreprocessor;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.processor.CatchingProcessor;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.progress.ActiveModContainerVisitor;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader.progress.ProgressBarVisitor;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public final class LoadingHandler {
    private static final L LOG = L.create(Constants.MOD_NAME, "Documentation Loader");

    private static final Loader LOADER;

    private static IForgeRegistry<DocumentationEntry> registry;

    private LoadingHandler() {}

    static {
        final ModContainer thisModContainer = net.minecraftforge.fml.common.Loader.instance()
                .getActiveModList()
                .stream()
                .filter(it -> Constants.MOD_ID.equals(it.getModId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Our mod container not found"));

        LOADER = LoaderBuilder.loader()
                .name("Documentation Loader")
                .progressVisitor(new ProgressBarVisitor().chain(ActiveModContainerVisitor.create()))
                .contextBuilder(BaseContextBuilder.create())
                .identifierBuilder(DefaultIdentifierBuilder.create(true))
                .locators(LoaderBuilder.LoaderLocators.create()
                        .locator(() -> ModContainerLocator.create("documentation", ModContainerLocator.Kind.ASSETS))
                        .locator(() -> OneForAllModContainerLocator.create(thisModContainer, "documentation", OneForAllModContainerLocator.Kind.ASSETS))
                        .locator(() -> ResourcesDirectoryLocator.create("documentation", ResourcesDirectoryLocator.Kind.ASSETS))
                )
                .phases(LoaderBuilder.LoaderPhases.create()
                        .phase(LoaderBuilder.LoadingPhaseBuilder.createWithName("Ensuring schema cleanliness")
                                .filters(LoaderBuilder.LoadingPhaseFilters.create()
                                        .filter(() -> SpecialFileFilter.create(SpecialFileFilter.Kind.JSON_SCHEMA))
                                )
                                .processor(new Processor<Object>() {
                                    @Nonnull
                                    @Override
                                    public Unit process(@Nonnull final Object content, @Nonnull final NameSpacedString identifier,
                                                        @Nonnull final Nullable<Context> globalContext, @Nonnull final Nullable<Context> phaseContext) {
                                        throw new IllegalStateException("File name 'pattern.json' is invalid.\nThat name is reserved in JSON and has a special meaning " +
                                                "that does not apply to this case.\nPlease remove or rename the invalid file.\nID of the broken entry: " + identifier);
                                    }
                                })
                        )
                        .phase(LoaderBuilder.LoadingPhaseBuilder.createWithName("Loading JSON factories")
                                .filters(LoaderBuilder.LoadingPhaseFilters.create()
                                        .filter(RegularFileFilter::create)
                                        .filter(JsonFileFilter::create)
                                        .filter(() -> SpecialFileFilter.create(SpecialFileFilter.Kind.FACTORIES))
                                )
                                .preprocessor(JsonConverterPreprocessor.create())
                                .processor(CatchingProcessor.create(LOG, DocumentationLoadingProcessor.withFlag(1)))
                        )
                        .phase(LoaderBuilder.LoadingPhaseBuilder.createWithName("Reading metadata")
                                .filters(LoaderBuilder.LoadingPhaseFilters.create()
                                        .filter(RegularFileFilter::create)
                                        .filter(JsonFileFilter::create)
                                        .filter(() -> SpecialFileFilter.create(SpecialFileFilter.Kind.UNDERSCORE_PREFIX))
                                        .filter(() -> SpecialFileFilter.create(SpecialFileFilter.Kind.FACTORIES, true))
                                )
                                .preprocessor(JsonConverterPreprocessor.create())
                                .processor(CatchingProcessor.create(LOG, DocumentationLoadingProcessor.withFlag(2)))
                        )
                        .phase(LoaderBuilder.LoadingPhaseBuilder.createWithName("Loading documentation")
                                .filters(LoaderBuilder.LoadingPhaseFilters.create()
                                        .filter(RegularFileFilter::create)
                                        .filter(JsonFileFilter::create)
                                        .filter(() -> SpecialFileFilter.create(SpecialFileFilter.Kind.UNDERSCORE_PREFIX, true))
                                )
                                .preprocessor(CatchingPreprocessor.create(LOG, JsonConverterPreprocessor.create()))
                                .processor(CatchingProcessor.create(LOG, DocumentationLoadingProcessor.withFlag(4)))
                        )
                )
                .build();
    }

    public static void bindRegistry(@Nonnull final IForgeRegistry<DocumentationEntry> bindRegistry) {
        LOG.info("Attempting to bind the documentation registry to " + bindRegistry);
        registry = bindRegistry;
    }

    public static void performLoading() {
        // Boson already adds this bar, so we are not going to do it ourselves if it is loaded
        final ProgressManager.ProgressBar bar;
        if (!net.minecraftforge.fml.common.Loader.isModLoaded("boson")) {
            bar = ProgressManager.push(Constants.MOD_NAME, 1);
            bar.step("RegistryEvent.Register");
        } else {
            bar = null;
        }

        LOG.info("Preparing to load documentation data into registry");
        LOADER.load();
        LOG.info("Unbinding registry and state");
        registry = null;
        LoadingState.destroyCurrent();
        LOG.info("Loading has completed");

        if (bar != null) ProgressManager.pop(bar);
    }

    @Nonnull
    static IForgeRegistry<DocumentationEntry> getRegistry() {
        return registry;
    }
}
