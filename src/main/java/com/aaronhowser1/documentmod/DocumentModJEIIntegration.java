package com.aaronhowser1.documentmod;

import com.aaronhowser1.documentmod.config.DYMMConfig;
import com.aaronhowser1.documentmod.json.DocumentationRegistry;
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
            DocumentationRegistry.INSTANCE.getDocumentationForMod(container).forEach(modDocumentation -> {
                final String language = Minecraft.getMinecraft().gameSettings.language;
                final List<String> strings = modDocumentation.getStringsFor(language);
                if (!strings.isEmpty()) {
                    final String[] stringsArray = strings.toArray(new String[0]);
                    r.addIngredientInfo(modDocumentation.getReferredStack(), VanillaTypes.ITEM, stringsArray);
                }
            });
            if (DYMMConfig.debugModIsDocumented) DocumentMod.logger.info("Mod " + container.getName() + " is documented");
        });
    }
}
