package com.aaronhowser1.dymm.module.compatibility.minecraft.consume;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.consume.DocumentationDataConsumer;
import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.documentation.Target;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Side.CLIENT)
public final class TooltipDocumentationConsumer implements DocumentationDataConsumer {
    private static final Map<Target, List<Pair<TextFormatting, String>>> TARGET_ENTRIES;
    private static final LoadingCache<ResourceLocation, List<DocumentationEntry>> DOCUMENTATION_CACHE;
    private static final LoadingCache<ItemStack, List<Pair<TextFormatting, String>>> STACKS_CACHE;

    static {
        TARGET_ENTRIES = new HashMap<>();
        DOCUMENTATION_CACHE = CacheBuilder.newBuilder()
                .maximumSize(500)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<ResourceLocation, List<DocumentationEntry>>() {
                    @Nonnull
                    @Override
                    public List<DocumentationEntry> load(@Nonnull final ResourceLocation key) {
                        return ApiBindings.getMainApi()
                                .getDocumentationRegistry()
                                .getValuesCollection()
                                .stream()
                                .filter(it -> it.getTargets()
                                        .stream()
                                        .map(Target::obtainTarget)
                                        .map(ItemStack::getItem)
                                        .map(Item::getRegistryName)
                                        .peek(Objects::requireNonNull)
                                        .anyMatch(key::equals)
                                )
                                .collect(Collectors.toList());
                    }
                });

        // Stacks are the worst as keys, because they don't override equals, they rely on
        // identity etc... For this reason, this cache is only useful in case someone is
        // looking at the same ItemStack for a certain amount of time. Any operation on
        // an ItemStack will make it change (especially with "copy") and so on. This is
        // the reason why we keep them for such a low time: a few seconds basically.
        STACKS_CACHE = CacheBuilder.newBuilder()
                .maximumSize(50)
                .expireAfterAccess(3, TimeUnit.SECONDS)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .build(new CacheLoader<ItemStack, List<Pair<TextFormatting, String>>>() {
                    @Nonnull
                    @Override
                    public List<Pair<TextFormatting, String>> load(@Nonnull final ItemStack key) {
                        final ResourceLocation registryName = key.getItem().getRegistryName();
                        if (registryName == null) return new ArrayList<>();

                        final List<DocumentationEntry> candidates = DOCUMENTATION_CACHE.getUnchecked(registryName);
                        final List<Pair<TextFormatting, String>> lines = new ArrayList<>();

                        candidates.forEach(candidate -> {
                            boolean matches = false;
                            for (@Nonnull final Target target : candidate.getTargets()) {
                                if (matches) break;
                                final ItemStack stack = target.obtainTarget();

                                // First we check the items
                                matches = ItemStack.areItemsEqual(stack, key);

                                if (matches && stack.hasTagCompound()) {
                                    // This item stack has a tag compound: we need to go deeper
                                    matches = ItemStack.areItemStackTagsEqual(stack, key);

                                    if (!matches) {
                                        // We are going to match if and only if the "difference"
                                        // between the two is just the display name, unless the
                                        // second stack has itself a display name
                                        if (key.hasDisplayName() && stack.hasDisplayName()) continue;
                                        if (!key.hasDisplayName()) continue;

                                        stack.setStackDisplayName(key.getDisplayName());

                                        matches = ItemStack.areItemStackTagsEqual(stack, key);
                                    }
                                }

                                if (matches) {
                                    lines.addAll(TARGET_ENTRIES.get(target));
                                }
                            }
                        });

                        return lines;
                    }
                });
    }

    @Nonnull
    @Override
    public List<ResourceLocation> getCompatibleTypes() {
        return ImmutableList.of(new ResourceLocation("minecraft", "tooltip"));
    }

    @Override
    public void consumeData(@Nonnull final DocumentationData data, @Nonnull final Set<Target> targets) {
        targets.forEach(target -> TARGET_ENTRIES.computeIfAbsent(target, key -> new ArrayList<>()).addAll(this.convert(data.getData())));
    }

    @Nonnull
    private List<Pair<TextFormatting, String>> convert(@Nonnull final List<String> strings) {
        return strings.stream().map(this::convert).collect(Collectors.toList());
    }

    @Nonnull
    private Pair<TextFormatting, String> convert(@Nonnull final String string) {
        if (string.startsWith("[")) {
            // [reset], [dark_blue]
            final int indexOf = string.indexOf(']');
            final String sub = string.substring(1, indexOf);
            final String other = string.substring(indexOf + 1);
            return ImmutablePair.of(TextFormatting.getValueByName(sub), other);
        }
        return ImmutablePair.of(null, string);
    }

    @SubscribeEvent
    public static void onItemTooltip(@Nonnull final ItemTooltipEvent event) {
        STACKS_CACHE.getUnchecked(event.getItemStack()).forEach(it -> {
            final String line;
            if (it.getLeft() == null) {
                line = I18n.format(it.getRight());
            } else {
                line = "" + it.getLeft().toString() + I18n.format(it.getRight()) + TextFormatting.RESET.toString();
            }
            event.getToolTip().add(line);
        });
    }
}
