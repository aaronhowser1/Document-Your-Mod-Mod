package com.aaronhowser1.dymm.module.base.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.documentation.Target;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.TargetFactory;
import com.aaronhowser1.dymm.module.base.BasicItemTarget;
import com.google.gson.JsonObject;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class SpawnEggTargetFactory implements TargetFactory {
    @Nonnull
    @Override
    public List<Target> fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String entityId = JsonUtilities.getString(object, "id");
        final ItemStack stack = new ItemStack(Items.SPAWN_EGG, 1, 0);
        stack.deserializeNBT(this.createNbt(state, entityId));
        final List<Target> target = new ArrayList<>();
        target.add(new BasicItemTarget(stack));
        return target;
    }

    @Nonnull
    private NBTTagCompound createNbt(@Nonnull final GlobalLoadingState state, @Nonnull final String id) {
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("tag", this.createTag(state, id));
        return tag;
    }

    @Nonnull
    private NBTTagCompound createTag(@Nonnull final GlobalLoadingState state, @Nonnull final String id) {
        final NBTTagCompound tagCompound = new NBTTagCompound();
        final NBTTagCompound entityTag = new NBTTagCompound();
        entityTag.setString("id", this.checkIdValid(state, id));
        tagCompound.setTag("EntityTag", entityTag);
        return tagCompound;
    }

    @Nonnull
    private String checkIdValid(@Nonnull final GlobalLoadingState state, @Nonnull final String id) {
        final EntityEntry entry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id));
        if (entry == null) {
            state.getReporter().report("The given entity ID '" + id + "' does not exist: the entry will be registered anyway, but it won't work!");
        }
        return id;
    }
}
