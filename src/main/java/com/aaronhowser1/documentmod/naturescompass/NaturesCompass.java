package com.aaronhowser1.documentmod.naturescompass;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.addItemInfo;

@GameRegistry.ObjectHolder("naturescompass")
public class NaturesCompass{
    public static final Item NATURESCOMPASS = Items.AIR;

    public static void init() {
        addItemInfo(NATURESCOMPASS, "documentationmod.naturescompass");
    }
}
