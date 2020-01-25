package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.implementation.loader;

import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.CheckedException;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Lazy;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Unit;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.With;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.ElvisExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.IfExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.TryExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.WhenExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction0;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction1;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction2;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.ContextBuilder;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Filter;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.IdentifierBuilder;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Loader;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.LoaderBuilder;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.LoadingPhase;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Location;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Locator;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Preprocessor;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Processor;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.ProgressVisitor;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BosonLoader implements Loader {
    private static final class BosonLoadingPhase<T> implements LoadingPhase<T> {
        private final String name;
        private final List<Filter> filters;
        private final Nullable<ContextBuilder> contextBuilder;
        private final Nullable<Preprocessor<String, T>> preprocessor;
        private final Processor<T> processor;

        @SuppressWarnings("unchecked")
        private BosonLoadingPhase(@Nonnull final LoaderBuilder.LoadingPhaseBuilder builder) {
            this.name = Objects.requireNonNull(builder.name());
            this.filters = Objects.requireNonNull(builder.filters());
            this.contextBuilder = Objects.requireNonNull(builder.contextBuilder());
            this.preprocessor = Nullable.get((Preprocessor<String, T>) Objects.requireNonNull(builder.preprocessor()).unwrap());
            this.processor = (Processor<T>) Objects.requireNonNull(builder.processor());
        }

        @Nonnull
        @Override
        public String getName() {
            return this.name;
        }

        @Nonnull
        @Override
        public List<Filter> getFilters() {
            return this.filters;
        }

        @Nonnull
        @Override
        public Nullable<ContextBuilder> getContextBuilder() {
            return this.contextBuilder;
        }

        @Nonnull
        @Override
        public Nullable<Preprocessor<String, T>> getPreprocessor() {
            return this.preprocessor;
        }

        @Nonnull
        @Override
        public Processor<T> getProcessor() {
            return this.processor;
        }
    }

    private static final class LocationPathWrapper implements Location {
        private final Path path;
        private final Nullable<Context> additionalContext;

        private LocationPathWrapper(@Nonnull final Path path, @Nonnull final Nullable<Context> additionalContext) {
            this.path = Objects.requireNonNull(path);
            this.additionalContext = Objects.requireNonNull(additionalContext);
        }

        @Nonnull
        @Override
        public Path getPath() {
            return this.path;
        }

        @Nonnull
        @Override
        public Nullable<String> getFriendlyName() {
            return Nullable.get(null);
        }

        @Nonnull
        @Override
        public Nullable<Context> getAdditionalContext() {
            return this.additionalContext;
        }
    }

    private static final class LoaderException extends RuntimeException {
        LoaderException(@Nonnull final String message, @Nonnull final Throwable cause) {
            super(message, cause);
        }
    }

    private static final class PhaseException extends RuntimeException {
        PhaseException(@Nonnull final String message, @Nonnull final Throwable cause) {
            super(message, cause);
        }
    }

    private static final class ProcessingException extends RuntimeException {
        ProcessingException(@Nonnull final String message, @Nonnull final Throwable cause) {
            super(message, cause);
        }
    }

    // Technically it should be the shaded L, but since we are rewriting everything and L is already present in
    // this project, might as well do this.
    private final Lazy<L> l;

    private final String name;
    private final List<Locator> locators;
    private final Nullable<ContextBuilder> globalContextBuilder;
    private final IdentifierBuilder identifierBuilder;
    private final Nullable<ProgressVisitor> progressReporter;
    private final List<LoadingPhase<Object>> phases;

    private BosonLoader(@Nonnull final LoaderBuilder builder) {
        this.name = ElvisExpression.create(Objects.requireNonNull(Objects.requireNonNull(builder).name()), () -> Integer.toString(this.hashCode())).invoke();
        this.l = Lazy.lazy(() -> L.create("[DYMM Shade] Boson Loader", this.name));
        this.locators = checkNotEmpty(builder.locators(), () -> "You must specify at least one locator for the loader");
        this.globalContextBuilder = Objects.requireNonNull(builder.contextBuilder());
        this.identifierBuilder = Objects.requireNonNull(builder.identifierBuilder());
        this.progressReporter = Objects.requireNonNull(builder.progressVisitor());
        this.phases = checkNotEmpty(
                Objects.requireNonNull(builder.phases())
                        .stream()
                        .map(BosonLoadingPhase::new)
                        .collect(Collectors.toList()),
                () -> "Unable to create a loader without phases");

        this.l.invoke().info("Loader initialized, waiting for requests");
    }

    @Nonnull
    public static BosonLoader from(@Nonnull final LoaderBuilder builder) {
        return new BosonLoader(builder);
    }

    @Nonnull
    private static <T> List<T> checkNotEmpty(@Nonnull final List<T> t, @Nonnull final KFunction0<String> message) {
        return IfExpression.build(t.isEmpty(), () -> { throw new IllegalArgumentException(message.invoke()); }, () -> t).invoke();
    }

    @Nonnull
    @Override
    public Unit load() {
        return With.with(this.l.invoke(), $this$receiver -> {
            $this$receiver.info(String.format("Using loader %s, with %d locators and %d phases", this.name, this.locators.size(), this.phases.size()));
            $this$receiver.debug("Locators: " + this.locators);
            $this$receiver.debug("Identifier builder: " + this.identifierBuilder);
            $this$receiver.debug("Progress visitor: " + this.progressReporter);
            return TryExpression.create(
                    this::doLoading,
                    ImmutableList.of(TryExpression.CatchClause.create(
                            KClass.get(Exception.class),
                            it -> {
                                throw new LoaderException("An exception has occurred while attempting to load with loader '" + this.name + "'", it);
                            })
                    )
            ).invoke();
        });
    }

    @Nonnull
    private Unit doLoading() {
        return TryExpression.create(() -> {
            this.l.invoke().info("Loading process has started");
            this.progressReporter.ifPresent(ProgressVisitor::beginVisit);

            this.l.invoke().debug("Creating global context for Loader");
            final Nullable<Context> globalContext = this.globalContextBuilder.ifPresent(it -> it.buildContext(Nullable.get(null)));

            this.progressReporter.ifPresent(it -> it.visitPhases(this.phases.size()));
            forEachIndexed(this.phases, (index, phase) -> {
                this.l.invoke().info("Beginning phase " + index + ": " + phase.getName());
                TryExpression.create(
                        () -> this.attemptPhase(phase, globalContext),
                        ImmutableList.of(
                                TryExpression.CatchClause.create(KClass.get(Exception.class), e -> {
                                    throw new PhaseException("Unable to reach end of phase " + index + " '" + phase.getName() + "' cleanly due to an error", e);
                                })
                        )
                ).invoke();
                this.l.invoke().info("Reached end of phase " + phase.getName() + " successfully");
                return Unit.UNIT;
            });

            this.l.invoke().info("Cleaning up loader resources");
            this.locators.forEach(Locator::clean);

            this.l.invoke().info("Loading process completed");
            return ElvisExpression.create(this.progressReporter.ifPresent(ProgressVisitor::endVisit), () -> Unit.UNIT).invoke(); // Okay what?
        }, ImmutableList.of(
                TryExpression.CatchClause.create(KClass.get(Exception.class), it -> {
                    throw new LoaderException(ElvisExpression.create(Nullable.get(it.getMessage()), () -> "null").invoke(), it);
                })
        )).invoke();
    }

    @Nonnull
    private Unit attemptPhase(@Nonnull final LoadingPhase<Object> $this$receiver, @Nonnull final Nullable<Context> globalContext) {
        return TryExpression.create(
                () -> this.goThroughPhase($this$receiver, globalContext),
                ImmutableList.of(
                        TryExpression.CatchClause.create(KClass.get(Exception.class), e -> {
                            throw new PhaseException("An error has occurred while executing phase '" + $this$receiver.getName() + "'", e);
                        })
                )
        ).invoke();
    }

    @Nonnull
    private Unit goThroughPhase(@Nonnull final LoadingPhase<Object> $this$receiver, @Nonnull final Nullable<Context> globalContext) {
        this.progressReporter.ifPresent(it -> it.visitPhase($this$receiver));

        final List<Locator> locators = this.locators;
        final List<Lazy<Location>> itemsToLoad = locators.stream().flatMap(it -> it.getLocations().stream()).collect(Collectors.toList());
        this.l.invoke().debug("Attempting to load a total of " + itemsToLoad.size() + " items");
        this.progressReporter.ifPresent(it -> it.visitItemsTotal(itemsToLoad.size()));

        final Nullable<Context> phaseContext = $this$receiver.getContextBuilder().ifPresent(it -> it.buildContext(Nullable.get($this$receiver)));

        locators.forEach(it -> this.loadThroughLocator(it, $this$receiver, globalContext, phaseContext));
        return Unit.UNIT;
    }

    @Nonnull
    @SuppressWarnings("UnusedReturnValue")
    private Unit loadThroughLocator(@Nonnull final Locator $this$receiver, @Nonnull final LoadingPhase<Object> phase, @Nonnull final Nullable<Context> globalContext,
                                    @Nonnull final Nullable<Context> phaseContext) {
        this.l.invoke().debug("Attempting to load data through locator " + $this$receiver);
        final List<Lazy<Location>> locations = $this$receiver.getLocations();
        this.progressReporter.ifPresent(it -> it.visitItems(locations.size()));
        locations.forEach(it -> processLocation(it.invoke(), phase, globalContext, phaseContext));
        return Unit.UNIT;
    }

    @Nonnull
    @SuppressWarnings("UnusedReturnValue")
    private Unit processLocation(@Nonnull final Location $this$receiver, @Nonnull final LoadingPhase<Object> phase, @Nonnull final Nullable<Context> globalContext,
                                 @Nonnull final Nullable<Context> phaseContext) {
        this.progressReporter.ifPresent(it -> it.visitLocation($this$receiver, isDirectory($this$receiver)));
        return WhenExpression.create(
                ImmutableList.of(
                        WhenExpression.Case.create(isDirectory($this$receiver), () -> this.processDirectory($this$receiver, phase, globalContext, phaseContext)),
                        WhenExpression.Case.create(exists($this$receiver), () -> this.processFile($this$receiver, $this$receiver, phase, globalContext, phaseContext))
                ),
                () -> {
                    this.l.invoke().debug("Skipping location '" + $this$receiver + "' because it doesn't exist: please complain to your nearest cat");
                    return Unit.UNIT;
                }
                ).invoke();
    }

    @Nonnull
    private Unit processDirectory(@Nonnull final Location $this$receiver, @Nonnull final LoadingPhase<Object> phase, @Nonnull final Nullable<Context> globalContext,
                                  @Nonnull final Nullable<Context> phaseContext) {
        this.l.invoke().debug("Attempting to read all the files inside the directory '" + $this$receiver + "'");
        try (@Nonnull final Stream<Path> file = Files.walk($this$receiver.getPath())) {
            file.forEach(it -> this.processFile(
                    toLocation(it, $this$receiver.getAdditionalContext()),
                    toLocation($this$receiver.getPath().relativize(it), $this$receiver.getAdditionalContext()),
                    phase, globalContext, phaseContext
            ));
            return Unit.UNIT;
        } catch (@Nonnull final IOException e) {
            throw CheckedException.wrap(e);
        }
    }

    @Nonnull
    private Unit processFile(@Nonnull final Location $this$receiver, @Nonnull final Location relative, @Nonnull final LoadingPhase<Object> phase,
                             @Nonnull final Nullable<Context> globalContext, @Nonnull final Nullable<Context> phaseContext) {
        this.l.invoke().debug("Attempting to read file '" + $this$receiver + "' (relative: " + relative + ")");
        final NameSpacedString name = this.identifierBuilder.makeIdentifier(relative, globalContext, phaseContext);
        return IfExpression.build(
                isFiltered($this$receiver, phase),
                () -> {
                    this.l.invoke().debug("Skipping processing of file '" + name + "' because it was filtered");
                    return Unit.UNIT;
                },
                () -> TryExpression.create(
                        () -> this.process($this$receiver, phase, name, globalContext, phaseContext),
                        ImmutableList.of(
                                TryExpression.CatchClause.create(KClass.get(Exception.class), e -> {
                                    throw new ProcessingException("An error has occurred while attempting to process location '" + this + "' (name is '" + name + "')", e);
                                })
                        )
                ).invoke()
        ).invoke();
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private Unit process(@Nonnull final Location $this$receiver, @Nonnull final LoadingPhase<Object> phase, @Nonnull final NameSpacedString name,
                         @Nonnull final Nullable<Context> globalContext, @Nonnull final Nullable<Context> phaseContext) {
        this.progressReporter.ifPresent(it -> it.visitItem(name));
        this.l.invoke().debug("Reading data from '" + name + "'");
        final String content = this.loadContent($this$receiver);
        final Nullable<Preprocessor<String, Object>> pre = phase.getPreprocessor();
        return IfExpression.build(
                pre.unwrap() == null,
                () -> phase.getProcessor().process(content, name, globalContext, phaseContext),
                () -> {
                    final Nullable<Object> preContent = pre.unwrap().preProcessData(content, name, globalContext, phaseContext);
                    return IfExpression.build(
                            preContent.unwrap() != null,
                            () -> phase.getProcessor().process(preContent.unwrap(), name, globalContext, phaseContext),
                            () -> Unit.UNIT
                    ).invoke();
                }
        ).invoke();
    }

    @Nonnull
    private String loadContent(@Nonnull final Location $this$receiver) {
        try (@Nonnull final BufferedReader file = Files.newBufferedReader($this$receiver.getPath())) {
            return file.lines().collect(Collectors.joining("\n"));
        } catch (@Nonnull final IOException e) {
            throw CheckedException.wrap(e);
        }
    }

    @Nonnull
    @SuppressWarnings("UnusedReturnValue")
    private static <T> Unit forEachIndexed(@Nonnull final Iterable<T> $this$receiver, @Nonnull final KFunction2<Integer, T, Unit> loop) {
        /*mutable*/ int index = 0;
        for (final T item : $this$receiver) loop.invoke(index++, item);
        return Unit.UNIT;
    }

    private static boolean isFiltered(@Nonnull final Location $this$receiver, @Nonnull final LoadingPhase<?> phase) {
        return any(phase.getFilters(), it -> !it.canLoad($this$receiver));
    }

    private static <T> boolean any(@Nonnull final Iterable<T> $this$receiver, @Nonnull final KFunction1<T, Boolean> predicate) {
        if ($this$receiver instanceof Collection && ((Collection<?>) $this$receiver).isEmpty()) return false;
        for (final T element : $this$receiver) if (predicate.invoke(element)) return true;
        return false;
    }

    private static boolean exists(@Nonnull final Location $this$receiver) {
        return exists($this$receiver.getPath());
    }

    private static boolean isDirectory(@Nonnull final Location $this$receiver) {
        return isDirectory($this$receiver.getPath());
    }

    private static boolean exists(@Nonnull final Path $this$receiver) {
        return Files.exists($this$receiver);
    }

    private static boolean isDirectory(@Nonnull final Path $this$receiver) {
        return Files.isDirectory($this$receiver);
    }

    @Nonnull
    private static Location toLocation(@Nonnull final Path $this$receiver, @Nonnull final Nullable<Context> context) {
        return new LocationPathWrapper($this$receiver, context);
    }
}
