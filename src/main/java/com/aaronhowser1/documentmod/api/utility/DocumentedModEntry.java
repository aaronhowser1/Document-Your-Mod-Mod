package com.aaronhowser1.documentmod.api.utility;

import com.google.common.base.Preconditions;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("WeakerAccess")
public final class DocumentedModEntry {

    @SuppressWarnings("SpellCheckingInspection")
    private static final String MOD_ACRONYM = "DYMM";

    private final String id;
    private final String name;
    private final List<String> supportedVersions;

    private DocumentedModEntry(@Nonnull final String modId, @Nonnull final String name, @Nonnull final String... supportedVersions) {
        this.id = modId;
        this.name = name;
        this.supportedVersions = Arrays.asList(supportedVersions);
    }

    @Nonnull
    public static DocumentedModEntry from(@Nonnull final ModContainer container, @Nonnull final String... supportedVersions) {
        return of(container.getModId(), container.getName(), supportedVersions);
    }

    @Nonnull
    public static DocumentedModEntry of(@Nonnull final String modId, @Nonnull final String name, @Nonnull final String... supportedVersions) {
        Preconditions.checkArgument(supportedVersions.length > 0, "Must specify at least one supported version");
        return new DocumentedModEntry(modId, name, supportedVersions);
    }

    @Nonnull
    public String getId() {
        return this.id;
    }

    public boolean isVersionDocumented(@Nonnull final ModContainer container) {
        return this.supportedVersions.stream().anyMatch(it -> this.strip(container.getVersion()).startsWith(it));
    }

    @Nonnull
    public String getDisplayableString() {
        return this.getDisplayableStringFor(ContainerFinder.INSTANCE.findContainerFromId(this.id));
    }

    @Nonnull
    public String getDisplayableStringFor(@Nullable final ModContainer container) {
        if (container == null) return this.getDisplayableStringWithoutContainer();
        return this.wrapInBuilder(builder -> {
            final boolean documented = this.isVersionDocumented(container);

            builder.append(documented? TextFormatting.GREEN : TextFormatting.YELLOW);
            builder.append(container.getName());
            builder.append(" version ");
            builder.append(container.getDisplayVersion());

            if (documented) {
                builder.append(" (Fully supported)");
                return;
            }

            builder.append(" (Documentation may be out of date - ");
            this.appendSupportedVersions(builder);
            builder.append(")");
        });
    }

    @Nonnull
    private String getDisplayableStringWithoutContainer() {
        return this.wrapInBuilder(builder -> {
            builder.append(TextFormatting.RED);
            builder.append(this.name);
            builder.append(" (Not installed - ");
            this.appendSupportedVersions(builder);
            builder.append(")");
        });
    }

    @Nonnull
    private String wrapInBuilder(@Nonnull final Consumer<StringBuilder> builderConsumer) {
        final StringBuilder builder = new StringBuilder("- ");
        builderConsumer.accept(builder);
        builder.append(TextFormatting.RESET);
        builder.append('\n');
        return builder.toString();
    }

    private void appendSupportedVersions(@Nonnull final StringBuilder builder) {
        builder.append(MOD_ACRONYM);
        builder.append(" supports: ");
        builder.append(String.join(", ", this.supportedVersions));
    }

    @Nonnull
    private String strip(@Nonnull final String s) {
        if (s.startsWith("1.12.2-")) return s.substring("1.12.2-".length());
        else if (s.startsWith("1.12.2")) return s.substring("1.12.2".length());
        else return s;
    }
}
