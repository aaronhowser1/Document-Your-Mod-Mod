package com.aaronhowser1.documentmod.client;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.json.DocumentationRegistry;
import com.aaronhowser1.documentmod.json.ModDocumentation;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = DocumentMod.MOD_ID, value = Side.CLIENT)
public final class TooltipHandler {

    private static final LoadingCache<ResourceLocation, List<ModDocumentation>> MOD_DOCUMENTATION_CACHE;
    private static final LoadingCache<ItemStack, List<Pair<TextFormatting, String>>> STACKS_CACHE;

    private TooltipHandler() {}

    static {
        MOD_DOCUMENTATION_CACHE = CacheBuilder.newBuilder()
                .maximumSize(500)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<ResourceLocation, List<ModDocumentation>>() {
                    @Nonnull
                    @Override
                    public List<ModDocumentation> load(@Nonnull final ResourceLocation key) {
                        return DocumentationRegistry.INSTANCE.getRegistry().getEntries().stream()
                                .map(Map.Entry::getValue)
                                .filter(it -> it.getReferredStacks().stream()
                                        .map(ItemStack::getItem)
                                        .map(Item::getRegistryName)
                                        .filter(Objects::nonNull)
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
                        if (registryName == null) return Lists.newArrayList();

                        final List<ModDocumentation> documentationCandidates = MOD_DOCUMENTATION_CACHE.getUnchecked(registryName);
                        final List<Pair<TextFormatting, String>> lines = Lists.newArrayList();

                        documentationCandidates.forEach(candidate -> {
                            boolean matches = false;
                            for (@Nonnull final ItemStack stack : candidate.getReferredStacks()) {
                                if (matches) break;

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
                            }

                            if (!matches) return;

                            lines.addAll(candidate.getTooltipKeys());
                        });

                        return lines;
                    }
                });
    }

    @SubscribeEvent
    public static void onItemTooltipEvent(@Nonnull final ItemTooltipEvent event) {
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
