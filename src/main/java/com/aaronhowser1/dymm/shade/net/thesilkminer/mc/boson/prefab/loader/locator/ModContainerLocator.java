package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.locator;

import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.CheckedException;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Lazy;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Unit;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.ElvisExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.IfExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.TryExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.WhenExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Location;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Locator;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.CCK;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.context.BaseContext;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.location.BaseLocation;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ModContainerLocator implements Locator {
    public enum Kind {
        ASSETS("assets"),
        DATA("data");

        private final String directoryName;

        Kind(@Nonnull final String directoryName) {
            this.directoryName = directoryName;
        }
    }

    private static final L LOG = L.create("[DYMM Shade] Boson", "ModContainerLocator");
    private static final Lazy<Path> PATH_THAT_DOES_NOT_EXIST = Lazy.lazy(() -> {
        /*mutable*/ Path path = Paths.get("path.that.does.not.exist.because.is.illegal.in.most.file.systems.and.operating.systems");
        while (Files.exists(path)) path = path.resolve("path.that.does.not.exist.because.is.illegal.in.most.file.systems.and.operating.systems");
        return path;
    });

    private final String targetDirectory;
    private final Kind kind;

    private final List<Nullable<FileSystem>> systemsStack = new ArrayList<>();

    private final Lazy<List<Lazy<Location>>> lazyLocations;

    private ModContainerLocator(@Nonnull final String targetDirectory, @Nonnull final Kind kind) {
        this.targetDirectory = Objects.requireNonNull(targetDirectory);
        this.kind = Objects.requireNonNull(kind);
        this.lazyLocations = Lazy.lazy(() -> {
            LOG.info("Attempting to load data from ModContainer: currently looking in '" + this.targetDirectory + "' with kind " + this.kind);
            return Loader.instance().getModList()
                    .stream()
                    .map(this::findLazyLocation)
                    .map(Nullable::unwrap)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        });
    }

    @Nonnull
    public static ModContainerLocator create(@Nonnull final String targetDirectory, @Nonnull final Kind kind) {
        return new ModContainerLocator(targetDirectory, kind);
    }

    @Nonnull
    public static ModContainerLocator create(@Nonnull final String targetDirectory) {
        return create(targetDirectory, Kind.DATA);
    }

    @Nonnull
    @Override
    public List<Lazy<Location>> getLocations() {
        return this.lazyLocations.invoke();
    }

    @Nonnull
    private Nullable<Lazy<Location>> findLazyLocation(@Nonnull final ModContainer container) {
        class FS {
            private /*mutable*/ Nullable<FileSystem> fileSystem = Nullable.get(null);
        }

        final String jsonDirectory = this.kind.directoryName + "/" + container.getModId() + "/" + this.targetDirectory;
        final File source = container.getSource();

        final FS fileSystem = new FS();
        return TryExpression.create(
                () -> {
                    final Path root = ElvisExpression.create(
                            WhenExpression.create(ImmutableList.of(
                                    WhenExpression.Case.create(source.isFile(), () -> {
                                        final FileSystem fs;
                                        try {
                                            // This is a JAR mod
                                            fs = FileSystems.newFileSystem(source.toPath(), null);
                                            fileSystem.fileSystem = Nullable.get(fs);
                                        } catch (final IOException e) {
                                            throw CheckedException.wrap(e);
                                        }
                                        return Nullable.get(fs.getPath("/" + jsonDirectory));
                                    }),
                                    WhenExpression.Case.create(source.isDirectory(), () -> Nullable.get(source.toPath().resolve(jsonDirectory))),
                                    WhenExpression.Case.create(!source.exists(), () -> {
                                        LOG.debug("Source '" + source + "' for mod container '" + container + "' doesn't exist, it probably is a Launch Plugin or a JAR mod: skipping");
                                        return Nullable.<Path>get(null);
                                    })
                            ), () -> {
                                throw new IllegalStateException("Source '" + source + "' for mod container '" + container + "' is not a file nor a directory: this should be impossible");
                            }).invoke(),
                            PATH_THAT_DOES_NOT_EXIST
                    ).invoke();

                    if (!Files.exists(root)) {
                        LOG.debug("No directory found in mod container '" + container + "' that matches the path '" + jsonDirectory + "': will be skipped later on");
                    } else {
                        LOG.info("Successfully found directory '" + jsonDirectory + "' for mod container '" + container + "': adding it to the list");
                    }

                    return Nullable.get(Lazy.lazy(() -> {
                        final Context context = BaseContext.create();
                        context.set(CCK.MOD_ID_CONTEXT_KEY.invoke(), container.getModId());
                        return (Location) BaseLocation.create(root, Nullable.get(container.getName()), Nullable.get(context));
                    }));
                },
                ImmutableList.of(
                    TryExpression.CatchClause.create(KClass.get(CheckedException.class), e -> {
                        LOG.bigWarn("An error has occurred while attempting to identify the directory for the candidate " + container + "\n" +
                                "The container will now be skipped. The exception and the relevant stack traces are the following:\n\n" +
                                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
                        return Nullable.<Lazy<Location>>get(null);
                    })
                ),
                () -> {
                    this.systemsStack.add(fileSystem.fileSystem);
                    return Unit.UNIT;
                }
        ).invoke();
    }

    @Nonnull
    @Override
    public Unit clean() {
        reversed(this.systemsStack).stream().map(Nullable::unwrap).filter(Objects::nonNull).forEach(it -> {
            try {
                it.close();
            } catch (@Nonnull final IOException e) {
                throw CheckedException.wrap(e);
            }
        });
        return Locator.super.clean();
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
