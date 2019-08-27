package com.aaronhowser1.documentmod.client;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.json.DocumentationRegistry;
import com.aaronhowser1.documentmod.json.ModDocumentation;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(modid = DocumentMod.MODID, value = Side.CLIENT)
public final class TooltipHandler {

    private static final LoadingCache<ResourceLocation, List<Pair<TextFormatting, String>>> CACHE;

    private TooltipHandler() {}

    static {
        CACHE = CacheBuilder.newBuilder()
                .maximumSize(500)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<ResourceLocation, List<Pair<TextFormatting, String>>>() {
                    @Override
                    public List<Pair<TextFormatting, String>> load(@Nonnull final ResourceLocation key) {
                        return DocumentationRegistry.INSTANCE.getDocumentationForMod(key.getNamespace()).stream()
                                .filter(it -> it.getReferredStack().getItem().getRegistryName() != null
                                        && it.getReferredStack().getItem().getRegistryName().equals(key))
                                .map(ModDocumentation::getTooltipKeys)
                                .findFirst()
                                .orElseGet(ImmutableList::of);
                    }
                });
    }

    @SubscribeEvent
    public static void onItemTooltipEvent(@Nonnull final ItemTooltipEvent event) {
        final Item item = event.getItemStack().getItem();
        final ResourceLocation registryName = item.getRegistryName();
        if (registryName == null) return;
        final List<Pair<TextFormatting, String>> lines = CACHE.getUnchecked(registryName);
        lines.forEach(it -> {
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
