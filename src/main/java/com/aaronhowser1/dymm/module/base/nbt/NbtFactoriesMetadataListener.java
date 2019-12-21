package com.aaronhowser1.dymm.module.base.nbt;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.loading.metadata.MetadataListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.NBTBase;

import javax.annotation.Nonnull;

public final class NbtFactoriesMetadataListener implements MetadataListener {
    /*
     * Example JSON:
     *
     * {
     *   "compound": "...",
     *   "end": "...",
     *   "asd": "..."
     * }
     */

    public NbtFactoriesMetadataListener() {}

    @Override
    public void processMetadata(@Nonnull final JsonObject object, @Nonnull final String nameSpace) {
        if (!Constants.MOD_ID.equals(nameSpace)) {
            throw new JsonParseException("Only the default namespace is allowed to specify a 'nbt_factories' metadata file");
        }
        object.entrySet().forEach(it -> {
            final String tagType = it.getKey();
            final String factoryClassName = JsonUtilities.asString(it.getValue(), tagType);
            final NbtFactory<?> factory = this.instantiateFactoryFromClassName(factoryClassName);
            NbtFactoryRegistry.INSTANCE.registerFactory(tagType, factory);
        });
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    private <T extends NBTBase> NbtFactory<T> instantiateFactoryFromClassName(@Nonnull final String name) {
        try {
            final Class<?> tClass = Class.forName(name);
            final Object instance = tClass.getConstructor().newInstance();
            return (NbtFactory<T>) instance;
        } catch (@Nonnull final ClassCastException | ReflectiveOperationException e) {
            throw new JsonParseException("Unable to find specified class '" + name + "' when initializing factory", e);
        }
    }
}
