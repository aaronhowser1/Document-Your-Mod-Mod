package com.aaronhowser1.documentmod.api.utility;

import com.google.common.base.Preconditions;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Represents an entry on the documented mod list that identifies
 * a specific mod.
 *
 * <p>Every instance of this class, constructed through its factory
 * methods {@link #from(ModContainer, String...)} and
 * {@link #of(String, String, String...)} should then be provided to
 * Document Your Mod Mod through events or other means that the mod
 * deems necessary.</p>
 *
 * <p>These entries are then automatically parsed and displayed in
 * the documented mod list, with the given formatting according to
 * the current documentation status. Refer to the internal code for
 * more information.</p>
 *
 * @see com.aaronhowser1.documentmod.api.event.PopulateDocumentationStatusEvent
 *
 * @since 1.1.2
 */
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

    /**
     * Constructs a documented mod entry starting from the given
     * {@link ModContainer} and the provided supported versions.
     *
     * <p>Implementations are suggested not to pass the container's
     * version in {@code supportedVersions}. This will result in
     * an always up-to-date entry being added. While this may be
     * desirable, it is a potentially false information for users
     * in case you do not update the mod as soon as the container's
     * version updates.</p>
     *
     * <p>Note that the matching between the supported versions
     * and the one currently loaded is performed according to
     * certain rules. These are outlined in
     * {@link #isVersionDocumented(ModContainer)}: refer to that
     * method for more information.</p>
     *
     * @param container
     *     The container which needs to be used to pull the
     *     information about the mod. It cannot be null.
     * @param supportedVersions
     *     The versions that are currently documented inside
     *     Document Your Mod Mod. It cannot be null and it
     *     cannot be empty.
     * @return
     *     A constructed documented mod entry containing the
     *     supplied information.
     *
     * @since 1.1.2
     */
    @Nonnull
    @SuppressWarnings("unused")
    public static DocumentedModEntry from(@Nonnull final ModContainer container, @Nonnull final String... supportedVersions) {
        return of(container.getModId(), container.getName(), supportedVersions);
    }

    /**
     * Constructs a documented mod entry starting from the given
     * id, name and supported versions.
     *
     * <p>Implementations are suggested not to pass the container's
     * version in {@code supportedVersions}. This will result in
     * an always up-to-date entry being added. While this may be
     * desirable, it is a potentially false information for users
     * in case you do not update the mod as soon as the container's
     * version updates.</p>
     *
     * <p>Note that the matching between the supported versions
     * and the one currently loaded is performed according to
     * certain rules. These are outlined in
     * {@link #isVersionDocumented(ModContainer)}: refer to that
     * method for more information.</p>
     *
     * @param modId
     *     The ID of the mod that this entry refers to. It must
     *     not be null.
     * @param name
     *     The name to use when the mod container cannot be
     *     found. It must not be null.
     * @param supportedVersions
     *     The versions that are currently documented inside
     *     Document Your Mod Mod. It cannot be null and it
     *     cannot be empty.
     * @return
     *     A constructed documented mod entry containing the
     *     supplied information.
     *
     * @since 1.1.2
     */
    @Nonnull
    public static DocumentedModEntry of(@Nonnull final String modId, @Nonnull final String name, @Nonnull final String... supportedVersions) {
        Preconditions.checkArgument(supportedVersions.length > 0, "Must specify at least one supported version");
        return new DocumentedModEntry(modId, name, supportedVersions);
    }

    /**
     * Gets the ID that identifies the mod that this entry
     * is referring to.
     *
     * @return
     *     The ID that identifies the mod that this entry
     *     is referring to. Guaranteed to be not-null.
     *
     * @since 1.1.2
     */
    @Nonnull
    public String getId() {
        return this.id;
    }

    /**
     * Finds out whether the version identified by the given
     * {@link ModContainer} is supported according to this
     * entry.
     *
     * <p>The container's version is manipulated in order to
     * make it easier for users of this entry to indicate
     * which versions are supported. More specifically, the
     * version is first stripped of the leading "1.12.2-"
     * string, that indicates the Minecraft version this mod
     * is made for, if present. Then the version is matched
     * according to a "startsWith" call. In other words, what
     * matters is that the version starts with the one defined
     * in this entry, not that it matches completely.</p>
     *
     * @param container
     *     The container it should be checked for compatibility.
     *     It must not be null.
     * @return
     *     Whether the version of the given mod container
     *     is documented or not.
     *
     * @since 1.1.2
     */
    public boolean isVersionDocumented(@Nonnull final ModContainer container) {
        return this.supportedVersions.stream().anyMatch(it -> this.strip(container.getVersion()).startsWith(it));
    }

    /**
     * Gets the string to display on the Mod Information
     * screen about the mod identified by this mod entry.
     *
     * @return
     *     The string to display on the screen. Guaranteed
     *     to be not-null.
     *
     * @since 1.1.2
     */
    @Nonnull
    public String getDisplayableString() {
        return this.getDisplayableStringFor(ContainerFinder.INSTANCE.findContainerFromId(this.id));
    }

    /**
     * Gets the string to display on the Mod Information
     * screen about the mod identified by this mod entry
     * and the given container.
     *
     * @param container
     *     The container that should be used for checking
     *     against the supported versions of this entry.
     *     It can be null, which means that no suitable
     *     mod container could be found.
     * @return
     *     The string to display on the screen. Guaranteed
     *     to be not-null
     *
     * @since 1.1.2
     */
    @Nonnull
    public String getDisplayableStringFor(@Nullable final ModContainer container) {
        if (container == null) return this.getDisplayableStringWithoutContainer();
        return this.wrapInBuilder(builder -> {
            final boolean documented = this.isVersionDocumented(container);
            final TextFormatting color = documented? TextFormatting.GREEN : TextFormatting.YELLOW;

            builder.append(color);
            builder.append("- ");
            builder.append(container.getName());
            builder.append(" version ");
            builder.append(this.getDisplayableVersion(container.getDisplayVersion(), color));

            if (documented) {
                builder.append(" (Fully supported)");
                return;
            }

            builder.append(" (Documentation may be out of date - ");
            this.appendSupportedVersions(builder, color);
            builder.append(")");
        });
    }

    @Nonnull
    private String getDisplayableStringWithoutContainer() {
        return this.wrapInBuilder(builder -> {
            builder.append(TextFormatting.DARK_GRAY);
            builder.append("- ");
            builder.append(this.name);
            builder.append(" (Not installed - ");
            this.appendSupportedVersions(builder, TextFormatting.DARK_GRAY);
            builder.append(")");
        });
    }

    @Nonnull
    private String wrapInBuilder(@Nonnull final Consumer<StringBuilder> builderConsumer) {
        final StringBuilder builder = new StringBuilder();
        builderConsumer.accept(builder);
        builder.append(TextFormatting.RESET);
        builder.append('\n');
        return builder.toString();
    }

    private void appendSupportedVersions(@Nonnull final StringBuilder builder, @Nonnull final TextFormatting color) {
        builder.append(MOD_ACRONYM);
        builder.append(" supports: ");
        builder.append(this.supportedVersions.stream()
                .map(it -> this.getDisplayableVersion(it, color))
                .collect(Collectors.joining(", ")));
    }

    @Nonnull
    private String strip(@Nonnull final String s) {
        if (s.startsWith("1.12.2-")) return s.substring("1.12.2-".length());
        else if (s.startsWith("1.12.2")) return s.substring("1.12.2".length());
        else return s;
    }

    @Nonnull
    private String getDisplayableVersion(@Nonnull final String containerString, @Nonnull final TextFormatting color) {
        final int dots = StringUtils.countMatches(containerString, '.');
        if (dots < 3) return containerString;
        // Basically MC's color formatter decides to blow himself up when a string contains 3 dots or more, probably
        // because it identifies it as an IP and... it doesn't want to have IPs colored? Who the fuck knows? Anyway,
        // we are going to split the string every dot and add the color back again. Hopefully this avoids weird issues.
        final String[] split = containerString.split(Pattern.quote("."));
        final String colorAdder = "" + TextFormatting.RESET + color;
        return Arrays.stream(split).collect(Collectors.joining("." + colorAdder, "", colorAdder));
    }
}
