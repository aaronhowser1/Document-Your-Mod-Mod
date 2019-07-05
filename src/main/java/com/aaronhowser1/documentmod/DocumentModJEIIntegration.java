package com.aaronhowser1.documentmod;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import static com.aaronhowser1.documentmod.config.DYMMConfig.*;

@JEIPlugin
public class DocumentModJEIIntegration implements IModPlugin
{
    public static IModRegistry registry;
    @Override
    public void register(IModRegistry r)
    {
        registry = r;
        if(useQuark) {
            QuarkItems.init();
        }
    }
    public static void addInfo(Item item, String desc)
    {
        if(item != Items.AIR) registry.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM, desc);
    }
}
