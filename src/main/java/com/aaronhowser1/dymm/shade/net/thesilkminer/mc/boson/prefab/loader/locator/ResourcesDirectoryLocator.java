package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.locator;

import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.CheckedException;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Lazy;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Unit;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.IfExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Location;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Locator;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.CCK;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.context.BaseContext;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.location.BaseLocation;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ResourcesDirectoryLocator implements Locator {
    public enum Kind {
        ASSETS("assets"),
        DATA("data");

        private final String directoryName;

        Kind(@Nonnull final String directoryName) {
            this.directoryName = directoryName;
        }
    }

    private static final L LOG = L.create("[DYMM Shade] Boson", "ResourcesDirectoryLocator");

    private final String targetDirectory;
    private final Kind kind;

    private final List<AutoCloseable> walkStack = new ArrayList<>();

    private final Lazy<List<Lazy<Location>>> lazyLocations;

    private ResourcesDirectoryLocator(@Nonnull final String targetDirectory, @Nonnull final Kind kind) {
        this.targetDirectory = Objects.requireNonNull(targetDirectory);
        this.kind = Objects.requireNonNull(kind);
        this.lazyLocations = Lazy.lazy(() -> {
            LOG.info("Attempting to load data from the 'resources/' directory, situated in your main game directory");
            LOG.info("We are currently looking in the '" + this.targetDirectory + "' directory with kind " + this.kind);
            return this.scanResourcesDirectory(Paths.get(".").resolve("./resources").normalize().toAbsolutePath());
        });
    }

    @Nonnull
    public static ResourcesDirectoryLocator create(@Nonnull final String targetDirectory, @Nonnull final Kind kind) {
        return new ResourcesDirectoryLocator(targetDirectory, kind);
    }

    @Nonnull
    public static ResourcesDirectoryLocator create(@Nonnull final String targetDirectory) {
        return create(targetDirectory, Kind.DATA);
    }

    @Nonnull
    @Override
    public List<Lazy<Location>> getLocations() {
        return this.lazyLocations.invoke();
    }

    @Nonnull
    private List<Lazy<Location>> scanResourcesDirectory(@Nonnull final Path resources) {
        return this.scanAllDirectories(resources.resolve("./" + this.kind.directoryName + "/").normalize().toAbsolutePath(), resources);
    }

    @Nonnull
    private List<Lazy<Location>> scanAllDirectories(@Nonnull final Path data, @Nonnull final Path resources) {
        return IfExpression
                .build(
                        Files.exists(data),
                        () -> {
                            try {
                                return addToWalkStack(Files.walk(data, 1))
                                        .filter(it -> !it.getParent().getFileName().toString().endsWith("resources"))
                                        .flatMap(it -> this.scanDirectory(it, resources))
                                        .collect(Collectors.toList());
                            } catch (@Nonnull final IOException e) {
                                throw CheckedException.wrap(e);
                            }
                        },
                        () -> {
                            LOG.info("Directory '" + data + "' does not exist: skipping resource loading");
                            return new ArrayList<Lazy<Location>>();
                        }
                ).invoke();
    }

    @Nonnull
    private Stream<Lazy<Location>> scanDirectory(@Nonnull final Path id, @Nonnull final Path resources) {
        return this.scanForFiles(id.resolve("./" + this.targetDirectory).normalize().toAbsolutePath(), id, resources);
    }

    @Nonnull
    private Stream<Lazy<Location>> scanForFiles(@Nonnull final Path target, @Nonnull final Path id, @Nonnull final Path resources) {
        final String modId = id.getFileName().toString();
        if (!Files.exists(target)) {
            LOG.warn("Directory '" + resources.relativize(target) + "' doesn't exist: skipping loading of files");
        } else {
            LOG.info("Successfully identified directory '" + resources.relativize(target) + "': proceeding with loading of files");
        }
        return Stream.of(Lazy.lazy(() -> {
            final Context context = BaseContext.create();
            context.set(CCK.MOD_ID_CONTEXT_KEY.invoke(), modId);
            return BaseLocation.create(target.toAbsolutePath(), Nullable.get(modId + " - User-Added Resources"), Nullable.get(context));
        }));
    }

    @Nonnull
    @Override
    public Unit clean() {
        reversed(this.walkStack).forEach(it -> {
            try {
                it.close();
            } catch (@Nonnull final Exception e) {
                throw CheckedException.wrap(e);
            }
        });
        return Locator.super.clean();
    }

    @Nonnull
    private <T extends AutoCloseable> T addToWalkStack(@Nonnull final T t) {
        this.walkStack.add(t);
        return t;
    }

    @Nonnull
    private static <T> List<T> reversed(@Nonnull final List<T> $this$receiver) {
        return IfExpression
                .build(
                        $this$receiver.size() <= 1,
                        () -> $this$receiver,
                        () -> {
                            final List<T> copy = new ArrayList<>($this$receiver);
                            reverse(copy);
                            return copy;
                        }
                ).invoke();
    }

    @Nonnull
    @SuppressWarnings("UnusedReturnValue")
    private static <T> Unit reverse(@Nonnull final List<T> $this$receiver) {
        Collections.reverse($this$receiver);
        return Unit.UNIT;
    }
}
