package com.aaronhowser1.documentmod.refinedstorage;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.*;
import static com.aaronhowser1.documentmod.config.DYMMConfig.debugModIsDocumented;

@GameRegistry.ObjectHolder("refinedstorageaddons")
public class RefinedStorageAddons {
    public static final Item WIRELESS_CRAFTING_GRID = Items.AIR;

    public static void init() {
        //addItemInfo(WIRELESS_CRAFTING_GRID, "documentationmod.refinedstorageaddons.wirelesscraftinggrid");

        if(debugModIsDocumented) System.out.println("Refined Storage Addons documented");
    }
}
