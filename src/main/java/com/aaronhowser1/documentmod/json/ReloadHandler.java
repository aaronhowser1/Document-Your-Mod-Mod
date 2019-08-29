package com.aaronhowser1.documentmod.json;

import com.aaronhowser1.documentmod.DocumentMod;
import com.aaronhowser1.documentmod.config.DYMMConfig;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ProgressManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public enum ReloadHandler {
    INSTANCE;

    private ProgressManager.ProgressBar bar;

    public void reload() {
        DocumentMod.logger.info("Handling reload of documentations that require reload");
        final Map<ModDocumentation, JsonObject> needReload = DocumentationLoader.INSTANCE.getReloadNeeded();
        this.bar = ProgressManager.push("Reloading JSON documentation", needReload.size());
        needReload.forEach(this::reloadModDocumentation);
        ProgressManager.pop(this.bar);
        this.bar = null;
        DocumentMod.logger.info("Reload completed");
    }

    public void dumpAndClear() {
        final Consumer<String> loggingMethod = DYMMConfig.debugModIsDocumented ? DocumentMod.logger::info : DocumentMod.logger::debug;
        loggingMethod.accept("Dumping reloaded resources: ");
        DocumentationLoader.INSTANCE.getReloadNeeded().keySet().stream()
                .map(it -> "    " + it.getRegistryName() + " --> " + it)
                .forEach(loggingMethod);
        this.clear();
    }

    private void reloadModDocumentation(@Nonnull final ModDocumentation documentation, @Nonnull final JsonObject parse) {
        this.reloadModDocumentation(documentation, parse, documentation.getRegistryName());
    }

    private void reloadModDocumentation(@Nonnull final ModDocumentation doc, @Nonnull final JsonObject obj, @Nullable final ResourceLocation name) {
        if (name == null) throw new IllegalStateException("Found Mod documentation without a registry name at this stage! This is impossible!");
        DocumentMod.logger.debug("Reloading " + name);
        this.bar.step(name.toString());
        final ModDocumentation tmpDoc = this.constructTemporaryDocumentation(obj, name);
        this.moveItems(tmpDoc, doc, name);
    }

    @Nonnull
    private ModDocumentation constructTemporaryDocumentation(@Nonnull final JsonObject object, @Nonnull final ResourceLocation name) {
        final ResourceLocation tmpName = new ResourceLocation(DocumentMod.MODID, "__$__.reload.__$__." + name.toString().replace(':', '.'));
        final ModDocumentation doc = ModDocumentation.create(object, tmpName);
        if (!doc.isReloadable()) {
            throw new IllegalArgumentException("Attempted to reload a non-reloadable resource! This is a SERIOUS error!");
        }
        return doc;
    }

    private void moveItems(@Nonnull final ModDocumentation from, @Nonnull final ModDocumentation to, @Nonnull final ResourceLocation name) {
        final List<ItemStack> stacks = ImmutableList.copyOf(from.getReferredStacks());
        DocumentMod.logger.trace("Attempting to move " + stacks.size() + " ItemStack instances from " + from.getRegistryName() + " to " + to.getRegistryName());
        final Field itemStacks = this.reflectAndGetStacks(to, name);
        if (itemStacks == null) return;
        if (!this.setNewStacks(to, itemStacks, stacks, name)) return;
        this.disableReload(to, name);
    }

    @Nullable
    private Field reflectAndGetStacks(@Nonnull final ModDocumentation documentation, @Nonnull final ResourceLocation name) {
        return this.reflectAndGet(documentation, "itemStacks", false, name);
    }

    private boolean setNewStacks(@Nonnull final ModDocumentation documentation, @Nonnull final Field itemStacks,
                              @Nonnull final List<ItemStack> newStacks, @Nonnull final ResourceLocation name) {
        try {
            @SuppressWarnings("unchecked")
            final List<ItemStack> oldStacks = (List<ItemStack>) itemStacks.get(documentation);
            itemStacks.set(documentation, ImmutableList.copyOf(newStacks));
            DocumentMod.logger.trace("Successfully replaced old stacks " + oldStacks + " with new stacks " + newStacks);
            return true;
        } catch (@Nonnull final ReflectiveOperationException e) {
            DocumentMod.logger.warn("Unable to reload entry '" + name + "'. It will now be skipped and the error printed to STDERR");
            e.printStackTrace(System.err);
            return false;
        }
    }

    private void disableReload(@Nonnull final ModDocumentation doc, @Nonnull final ResourceLocation name) {
        try {
            final Field reload = this.reflectAndGet(doc, "reload", true, name);
            if (reload == null) throw new IllegalStateException("'reload' field does not exist in ModDocumentation?");
            reload.setBoolean(doc, false);
        } catch (@Nonnull final ReflectiveOperationException e) {
            DocumentMod.logger.warn("Unable to mark entry '" + name + "' as reloaded. This will cause a crash later on! The error will be printed to STDERR");
            e.printStackTrace(System.err);
        }
    }

    @Nullable
    private Field reflectAndGet(@Nonnull final ModDocumentation documentation, @Nonnull final String fieldName,
                                final boolean removeFinal, @Nonnull final ResourceLocation name) {
        try {
            final Class<? extends ModDocumentation> clazz = documentation.getClass();
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            if (removeFinal) this.removeFinalModifier(field);
            return field;
        } catch (@Nonnull final ReflectiveOperationException e) {
            DocumentMod.logger.warn("Unable to reload entry '" + name + "'. It will now be skipped. The stacktrace of the error will be printed to STDERR");
            e.printStackTrace(System.err);
            return null;
        }
    }

    private void removeFinalModifier(@Nonnull final Field field) throws ReflectiveOperationException {
        final Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }

    private void clear() {
        try {
            final Class<? extends DocumentationLoader> clazz = DocumentationLoader.INSTANCE.getClass();
            final Field map = clazz.getDeclaredField("RELOAD_NEEDED_MOD_DOCUMENTATIONS");
            map.setAccessible(true);
            final Method clear = Map.class.getDeclaredMethod("clear");
            clear.invoke(map.get(DocumentationLoader.INSTANCE));
        } catch (@Nonnull final ReflectiveOperationException ignore) {
            // We do not care if this doesn't work
        }
    }
}
