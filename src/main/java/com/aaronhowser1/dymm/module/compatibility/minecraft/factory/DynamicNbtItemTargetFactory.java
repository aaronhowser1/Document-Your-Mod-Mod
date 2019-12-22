package com.aaronhowser1.dymm.module.compatibility.minecraft.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.documentation.Target;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.TargetFactory;
import com.aaronhowser1.dymm.module.base.BasicItemTarget;
import com.google.gson.JsonObject;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public final class DynamicNbtItemTargetFactory implements TargetFactory {
    @Nonnull
    @Override
    public List<Target> fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String registryName = JsonUtilities.getString(object, "registry_name");
        final int metadata = JsonUtilities.getIntOrElse(object, "metadata", () -> -1); // metadata cannot be negative

        final List<Target> targets = new ArrayList<>();

        final Item mainItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
        if (mainItem == null || mainItem == Items.AIR) {
            state.getReporter().report("No dynamic NBT item with the given registry name '" + registryName + "' exists: addition will be skipped");
            return targets;
        }

        final NonNullList<ItemStack> nonNullList = NonNullList.create();
        mainItem.getSubItems(CreativeTabs.SEARCH, nonNullList);

        final Predicate<ItemStack> filter = metadata == -1? it -> true : it -> it.getMetadata() == metadata;
        nonNullList.stream().filter(filter).map(BasicItemTarget::new).forEach(targets::add);
        return targets;
    }
}
