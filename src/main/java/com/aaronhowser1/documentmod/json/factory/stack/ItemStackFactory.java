package com.aaronhowser1.documentmod.json.factory.stack;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.json.DocumentationLoader;
import com.aaronhowser1.documentmod.json.factory.nbt.NbtTagFactory;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemStackFactory implements StackFactory {

    @Nonnull
    @Override
    public List<ItemStack> parseFromJson(@Nonnull final JsonObject jsonObject, @Nonnull final ResourceLocation name) {
        final String registryName = JsonUtils.getString(jsonObject, "registry_name");
        final int metadata = JsonUtils.getInt(jsonObject, "metadata", 0);
        final NBTTagCompound base = this.parseNbt(jsonObject, name);
        final Item item = this.getItemFromRegistryName(registryName, name);
        if (item == null) return ImmutableList.of();
        final ItemStack targetStack = new ItemStack(item, 1, metadata);
        if (base != null) {
            targetStack.deserializeNBT(base);
        }
        return ImmutableList.of(targetStack);
    }

    @Nullable
    protected Item getItemFromRegistryName(@Nonnull final String registryName, @Nonnull final ResourceLocation name) {
        final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
        if (item == null || item == Items.AIR) {
            DocumentMod.logger.warn("In entry '" + name + "': Item with given registry name '" + registryName + "' does not exist; skipping");
            return null;
        }
        return item;
    }

    @Nullable
    private NBTTagCompound parseNbt(@Nonnull final JsonObject jsonObject, @Nonnull final ResourceLocation name) {
        if (!jsonObject.has("nbt")) return null;
        if (!jsonObject.get("nbt").isJsonObject()) throw new JsonSyntaxException("nbt must be an object!");

        final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("id", "minecraft:air");
        compound.setByte("Count", (byte) 1);
        compound.setShort("Damage", (short) 0);

        final JsonObject nbt = jsonObject.get("nbt").getAsJsonObject();

        @SuppressWarnings("unchecked")
        final NbtTagFactory<NBTTagCompound> compoundParseFactory = DocumentationLoader.INSTANCE.getFactory(NbtTagFactory.class,
                new ResourceLocation(DocumentMod.MOD_ID, "compound"));

        compound.setTag("tag", Objects.requireNonNull(compoundParseFactory).parseFromJson(nbt, name, 0));

        return compound;
    }
}
