package com.aaronhowser1.documentmod;

import com.google.common.collect.Lists;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum DocumentedMods {
    INSTANCE;

    public static final class DocumentedMod {
        private final String id;
        private final String displayName;
        private final List<String> versions;

        private DocumentedMod(@Nonnull final String id, @Nonnull final String displayName, @Nonnull final String...documentedVersions) {
            this.id = id;
            this.displayName = displayName;
            this.versions = Arrays.asList(documentedVersions);
        }

        @Nonnull
        public static DocumentedMod of(@Nonnull final String id, @Nonnull final String displayName, @Nonnull final String... documentedVersions) {
            if (documentedVersions.length <= 0) throw new IllegalArgumentException("documentedVersions.length <= 0");
            return new DocumentedMod(id, displayName, documentedVersions);
        }

        @Nonnull
        public String getId() {
            return this.id;
        }

        @SuppressWarnings("WeakerAccess")
        public boolean isVersionDocumented(@Nonnull final ModContainer container) {
            return this.versions.stream().anyMatch(it -> this.strip(container.getVersion()).startsWith(it));
        }

        @Nonnull
        @SideOnly(Side.CLIENT)
        public String format(@Nullable final ModContainer container) {
            if (container == null) return this.formatNotInstalled();

            final boolean isDocumented = this.isVersionDocumented(container);
            final String version = container.getDisplayVersion();

            final StringBuilder builder = new StringBuilder("- ");
            builder.append(isDocumented? TextFormatting.GREEN : TextFormatting.YELLOW);
            builder.append(container.getName());
            builder.append(" version ");
            builder.append(StringUtils.stripControlCodes(version));
            if (!isDocumented) {
                //noinspection SpellCheckingInspection
                builder.append(" (May be out of date - DYMM supports: ");
                for (int i = 0; i < this.versions.size(); i++) {
                    final String supportedVersion = this.versions.get(i);
                    builder.append(supportedVersion);
                    if (i != (this.versions.size() - 1)) builder.append(", ");
                }
                builder.append(")");
            }
            builder.append(TextFormatting.RESET);
            builder.append('\n');
            return builder.toString();
        }

        @Nonnull
        @SideOnly(Side.CLIENT)
        private String formatNotInstalled() {
            final StringBuilder builder = new StringBuilder("- ");
            builder.append(TextFormatting.RED);
            builder.append(this.displayName);
            //noinspection SpellCheckingInspection
            builder.append(" (Not installed - DYMM supports: ");
            for (int i = 0; i < this.versions.size(); i++) {
                final String supportedVersion = this.versions.get(i);
                builder.append(supportedVersion);
                if (i != (this.versions.size() - 1)) builder.append(", ");
            }
            builder.append(")");
            builder.append(TextFormatting.RESET);
            builder.append('\n');
            return builder.toString();
        }

        @Nonnull
        private String strip(@Nonnull final String s) {
            if (s.startsWith("1.12.2-")) return s.substring("1.12.2-".length());
            else if (s.startsWith("1.12.2")) return s.substring("1.12.2".length());
            else return s;
        }
    }

    @Nonnull
    public List<DocumentedMod> getAllDocumentedMods() {
        return Lists.newArrayList(
                DocumentedMod.of(ModId.QUARK, "Quark", "r1.6"),
                DocumentedMod.of(ModId.IRONCHEST, "Iron Chests", "7.0.67"),
                DocumentedMod.of(ModId.NATURES_COMPASS, "Nature's Compass", "1.5.0"),
                DocumentedMod.of("none", "Random Mod", "44"),
                DocumentedMod.of(DocumentMod.MODID, "Document Your Mod Mod", "Whatever")
        );
    }

    @Nonnull
    public Optional<ModContainer> findContainerFor(@Nonnull final DocumentedMod mod) {
        return Loader.instance().getActiveModList().stream()
                .filter(it -> it.getModId().equals(mod.getId()))
                .findFirst();
    }
}
