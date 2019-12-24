package com.aaronhowser1.dymm.module.compatibility.refinedstorage.consume;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.ApiBindings;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;

@JEIPlugin
@SuppressWarnings("unused")
public final class CoverJeiPlugin implements IModPlugin {
    static final L LOG = L.create(Constants.MOD_NAME, "Refined Storage Integration JEI Plugin");

    @Override
    public void register(@Nonnull final IModRegistry registry) {
        LOG.info("Attempting to set up JEI integration for Refined Storage's covers");

        final RecipeRegistryCoverDocumentation plugin = new RecipeRegistryCoverDocumentation(registry.getJeiHelpers().getGuiHelper());

        if (plugin.isSuccessful() && !this.shallFallback()) {
            LOG.warn("Experimental behavior is enabled: this will not cause major issues but it may lead to weird errors in the logs.");
            LOG.warn("Do not be scared about stack-traces appearing when viewing documentation: that's normal and it's the reason of why this is marked as 'experimental'");
            registry.addRecipeRegistryPlugin(plugin);
        } else {
            if (this.shallFallback()) {
                LOG.info("Falling back to non-experimental mode as per request");
            } else {
                LOG.warn("Registration of experimental triggers has failed! Falling back to non-experimental mode!");
            }
            CoverJeiDocumentationConsumer.docData.forEach(it -> registry.addIngredientInfo(it.getLeft().obtainTarget(), VanillaTypes.ITEM, it.getRight().getData().toArray(new String[0])));
        }

        LOG.info("Set up completed");
    }

    private boolean shallFallback() {
        final Configuration configuration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor("refinedstorage");
        final ConfigCategory category = configuration.getCategory("experimental");
        final Property property = category.get("covers_jei_support");
        return !property.getBoolean();
    }
}
