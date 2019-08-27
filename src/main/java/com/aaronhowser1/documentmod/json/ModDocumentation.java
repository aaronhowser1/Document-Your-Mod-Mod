package com.aaronhowser1.documentmod.json;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.json.factory.stack.StackFactory;
import com.aaronhowser1.documentmod.utility.TranslationUtility;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class ModDocumentation extends IForgeRegistryEntry.Impl<ModDocumentation> {

    private final List<ItemStack> itemStacks;
    private final List<String> translationKeys;
    private final List<Pair<TextFormatting, String>> tooltipKeys;

    private ModDocumentation(@Nonnull final List<ItemStack> itemStack, @Nonnull final List<String> translationKeys,
                             @Nonnull final List<Pair<TextFormatting, String>> tooltipKeys, @Nonnull final ResourceLocation registryName) {
        this.itemStacks = ImmutableList.copyOf(itemStack);
        this.translationKeys = ImmutableList.copyOf(translationKeys);
        this.tooltipKeys = ImmutableList.copyOf(tooltipKeys);
        this.setRegistryName(registryName);
    }

    @Nullable
    static ModDocumentation create(@Nonnull final JsonObject object, @Nonnull final ResourceLocation name) {
        final List<ItemStack> stacks = getItemStacksIntoList(JsonUtils.getJsonArray(object, "for"));
        if (stacks.isEmpty()) return null;

        final List<String> translationKeys = parseTranslationKeys(JsonUtils.getJsonArray(object, "documentation"));
        final List<Pair<TextFormatting, String>> tooltipKeys = parseTooltipKeys(object);

        return new ModDocumentation(stacks.stream().map(ItemStack::copy).collect(Collectors.toList()), translationKeys, tooltipKeys, name);
    }

    @Nonnull
    private static List<ItemStack> getItemStacksIntoList(@Nonnull final JsonArray jsonArray) {
        final List<ItemStack> returningList = Lists.newArrayList();
        jsonArray.forEach(element -> {
            if (!element.isJsonObject()) throw new JsonSyntaxException("for elements must be objects");
            final JsonObject jsonObject = element.getAsJsonObject();
            final List<ItemStack> stack = parseItemStackJsonObject(jsonObject);
            if (!stack.isEmpty()) returningList.addAll(stack);
        });
        return returningList;
    }

    @Nonnull
    private static List<ItemStack> parseItemStackJsonObject(@Nonnull final JsonObject jsonObject) {
        final String type = JsonUtils.getString(jsonObject, "type");
        if (type.trim().isEmpty()) throw new JsonSyntaxException("Type cannot be empty");
        if (type.indexOf(':') == -1) throw new JsonSyntaxException("Missing namespace for type '" + type + "'");
        final StackFactory stackFactory = DocumentationLoader.INSTANCE.getFactory(StackFactory.class, new ResourceLocation(type));
        if (stackFactory == null) {
            throw new JsonParseException("Unable to find stack factory for given type " + type);
        }
        return stackFactory.parseFromJson(jsonObject);
    }

    @Nonnull
    private static List<String> parseTranslationKeys(@Nonnull final JsonArray array) {
        if (array.size() <= 0) throw new JsonSyntaxException("The documentation array must contain at least one string");
        final List<String> translationKeys = Lists.newArrayList();
        for (int i = 0; i < array.size(); ++i) {
            final String key = JsonUtils.getString(array.get(i), "documentation[" + i + "]");
            if (!TranslationUtility.INSTANCE.canTranslate(key)) DocumentMod.logger.warn("Found non-translated key " + key + ". Please check your language file");
            translationKeys.add(key);
        }
        return translationKeys;
    }

    @Nonnull
    private static List<Pair<TextFormatting, String>> parseTooltipKeys(@Nonnull final JsonObject object) {
        if (!object.has("tooltip")) return ImmutableList.of();
        return parseTooltipKeys(JsonUtils.getJsonArray(object, "tooltip"));
    }

    @Nonnull
    private static List<Pair<TextFormatting, String>> parseTooltipKeys(@Nonnull final JsonArray array) {
        final List<Pair<TextFormatting, String>> list = Lists.newArrayList();
        array.forEach(element -> {
            final Pair<TextFormatting, String> pair;
            if (element.isJsonPrimitive()) {
                // It is a string
                final String string = JsonUtils.getString(element, "tooltip[?]");
                pair = ImmutablePair.of(null, string);
            } else if (element.isJsonObject()) {
                final JsonObject jsonObject = element.getAsJsonObject();
                final String key = JsonUtils.getString(jsonObject, "key");
                final String formatting = JsonUtils.getString(jsonObject, "formatting");
                TextFormatting textFormatting;
                try {
                    textFormatting = TextFormatting.valueOf(formatting.toUpperCase(Locale.ENGLISH));
                } catch (final IllegalArgumentException e) {
                    DocumentMod.logger.warn("No such formatting value exists for " + formatting + "! Reverting to no formatting: please check your JSON files");
                    textFormatting = null;
                }
                pair = ImmutablePair.of(textFormatting, key);
            } else {
                throw new JsonSyntaxException("Array elements of tooltip can be only Strings or Objects");
            }
            if (!TranslationUtility.INSTANCE.canTranslate(pair.getRight())) DocumentMod.logger.warn("Found non-translated key " + pair.getRight() + ". Please check your language file");
            list.add(pair);
        });
        return list;
    }

    @Nonnull
    public List<ItemStack> getReferredStacks() {
        return ImmutableList.copyOf(this.itemStacks);
    }

    @Nonnull
    public List<String> getTranslationKeys() {
        return ImmutableList.copyOf(this.translationKeys);
    }

    @Nonnull
    public List<Pair<TextFormatting, String>> getTooltipKeys() {
        return ImmutableList.copyOf(this.tooltipKeys);
    }

    @Override
    public String toString() {
        return "ModDocumentation(" + this.hashCode() + "){" +
                "itemStacks=" + this.itemStacks +
                ", translationKeys=" + this.translationKeys +
                ", tooltipKeys=" + this.tooltipKeys +
                '}';
    }
}
