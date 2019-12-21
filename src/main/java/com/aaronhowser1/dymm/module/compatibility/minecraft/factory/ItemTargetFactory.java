package com.aaronhowser1.dymm.module.compatibility.minecraft.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.documentation.Target;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.TargetFactory;
import com.aaronhowser1.dymm.module.base.BasicItemTarget;
import com.aaronhowser1.dymm.module.base.nbt.NbtFactoryRegistry;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class ItemTargetFactory implements TargetFactory {
    @Nonnull
    @Override
    public List<Target> fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String registryName = JsonUtilities.getString(object, "registry_name");
        final int metadata = JsonUtilities.getIntOrElse(object, "metadata", () -> 0);
        final NBTTagCompound nbtCompound = this.parseNbt(state, object);

        final List<Target> returnList = new ArrayList<>();
        final Item item = this.getFromRegistry(state, registryName);

        if (item == null) return returnList;

        final ItemStack stack = new ItemStack(item, 1, metadata);
        if (nbtCompound != null) stack.deserializeNBT(nbtCompound);

        returnList.add(new BasicItemTarget(stack));

        return returnList;
    }

    @Nullable
    private NBTTagCompound parseNbt(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        if (!object.has("nbt")) return null;
        return this.convertToNbt(state, JsonUtilities.getJsonObject(object, "nbt"));
    }

    @Nonnull
    private NBTTagCompound convertToNbt(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject nbt) {
        final NBTTagCompound base = new NBTTagCompound();
        base.setString("id", "minecraft:air");
        base.setByte("Count", (byte) 1);
        base.setShort("Damage", (short) 0);
        try {
            base.setTag("tag", NbtFactoryRegistry.INSTANCE.getForType("compound").parseFromJson("tag", nbt, 0));
        } catch (@Nonnull final Exception e) {
            state.getReporter().interrupt("An error has occurred while attempting to read the NBT from the JSON file: maybe a syntax error?");
            throw new JsonParseException(e);
        }
        return base;
    }

    @Nullable
    private Item getFromRegistry(@Nonnull final GlobalLoadingState state, @Nonnull final String registryName) {
        return this.getFromRegistry(state, new ResourceLocation(registryName));
    }

    @Nullable
    private Item getFromRegistry(@Nonnull final GlobalLoadingState state, @Nonnull final ResourceLocation registryName) {
        final Item item = ForgeRegistries.ITEMS.getValue(registryName);
        if (item == null || item == Items.AIR) {
            state.getReporter().notify("No item with the given registry name '" + registryName + "' exists: addition will be skipped");
            return null;
        }
        return item;
    }
}
