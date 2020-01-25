package com.aaronhowser1.dymm.module.base.factory;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.documentation.Target;
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState;
import com.aaronhowser1.dymm.api.loading.factory.TargetFactory;
import com.aaronhowser1.dymm.module.base.BasicItemTarget;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class PotionTargetFactory implements TargetFactory {
    private enum PotionType {
        NORMAL(Items.POTIONITEM),
        SPLASH(Items.SPLASH_POTION),
        LINGERING(Items.LINGERING_POTION),
        ARROW_HEAD(Items.TIPPED_ARROW);

        private final Item correspondingItem;

        PotionType(@Nonnull final Item correspondingItem) {
            this.correspondingItem = correspondingItem;
        }

        @Nonnull
        private Item getCorrespondingItem() {
            return this.correspondingItem;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ENGLISH);
        }
    }

    @Nonnull
    @Override
    public List<Target> fromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final Set<PotionType> types = this.getTypesFromJson(state, object);
        final String effect = this.checkPresence(state, JsonUtilities.getString(object, "effect"));
        return types.stream()
                .map(it -> this.convertToItem(it, effect))
                .map(BasicItemTarget::new)
                .collect(Collectors.toList());
    }

    @Nonnull
    private Set<PotionType> getTypesFromJson(@Nonnull final GlobalLoadingState state, @Nonnull final JsonObject object) {
        final String type = JsonUtilities.getString(object, "kind");
        if ("all".equals(type)) {
            state.getReporter().report("Found deprecated 'all' kind, it will match " + Arrays.toString(PotionType.values()) + ": prefer specifying them explicitly!");
            return new HashSet<>(Arrays.asList(PotionType.values()));
        }
        final Set<PotionType> types = new HashSet<>();
        Arrays.stream(PotionType.values()).filter(it -> type.equals(it.toString())).findFirst().ifPresent(types::add);
        if (types.isEmpty()) {
            throw new JsonParseException("The given kind '" + type + "' is not recognized as the ID of a valid potion type. Valid ones are: " + Arrays.toString(PotionType.values()));
        }
        return types;
    }

    @Nonnull
    private String checkPresence(@Nonnull final GlobalLoadingState state, @Nonnull final String effect) {
        final net.minecraft.potion.PotionType vanillaType = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(effect));
        if (vanillaType == null || new ResourceLocation("empty").equals(vanillaType.getRegistryName())) {
            final Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effect));
            if (potion == null) {
                state.getReporter().report("The given effect '" + effect + "' does not exist: the entry will be registered anyway, but it won't work!");
            } else {
                state.getReporter().report("The given effect '" + effect + "' is the name of a potion: you need to use the potion type, otherwise the entry will not work");
            }
        }
        return effect;
    }

    @Nonnull
    private ItemStack convertToItem(@Nonnull final PotionType type, @Nonnull final String effect) {
        final Item item = type.getCorrespondingItem();
        final NBTTagCompound tag = this.generateTagCompound(effect);
        final ItemStack stack = new ItemStack(item, 1, 0, tag);
        stack.setTagCompound(tag);
        return stack;
    }

    @Nonnull
    private NBTTagCompound generateTagCompound(@Nonnull final String effect) {
        final NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("Potion", effect);
        return tagCompound;
    }
}
