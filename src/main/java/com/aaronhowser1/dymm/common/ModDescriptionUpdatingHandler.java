package com.aaronhowser1.dymm.common;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.api.ApiBindings;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ModDescriptionUpdatingHandler {
    private enum Support {
        FULL_SUPPORT("dymm.mod.support.full_support", TextFormatting.DARK_GREEN),
        DOCUMENTATION_OUT_OF_DATE("dymm.mod.support.documentation_out_of_date", TextFormatting.GOLD),
        DOCUMENTATION_NEWER_THAN_MOD("dymm.mod.support.documentation_newer_than_mod", TextFormatting.RED),
        INFO_NOT_PROVIDED("dymm.mod.support.info_not_provided", TextFormatting.DARK_PURPLE),
        NOT_SUPPORTED("dymm.mod.support.not_supported", TextFormatting.GRAY);

        private final String friendlyName;
        private final TextFormatting color;

        Support(@Nonnull final String friendlyName, @Nonnull final TextFormatting color) {
            this.friendlyName = friendlyName;
            this.color = color;
        }

        @Nonnull
        private String getFriendlyName() {
            return this.friendlyName;
        }

        @Nonnull
        private TextFormatting getColor() {
            return this.color;
        }
    }

    private static final Map<String, List<String>> SUPPORTED_VERSIONS = new HashMap<>();

    public static void addVersion(@Nonnull final String id, @Nonnull final List<String> supportedVersions) {
        Objects.requireNonNull(supportedVersions);
        SUPPORTED_VERSIONS.put(id, supportedVersions);
    }

    public static void updateModDescription() {
        final ModContainer container = Loader.instance().getModList().stream().filter(it -> it.getModId().equals(Constants.MOD_ID)).findFirst().orElseThrow(RuntimeException::new);
        final ModMetadata metadata = container.getMetadata();
        metadata.description = updateModDescription(metadata.description);
    }

    @Nonnull
    private static String updateModDescription(@Nonnull final String oldDescription) {
        updateMap();
        final StringBuilder builder = new StringBuilder(oldDescription);
        builder.append("\n\nMod support status:\n");
        sortCompatibilityList(buildCompatibilityList())
                .forEach(it -> {
                    builder.append(it.getRight().getColor());
                    builder.append("  -");
                    builder.append(it.getLeft().getName());
                    builder.append(' ');
                    builder.append(formatIpBased(it.getLeft().getDisplayVersion(), it.getRight().getColor()));
                    builder.append(" (");
                    builder.append(formatIpBased(I18n.format(it.getRight().getFriendlyName()), it.getRight().getColor()));
                    builder.append(')');
                    builder.append(TextFormatting.RESET);
                    builder.append('\n');
                });
        return builder.toString();
    }

    private static void updateMap() {
        ApiBindings.getMainApi()
                .getDocumentationRegistry()
                .getEntries()
                .stream()
                .map(Map.Entry::getKey)
                .map(ResourceLocation::getNamespace)
                .forEach(it -> SUPPORTED_VERSIONS.computeIfAbsent(it, key -> new ArrayList<>()));
    }

    @Nonnull
    private static List<Pair<ModContainer, Support>> buildCompatibilityList() {
        return Loader.instance()
                .getModList()
                .stream()
                .map(it -> ImmutablePair.of(it, getContainerSupportLevel(it)))
                .collect(Collectors.toList());
    }

    @Nonnull
    private static Support getContainerSupportLevel(@Nonnull final ModContainer container) {
        final String modId = container.getModId();
        final String currentVersion = container.getDisplayVersion();
        final List<String> supportedVersions = SUPPORTED_VERSIONS.get(modId);

        if (supportedVersions == null) return Support.NOT_SUPPORTED;
        if (supportedVersions.isEmpty()) return Support.INFO_NOT_PROVIDED;
        if (supportedVersions.contains(currentVersion)) return Support.FULL_SUPPORT;

        // Now we should parse the version strings and check whether the one provided by the mod container
        // is newer or older than the version. First off, though, we need to find the "highest" string in the
        // list of provided ones.
        final String highest = findHighestVersion(supportedVersions);
        final String actualHighest = findHighestVersion(highest, currentVersion);
        return actualHighest.equals(highest)? Support.DOCUMENTATION_NEWER_THAN_MOD : Support.DOCUMENTATION_OUT_OF_DATE;
    }

    @Nonnull
    private static String findHighestVersion(@Nonnull final List<String> strings) {
        /*mutable*/ String highest = strings.get(0);
        if (strings.size() == 1) return highest; // Well, obvious
        for (@Nonnull final String version : strings) {
            highest = findHighestVersion(highest, version);
        }
        return highest;
    }

    @Nonnull
    private static String findHighestVersion(@Nonnull final String a, @Nonnull final String b) {
        return a.equals(b)? a : compareVersionString(a, b);
    }

    @Nonnull
    private static String compareVersionString(@Nonnull final String a, @Nonnull final String b) {
        if (a.startsWith("1.12.2-")) return compareVersionString(a.substring("1.12.2-".length()), b);
        if (a.startsWith("1.12.2")) return compareVersionString(a.substring("1.12.2".length()), b);
        if (b.startsWith("1.12.2-")) return compareVersionString(a, b.substring("1.12.2-".length()));
        if (b.startsWith("1.12.2")) return compareVersionString(a, b.substring("1.12.2".length()));
        return compareVersion(new DefaultArtifactVersion(a), new DefaultArtifactVersion(b));
    }

    @Nonnull
    private static String compareVersion(@Nonnull final ArtifactVersion a, @Nonnull final ArtifactVersion b) {
        return a.compareTo(b) > 0? a.toString() : b.toString();
    }

    @Nonnull
    private static List<Pair<ModContainer, Support>> sortCompatibilityList(@Nonnull final List<Pair<ModContainer, Support>> unsorted) {
        final List<Pair<ModContainer, Support>> sorted = new ArrayList<>();
        unsorted.sort(Comparator.comparing(it -> it.getLeft().getName().toLowerCase(Locale.ENGLISH)));
        unsorted.stream().filter(it -> it.getRight() == Support.FULL_SUPPORT).forEach(sorted::add);
        unsorted.stream().filter(it -> it.getRight() == Support.DOCUMENTATION_OUT_OF_DATE).forEach(sorted::add);
        unsorted.stream().filter(it -> it.getRight() == Support.DOCUMENTATION_NEWER_THAN_MOD).forEach(sorted::add);
        unsorted.stream().filter(it -> it.getRight() == Support.INFO_NOT_PROVIDED).forEach(sorted::add);
        unsorted.stream().filter(it -> it.getRight() == Support.NOT_SUPPORTED).forEach(sorted::add);
        return sorted;
    }

    @Nonnull
    private static String formatIpBased(@Nonnull final String string, @Nonnull final TextFormatting color) {
        final int dots = StringUtils.countMatches(string, '.');
        if (dots < 3) return string;
        // Basically MC's color formatter decides to blow himself up when a string contains 3 dots or more, probably
        // because it identifies it as an IP and... it doesn't want to have IPs colored? Who the fuck knows? Anyway,
        // we are going to split the string every dot and add the color back again. Hopefully this avoids weird issues.
        final String[] split = string.split(Pattern.quote("."));
        final String colorAdder = "" + TextFormatting.RESET + color;
        return Arrays.stream(split).collect(Collectors.joining("." + colorAdder, "", colorAdder));
    }
}
