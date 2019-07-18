package com.aaronhowser1.documentmod.waystones;


import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.addItemInfo;
import static com.aaronhowser1.documentmod.config.DYMMConfig.debugModIsDocumented;

@GameRegistry.ObjectHolder("waystones")
public class Waystones {
    public static final Item WAYSTONE = Items.AIR;
    public static final Item RETURN_SCROLL = Items.AIR;
    public static final Item BOUND_SCROLL = Items.AIR;
    public static final Item WARP_SCROLL = Items.AIR;
    public static final Item WARP_STONE = Items.AIR;

    public static void init() {
        addItemInfo(WAYSTONE, "documentationmod.waystone.waystone");
        addItemInfo(RETURN_SCROLL, "documentationmod.waystone.return");
        addItemInfo(BOUND_SCROLL, "documentationmod.waystone.bound");
        addItemInfo(WARP_SCROLL, "documentationmod.waystone.warpscroll");
        addItemInfo(WARP_STONE, "documentationmod.waystone.warpstone");

        if(debugModIsDocumented) System.out.println("Waystones is documented");
    }
}
