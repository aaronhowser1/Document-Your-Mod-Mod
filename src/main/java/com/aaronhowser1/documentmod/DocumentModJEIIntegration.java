package com.aaronhowser1.documentmod;

import com.aaronhowser1.documentmod.config.DYMMConfig;
import com.aaronhowser1.documentmod.json.DocumentationRegistry;
import com.aaronhowser1.documentmod.json.ModDocumentation;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.util.List;

@JEIPlugin
public class DocumentModJEIIntegration implements IModPlugin
{
    @Override
    public void register(@Nonnull IModRegistry r)
    {
        Loader.instance().getActiveModList().forEach(container -> {
            final List<ModDocumentation> modDocumentations = DocumentationRegistry.INSTANCE.getDocumentationForMod(container);
            modDocumentations.forEach(modDocumentation -> {
                final List<String> strings = modDocumentation.getTranslationKeys();
                if (!strings.isEmpty()) {
                    final String[] stringsArray = strings.toArray(new String[0]);
                    r.addIngredientInfo(modDocumentation.getReferredStack(), VanillaTypes.ITEM, stringsArray);
                }
            });
            if (DYMMConfig.debugModIsDocumented && !modDocumentations.isEmpty()) {
                DocumentMod.logger.info("Mod candidate " + container.getName() + " is documented");
            } else if (DYMMConfig.debugModIsDocumented) {
                DocumentMod.logger.info("No documentation found for mod candidate " + container.getName());
            }
        });
    }
}
