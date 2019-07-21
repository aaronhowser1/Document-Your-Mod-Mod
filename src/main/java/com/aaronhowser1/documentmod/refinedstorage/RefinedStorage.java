package com.aaronhowser1.documentmod.refinedstorage;


import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import static com.aaronhowser1.documentmod.DocumentModJEIIntegration.*;
import static com.aaronhowser1.documentmod.config.DYMMConfig.debugModIsDocumented;

@GameRegistry.ObjectHolder("refinedstorage")
public class RefinedStorage {

    public static final Item CONTROLLER = Items.AIR;
    public static final Item GRID = Items.AIR;
    public static final Item PORTABLE_GRID = Items.AIR;
    public static final Item CRAFTING_MONITOR = Items.AIR;
    public static final Item STORAGE_MONITOR = Items.AIR;
    public static final Item SECURITY_MANAGER = Items.AIR;
    public static final Item CRAFTER = Items.AIR;
    public static final Item DISK_DRIVE = Items.AIR;
    public static final Item STORAGE = Items.AIR;
    public static final Item FLUID_STORAGE = Items.AIR;
    public static final Item CABLE = Items.AIR;
    public static final Item IMPORTER = Items.AIR;
    public static final Item EXPORTER = Items.AIR;
    public static final Item CONSTRUCTOR = Items.AIR;
    public static final Item DESTRUCTOR = Items.AIR;
    public static final Item READER = Items.AIR;
    public static final Item WRITER = Items.AIR;
    public static final Item DETECTOR = Items.AIR;
    public static final Item RELAY = Items.AIR;
    public static final Item INTERFACE = Items.AIR;
    public static final Item FLUID_INTERFACE = Items.AIR;
    public static final Item WIRELESS_TRANSMITTER = Items.AIR;
    public static final Item NETWORK_TRANSMITTER = Items.AIR;
    public static final Item NETWORK_RECEIVER = Items.AIR;
    public static final Item DISK_MANIPULATOR = Items.AIR;
    public static final Item CRAFTER_MANAGER = Items.AIR;
    public static final Item CUTTING_TOOL = Items.AIR;
    public static final Item STORAGE_DISK = Items.AIR;
    public static final Item FLUID_STORAGE_DISK = Items.AIR;
    public static final Item PATTERN = Items.AIR;
    public static final Item WIRELESS_GRID = Items.AIR;
    public static final Item WIRELESS_FLUID_GRID = Items.AIR;
    public static final Item WIRELESS_CRAFTING_MONITOR = Items.AIR;
    public static final Item UPGRADE = Items.AIR;
    public static final Item FILTER = Items.AIR;
    public static final Item NETWORK_CARD = Items.AIR;
    public static final Item SECURITY_CARD = Items.AIR;
    public static final Item COVER = Items.AIR;
    public static final Item HOLLOW_COVER = Items.AIR;
    public static final Item WRENCH = Items.AIR;

