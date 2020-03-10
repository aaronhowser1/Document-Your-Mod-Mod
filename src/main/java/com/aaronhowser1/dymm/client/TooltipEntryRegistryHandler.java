package com.aaronhowser1.dymm.client;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import com.aaronhowser1.dymm.api.documentation.Target;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Side.CLIENT)
public final class TooltipEntryRegistryHandler {
    private static final LoadingCache<ResourceLocation, List<DocumentationEntry>> DOCUMENTATION_CACHE;
    private static final LoadingCache<ItemStack, List<ResourceLocation>> STACKS_CACHE;

    private boolean tooltip;

    private TooltipEntryRegistryHandler() {
        this.readTooltipValue();
    }

    static {
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
                .build(new CacheLoader<ItemStack, List<ResourceLocation>>() {
                    @Nonnull
                    @Override
                    public List<ResourceLocation> load(@Nonnull final ItemStack key) {
                        final ResourceLocation registryName = key.getItem().getRegistryName();
                        if (registryName == null) return new ArrayList<>();

                        final List<DocumentationEntry> entryCandidates = DOCUMENTATION_CACHE.getUnchecked(registryName);
                        final List<ResourceLocation> correctTargets = new ArrayList<>();

                        entryCandidates.forEach(candidate -> {
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
                            }

                            if (!matches) return;

                            correctTargets.add(candidate.getRegistryName());
                        });

                        return correctTargets;
                    }
                });
    }

    @SubscribeEvent
    public static void onMainMenuGuiOpen(@Nonnull final GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu) {
            MinecraftForge.EVENT_BUS.unregister(TooltipEntryRegistryHandler.class);
            MinecraftForge.EVENT_BUS.register(new TooltipEntryRegistryHandler());
        }
    }

    @SubscribeEvent
    public void onConfigChanged(@Nonnull final ConfigChangedEvent event) {
        this.readTooltipValue();
    }


    @SubscribeEvent
    public void onTooltip(@Nonnull final ItemTooltipEvent event) {
        if (!this.tooltip) return;
        final List<ResourceLocation> targets = STACKS_CACHE.getUnchecked(event.getItemStack());
        if (targets.isEmpty()) return;
        final List<String> tooltip = event.getToolTip();
        tooltip.add(String.format("%s%s:%s", TextFormatting.DARK_AQUA, I18n.format("dymm.mod.debug.target_entry"), TextFormatting.RESET));
        targets.forEach(it -> tooltip.add(String.format("  %s- %s%s%s", TextFormatting.DARK_GRAY, TextFormatting.DARK_GRAY, it.toString(), TextFormatting.RESET)));
    }

    private void readTooltipValue() {
        final Configuration config = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(Constants.ConfigurationMain.NAME);
        this.tooltip = config.getBoolean(Constants.ConfigurationMain.PROPERTY_DEBUG_TARGET_ENTRIES, Constants.ConfigurationMain.CATEGORY_DEBUG,
                false, Constants.ConfigurationMain.PROPERTY_DEBUG_TARGET_ENTRIES_COMMENT);
    }
}
