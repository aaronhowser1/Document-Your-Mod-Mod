package com.aaronhowser1.dymm.module.base.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.documentation.Target;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.TargetFactory;
import com.aaronhowser1.dymm.module.base.BasicItemTarget;
import com.google.gson.JsonObject;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public final class MetadataRangeItemTargetFactory implements TargetFactory {
    @Nonnull
    @Override
    public List<Target> fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String registryName = JsonUtilities.getString(object, "registry_name");
        final int metadataBegin = JsonUtilities.getInt(object, "metadata_begin");
        final int metadataEnd = JsonUtilities.getInt(object, "metadata_end");
        final boolean startInclusive = JsonUtilities.getBooleanOrElse(object, "include_start", () -> true);
        final boolean endInclusive = JsonUtilities.getBooleanOrElse(object, "include_end", () -> true);

        final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
        if (item == null || item == Items.AIR) {
            state.getReporter().report("The registry name '" + registryName + "' does not match any known item: it will be skipped");
            return new ArrayList<>();
        }

        return IntStream.rangeClosed(metadataBegin + (startInclusive? 0 : 1), metadataEnd - (endInclusive? 0 : 1))
                .mapToObj(it -> new ItemStack(item, 1, it))
                .map(BasicItemTarget::new)
                .collect(Collectors.toList());
    }
}