    public static void init() {
        /*
        addItemInfo(CONTROLLER, "documentationmod.refinedstorage.controller");
        addItemWithDamageInfo(GRID,0,  "documentationmod.refinedstorage.grid");
        addItemWithDamageInfo(GRID,1,  "documentationmod.refinedstorage.craftinggrid");
        addItemWithDamageInfo(GRID,2,  "documentationmod.refinedstorage.patterngrid");
        addItemWithDamageInfo(GRID,3,  "documentationmod.refinedstorage.fluidgrid");
        addItemInfo(PORTABLE_GRID, "documentationmod.refinedstorage.portablegrid");
        addItemInfo(CRAFTING_MONITOR, "documentationmod.refinedstorage.craftingmonitor");
        addItemInfo(STORAGE_MONITOR, "documentationmod.refinedstorage.storagemonitor");
        addItemInfo(SECURITY_MANAGER, "documentationmod.refinedstorage.securitymanager");
        addItemInfo(CRAFTER, "documentationmod.refinedstorage.crafter");
        addItemInfo(DISK_DRIVE, "documentationmod.refinedstorage.diskdrive");
        addItemWithDamageInfo(STORAGE,0,  "documentationmod.refinedstorage.1kstorageblock");
        addItemWithDamageInfo(STORAGE,1,  "documentationmod.refinedstorage.4kstorageblock");
        addItemWithDamageInfo(STORAGE,2,  "documentationmod.refinedstorage.16kstorageblock");
        addItemWithDamageInfo(STORAGE,3,  "documentationmod.refinedstorage.64kstorageblock");
        addItemWithDamageInfo(FLUID_STORAGE, 0, "documentationmod.refinedstorage.64kfluidstorageblock");
        addItemWithDamageInfo(FLUID_STORAGE, 1, "documentationmod.refinedstorage.256kfluidstorageblock");
        addItemWithDamageInfo(FLUID_STORAGE, 2, "documentationmod.refinedstorage.1024kfluidstorageblock");
        addItemWithDamageInfo(FLUID_STORAGE, 3, "documentationmod.refinedstorage.4096kfluidstorageblock");
        addItemInfo(CABLE, "documentationmod.refinedstorage.cable");
        addItemInfo(IMPORTER, "documentationmod.refinedstorage.importer");
        addItemInfo(EXPORTER, "documentationmod.refinedstorage.exporter");
        addItemInfo(CONSTRUCTOR, "documentationmod.refinedstorage.constructor");
        addItemInfo(DESTRUCTOR, "documentationmod.refinedstorage.destructor");
        addItemInfo(READER, "documentationmod.refinedstorage.reader");
        addItemInfo(WRITER, "documentationmod.refinedstorage.writer");
        addItemInfo(DETECTOR, "documentationmod.refinedstorage.detector");
        addItemInfo(RELAY, "documentationmod.refinedstorage.relay");
        addItemInfo(INTERFACE, "documentationmod.refinedstorage.interface");
        addItemInfo(FLUID_INTERFACE, "documentationmod.refinedstorage.fluidinterface");
        addItemInfo(WIRELESS_TRANSMITTER, "documentationmod.refinedstorage.wirelesstransmitter");
        addItemInfo(NETWORK_TRANSMITTER, "documentationmod.refinedstorage.networktransmitter");
        addItemInfo(NETWORK_RECEIVER, "documentationmod.refinedstorage.networkreceiver");
        addItemInfo(DISK_MANIPULATOR, "documentationmod.refinedstorage.diskmanipulator");
        addItemInfo(CRAFTER_MANAGER, "documentationmod.refinedstorage.craftermanager");
        addItemInfo(CUTTING_TOOL, "documentationmod.refinedstorage.cuttingtool");
        addItemWithDamageInfo(STORAGE_DISK,0, "documentationmod.refinedstorage.1kstoragedisk");
        addItemWithDamageInfo(STORAGE_DISK,1, "documentationmod.refinedstorage.4kstoragedisk");
        addItemWithDamageInfo(STORAGE_DISK,2, "documentationmod.refinedstorage.16kstoragedisk");
        addItemWithDamageInfo(STORAGE_DISK,3, "documentationmod.refinedstorage.64kstoragedisk");
        addItemWithDamageInfo(FLUID_STORAGE_DISK,0,  "documentationmod.refinedstorage.64kfluidstoragedisk");
        addItemWithDamageInfo(FLUID_STORAGE_DISK,1,  "documentationmod.refinedstorage.256kfluidstoragedisk");
        addItemWithDamageInfo(FLUID_STORAGE_DISK,2,  "documentationmod.refinedstorage.1024kfluidstoragedisk");
        addItemWithDamageInfo(FLUID_STORAGE_DISK,3,  "documentationmod.refinedstorage.4096kfluidstorage");
        addItemInfo(PATTERN, "documentationmod.refinedstorage.pattern");
        addItemInfo(WIRELESS_GRID, "documentationmod.refinedstorage.wirelessgrid");
        addItemInfo(WIRELESS_FLUID_GRID, "documentationmod.refinedstorage.wirelessfluidgrid");
        addItemInfo(WIRELESS_CRAFTING_MONITOR, "documentationmod.refinedstorage.wirelesscraftingmonitor");
        addItemWithDamageInfo(UPGRADE,1, "documentationmod.refinedstorage.upgrade.range");
        addItemWithDamageInfo(UPGRADE,2, "documentationmod.refinedstorage.upgrade.speed");
        addItemWithDamageInfo(UPGRADE,3, "documentationmod.refinedstorage.upgrade.crafting");
        addItemWithDamageInfo(UPGRADE,4, "documentationmod.refinedstorage.upgrade.stack");
        addItemWithDamageInfo(UPGRADE,6, "documentationmod.refinedstorage.upgrade.silktouch");
        addItemWithDamageInfo(UPGRADE,7, "documentationmod.refinedstorage.upgrade.fortune");
        addItemWithDamageInfo(UPGRADE,8, "documentationmod.refinedstorage.upgrade.fortune");
        addItemWithDamageInfo(UPGRADE,9, "documentationmod.refinedstorage.upgrade.fortune");
        addItemInfo(FILTER, "documentationmod.refinedstorage.filter");
        addItemInfo(NETWORK_CARD, "documentationmod.refinedstorage.networkcard");
        addItemInfo(SECURITY_CARD, "documentationmod.refinedstorage.securitycard");
        addItemInfo(COVER, "documentationmod.refinedstorage.cover");
        addItemInfo(HOLLOW_COVER, "documentationmod.refinedstorage.hollowcover");
        addItemInfo(WRENCH, "documentationmod.refinedstorage.wrench");
         */

        if(debugModIsDocumented) System.out.println("Refined Storage documented");
    }
}
