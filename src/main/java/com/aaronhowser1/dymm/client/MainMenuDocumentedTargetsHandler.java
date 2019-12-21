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
    private static final int DOCUMENTED_TARGETS_COUNT;

    static {
        DOCUMENTED_TARGETS_COUNT = (int) ApiBindings.getMainApi()
                .getDocumentationRegistry()
                .getValuesCollection()
                .stream()
                .map(DocumentationEntry::getRegistryName)
                .peek(Objects::requireNonNull)
                .map(ResourceLocation::getNamespace)
                .distinct()
                .count();
    }

    @SubscribeEvent
    public static void onOpenGuiEvent(@Nonnull final GuiOpenEvent event) {
        if (!(event.getGui() instanceof GuiMainMenu)) return;
        final Configuration configuration = ApiBindings.getMainApi().getConfigurationManager().getConfigurationFor(Constants.CONFIGURATION_MAIN);
        if (!configuration.get(Constants.CONFIGURATION_MAIN_FOOLERY_CATEGORY, "branding_time", false).getBoolean()) return;
        FMLCommonHandler.instance().computeBranding();
        try {
            final Class<?> handler = FMLCommonHandler.instance().getClass();

            final Field brand = handler.getDeclaredField("brandings");
            final Field brandNoMc = handler.getDeclaredField("brandingsNoMC");
            brand.setAccessible(true);
            brandNoMc.setAccessible(true);

            @SuppressWarnings("unchecked")
            final List<String> brandList = new ArrayList<>((Collection<String>) brand.get(FMLCommonHandler.instance()));
            @SuppressWarnings("unchecked")
            final List<String> brandListNoMc = new ArrayList<>((Collection<String>) brandNoMc.get(FMLCommonHandler.instance()));

            /*mutable*/ int targetIndex = -1;
            for (int i = 0; i < brandList.size(); ++i) {
                if (brandList.get(i).contains("active") && brandList.get(i).contains("mod") && !brandList.get(i).contains("documented")) {
                    targetIndex = i;
                    break;
                }
            }
            if (targetIndex == -1) return;

            brandList.set(targetIndex, brandList.get(targetIndex) + String.format(", %d mod%s documented", DOCUMENTED_TARGETS_COUNT, DOCUMENTED_TARGETS_COUNT != 1? "s" : ""));
            brandListNoMc.set(targetIndex - 1, brandListNoMc.get(targetIndex - 1) + String.format(", %d mod%s documented", DOCUMENTED_TARGETS_COUNT, DOCUMENTED_TARGETS_COUNT != 1? "s" : ""));

            brand.set(FMLCommonHandler.instance(), brandList);
            brandNoMc.set(FMLCommonHandler.instance(), brandListNoMc);
        } catch (@Nonnull final ReflectiveOperationException ignored) {
            // We won't edit the branding then
        }
    }
}
