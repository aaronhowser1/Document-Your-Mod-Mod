package com.aaronhowser1.documentmod.proxy;

import com.aaronhowser1.documentmod.json.DocumentationLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(@Nonnull FMLPreInitializationEvent event) {
        super.preInit(event);
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(
                (ISelectiveResourceReloadListener) (resourceManager, resourcePredicate) -> {
            if (resourcePredicate.test(VanillaResourceType.LANGUAGES) || resourcePredicate.test(VanillaResourceType.TEXTURES)) {
                DocumentationLoader.INSTANCE.loadFromJson();
            }
        });
    }
}
