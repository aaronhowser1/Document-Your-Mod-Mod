package com.aaronhowser1.dymm.client;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Side.CLIENT)
public final class MainMenuDocumentedTargetsHandler {
    @SuppressWarnings("unchecked")
    private static final class LazyConstants {
        private static final int DOCUMENTED_TARGETS_COUNT;

        private static final Field BRAND;
        private static final Field BRAND_NO_MC;

        private static final List<String> PREVIOUS_BRAND_LIST;
        private static final List<String> PREVIOUS_BRAND_NO_MC_LIST;

        static {
            FMLCommonHandler.instance().computeBranding();

            DOCUMENTED_TARGETS_COUNT = (int) ApiBindings.getMainApi()
                    .getDocumentationRegistry()
                    .getValuesCollection()
                    .stream()
                    .map(DocumentationEntry::getRegistryName)
                    .peek(Objects::requireNonNull)
                    .map(ResourceLocation::getNamespace)
                    .distinct()
                    .count();

            try {
                final Class<?> handler = FMLCommonHandler.instance().getClass();

                BRAND = handler.getDeclaredField("brandings");
                BRAND_NO_MC = handler.getDeclaredField("brandingsNoMC");
                BRAND.setAccessible(true);
                BRAND_NO_MC.setAccessible(true);

                PREVIOUS_BRAND_LIST = new ArrayList<>((Collection<String>) BRAND.get(FMLCommonHandler.instance()));
                PREVIOUS_BRAND_NO_MC_LIST = new ArrayList<>((Collection<String>) BRAND_NO_MC.get(FMLCommonHandler.instance()));
            } catch (@Nonnull final ReflectiveOperationException e) {
                throw new RuntimeException("Unable to reflectively get the handler class!", e);
            }
        }
    }

    private static boolean prevShallBrand = false;

    @SubscribeEvent
    public static void onOpenGuiEvent(@Nonnull final GuiOpenEvent event) {
        if (!(event.getGui() instanceof GuiMainMenu)) return;
        final Configuration configuration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(Constants.ConfigurationMain.NAME);
        final boolean shallBrand = configuration.getBoolean(Constants.ConfigurationMain.PROPERTY_FOOLERY_BRANDING_TIME, Constants.ConfigurationMain.CATEGORY_FOOLERY,
                false, Constants.ConfigurationMain.PROPERTY_FOOLERY_BRANDING_TIME_COMMENT);

        if (prevShallBrand != shallBrand) {
            prevShallBrand = shallBrand;

            final List<String> brandList = new ArrayList<>(LazyConstants.PREVIOUS_BRAND_LIST);
            final List<String> brandListNoMc = new ArrayList<>(LazyConstants.PREVIOUS_BRAND_NO_MC_LIST);

            if (shallBrand) {
                /*mutable*/
                int targetIndex = -1;
                for (int i = 0; i < brandList.size(); ++i) {
                    if (brandList.get(i).contains("active") && brandList.get(i).contains("mod")) {
                        targetIndex = i;
                        break;
                    }
                }
                if (targetIndex == -1) return;

                final String s = LazyConstants.DOCUMENTED_TARGETS_COUNT != 1? "s" : "";
                brandList.set(targetIndex, brandList.get(targetIndex) + String.format(", %d mod%s documented", LazyConstants.DOCUMENTED_TARGETS_COUNT, s));
                brandListNoMc.set(targetIndex - 1, brandListNoMc.get(targetIndex - 1) + String.format(", %d mod%s documented", LazyConstants.DOCUMENTED_TARGETS_COUNT, s));
            }

            try {
                LazyConstants.BRAND.set(FMLCommonHandler.instance(), brandList);
                LazyConstants.BRAND_NO_MC.set(FMLCommonHandler.instance(), brandListNoMc);
            } catch (@Nonnull final ReflectiveOperationException ignored) {
                // And no brand will be set
            }
        }
    }
}
