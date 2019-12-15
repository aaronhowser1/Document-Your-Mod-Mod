package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction0;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.ApiBindings;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class LoaderBuilder {
    public static final class LoaderLocators extends ArrayList<Locator> {
        private LoaderLocators() {}

        @Nonnull
        public static LoaderLocators create() {
            return new LoaderLocators();
        }

        @Nonnull
        public LoaderLocators locator(@Nonnull final KFunction0<Locator> locatorSupplier) {
            this.add(Objects.requireNonNull(Objects.requireNonNull(locatorSupplier).invoke()));
            return this;
        }
    }

    public static final class LoaderPhases extends ArrayList<LoadingPhaseBuilder> {
        private LoaderPhases() {}

        @Nonnull
        public static LoaderPhases create() {
            return new LoaderPhases();
        }

        @Nonnull
        public LoaderPhases phase(@Nonnull final LoadingPhaseBuilder builder) {
            this.add(Objects.requireNonNull(builder));
            return this;
        }
    }

    public static final class LoadingPhaseBuilder {
        private final String name;
        private final List<Filter> filters = new ArrayList<>();

        private Nullable<ContextBuilder> contextBuilder = Nullable.get(null);
        private Nullable<Preprocessor<String, ?>> preprocessor = Nullable.get(null);
        private Processor<?> processor;

        private LoadingPhaseBuilder(@Nonnull final String name) {
            this.name = Objects.requireNonNull(name);
        }

        @Nonnull
        public static LoadingPhaseBuilder createWithName(@Nonnull final String name) {
            return new LoadingPhaseBuilder(name);
        }

        @Nonnull
        public LoadingPhaseBuilder contextBuilder(@Nonnull final ContextBuilder builder) {
            this.contextBuilder = Nullable.get(Objects.requireNonNull(builder));
            return this;
        }

        @Nonnull
        public LoadingPhaseBuilder preprocessor(@Nonnull final Preprocessor<String, ?> preprocessor) {
            this.preprocessor = Nullable.get(Objects.requireNonNull(preprocessor));
            return this;
        }

        @Nonnull
        public LoadingPhaseBuilder processor(@Nonnull final Processor<?> processor) {
            this.processor = Objects.requireNonNull(processor);
            return this;
        }

        @Nonnull
        public LoadingPhaseBuilder filters(@Nonnull final LoadingPhaseFilters filters) {
            this.filters.addAll(filters);
            return this;
        }

        @Nonnull
        public String name() {
            return this.name;
        }

        @Nonnull
        public Nullable<ContextBuilder> contextBuilder() {
            return this.contextBuilder;
        }

        @Nonnull
        public Nullable<Preprocessor<String, ?>> preprocessor() {
            return this.preprocessor;
        }

        @Nonnull
        public Processor<?> processor() {
            return this.processor;
        }

        @Nonnull
        public List<Filter> filters() {
            return new ArrayList<>(this.filters);
        }
    }

    public static final class LoadingPhaseFilters extends ArrayList<Filter> {
        private LoadingPhaseFilters() {}

        @Nonnull
        public static LoadingPhaseFilters create() {
            return new LoadingPhaseFilters();
        }

        @Nonnull
        public LoadingPhaseFilters filter(@Nonnull final KFunction0<Filter> supplier) {
            this.add(Objects.requireNonNull(Objects.requireNonNull(supplier).invoke()));
            return this;
        }
    }

    private final List<Locator> locators = new ArrayList<>();
    private final List<LoadingPhaseBuilder> phases = new ArrayList<>();

    private IdentifierBuilder identifierBuilder;
    private Nullable<ContextBuilder> contextBuilder = Nullable.get(null);
    private Nullable<String> name = Nullable.get(null);
    private Nullable<ProgressVisitor> progressVisitor = Nullable.get(null);

    private LoaderBuilder() {}

    @Nonnull
    public static LoaderBuilder loader() {
        return new LoaderBuilder();
    }

    @Nonnull
    public LoaderBuilder identifierBuilder(@Nonnull final IdentifierBuilder identifierBuilder) {
        this.identifierBuilder = Objects.requireNonNull(identifierBuilder);
        return this;
    }

    @Nonnull
    public LoaderBuilder contextBuilder(@Nonnull final ContextBuilder contextBuilder) {
        this.contextBuilder = Nullable.get(Objects.requireNonNull(contextBuilder));
        return this;
    }

    @Nonnull
    public LoaderBuilder name(@Nonnull final String name) {
        this.name = Nullable.get(Objects.requireNonNull(name));
        return this;
    }

    @Nonnull
    public LoaderBuilder progressVisitor(@Nonnull final ProgressVisitor progressVisitor) {
        this.progressVisitor = Nullable.get(Objects.requireNonNull(progressVisitor));
        return this;
    }

    @Nonnull
    public LoaderBuilder locators(@Nonnull final LoaderLocators locators) {
        this.locators.addAll(locators);
        return this;
    }

    @Nonnull
    public LoaderBuilder phases(@Nonnull final LoaderPhases phases) {
        this.phases.addAll(phases);
        return this;
    }

    @Nonnull
    public IdentifierBuilder identifierBuilder() {
        return this.identifierBuilder;
    }

    @Nonnull
    public Nullable<ContextBuilder> contextBuilder() {
        return this.contextBuilder;
    }

    @Nonnull
    public Nullable<String> name() {
        return this.name;
    }

    @Nonnull
    public Nullable<ProgressVisitor> progressVisitor() {
        return this.progressVisitor;
    }

    @Nonnull
    public List<Locator> locators() {
        return new ArrayList<>(this.locators);
    }

    @Nonnull
    public List<LoadingPhaseBuilder> phases() {
        return new ArrayList<>(this.phases);
    }

    @Nonnull
    public Loader build() {
        return ApiBindings.BOSON_API.invoke().buildLoader(this);
    }
}
