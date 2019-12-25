package com.aaronhowser1.dymm.module.compatibility.jei.consume;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.consume.DocumentationDataConsumer;
import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import com.aaronhowser1.dymm.api.documentation.Target;
import com.google.common.collect.ImmutableList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JEIPlugin
public final class JeiDocumentationConsumer implements DocumentationDataConsumer, IModPlugin {
    private static final L LOG = L.create(Constants.MOD_NAME, "JEI Integration");

    private static final List<Pair<DocumentationData, Set<Target>>> DATA = new ArrayList<>();

    @Nonnull
    @Override
    public List<ResourceLocation> getCompatibleTypes() {
        return ImmutableList.of(new ResourceLocation("jei", "information"));
    }

    @Override
    public void consumeData(@Nonnull final DocumentationData data, @Nonnull final Set<Target> targets) {
        DATA.add(ImmutablePair.of(data, targets));
    }

    @Override
    public void register(final IModRegistry registry) {
        LOG.info("It's time: JEI are you ready to get a lot of sweet data?");
        LOG.info("Beginning integration");
        DATA.forEach(it -> {
            final Set<ItemStack> targets = it.getRight()
                    .stream()
                    .map(Target::obtainTarget)
                    .collect(Collectors.toSet());
            final String[] pages = it.getLeft()
                    .getData()
                    .stream()
                    .peek(this::checkLine)
                    .map(I18n::format)
                    .toArray(String[]::new);
            targets.forEach(target -> registry.addIngredientInfo(target, VanillaTypes.ITEM, pages));
        });
        LOG.info("Integration completed: registered a total of " + DATA.size() + " entries");
    }

    private void checkLine(@Nonnull final String line) {
        if (!I18n.hasKey(line)) LOG.warn("Found not localized line '" + line + "' in one of the entries! Check your language file");
    }
}
