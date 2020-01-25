package com.aaronhowser1.dymm.module.compatibility.refinedstorage.consume;

import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IIngredientType;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeRegistryPlugin;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class RecipeRegistryCoverDocumentation implements IRecipeRegistryPlugin {
    private final boolean successful;
    private final IGuiHelper helper;
    private Method create;

    RecipeRegistryCoverDocumentation(@Nonnull final IGuiHelper helper) {
        try {
            final Class<?> iir = Class.forName("mezz.jei.plugins.jei.info.IngredientInfoRecipe");
            this.create = iir.getDeclaredMethod("create", IGuiHelper.class, List.class, IIngredientType.class, String[].class);
            this.create.setAccessible(true);
        } catch (@Nonnull final ReflectiveOperationException e) {
            CoverJeiPlugin.LOG.bigError("We were unable to register the integration for covers: falling back to manual mode!");
            this.create = null;
        }
        this.successful = this.create != null;
        this.helper = helper;
    }

    @Nonnull
    @Override
    public <V> List<String> getRecipeCategoryUids(@Nonnull final IFocus<V> focus) {
        final List<String> categories = new ArrayList<>();
        if (focus.getValue() instanceof ItemStack) {
            final ItemStack stack = (ItemStack) focus.getValue();
            final ResourceLocation registryName = Objects.requireNonNull(stack.getItem().getRegistryName());

            if ("refinedstorage".equals(registryName.getNamespace()) && ("cover".equals(registryName.getPath())) || "hollow_cover".equals(registryName.getPath())) {
                categories.add(VanillaRecipeCategoryUid.INFORMATION);
            }
        }
        return categories;
    }

    @Nonnull
    @Override
    public <T extends IRecipeWrapper, V> List<T> getRecipeWrappers(@Nonnull final IRecipeCategory<T> recipeCategory, @Nonnull final IFocus<V> focus) {
        final List<T> recipeWrappers = new ArrayList<>();
        if (VanillaRecipeCategoryUid.INFORMATION.equals(recipeCategory.getUid()) && focus.getValue() instanceof ItemStack) {
            final ItemStack stack = (ItemStack) focus.getValue();
            final ResourceLocation regName = Objects.requireNonNull(stack.getItem().getRegistryName());
            if ("refinedstorage".equals(regName.getNamespace()) && ("cover".equals(regName.getPath())) || "hollow_cover".equals(regName.getPath())) {
                final String[] descKeys = CoverJeiDocumentationConsumer.docData.stream()
                        .filter(it -> regName.equals(it.getLeft().obtainTarget().getItem().getRegistryName()))
                        .findFirst()
                        .map(Pair::getRight)
                        .map(DocumentationData::getData)
                        .map(it -> it.toArray(new String[0]))
                        .orElse(new String[0]);
                final T t = this.createT(stack, descKeys);
                if (t != null) recipeWrappers.add(t);
            }
        }
        return recipeWrappers;
    }

    @Nonnull
    @Override
    public <T extends IRecipeWrapper> List<T> getRecipeWrappers(@Nonnull final IRecipeCategory<T> recipeCategory) {
        return new ArrayList<>();
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <T, U> T createT(@Nonnull final U ingredient, @Nonnull final String... descKeys) {
        return (T) this.create(ingredient, descKeys);
    }

    @Nullable
    private <T> IRecipeWrapper create(@Nonnull final T ingredient, @Nonnull final String... descKeys) {
        final List<T> ingredients = Collections.singletonList(ingredient);
        final List<IRecipeWrapper> irw = this.multiCreate(ingredients, descKeys);
        if (irw == null) return null;
        return irw.get(0);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <T> List<IRecipeWrapper> multiCreate(@Nonnull final List<T> ingredients, @Nonnull final String... descKeys) {
        try {
            return (List<IRecipeWrapper>) this.create.invoke(null, this.helper, ingredients, VanillaTypes.ITEM, descKeys);
        } catch (@Nonnull final ReflectiveOperationException e) {
            CoverJeiPlugin.LOG.bigError("Unable to create documentation entry for ingredients " + ingredients);
            return null;
        }
    }

    boolean isSuccessful() {
        return this.successful;
    }
}
