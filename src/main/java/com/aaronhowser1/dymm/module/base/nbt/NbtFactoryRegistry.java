package com.aaronhowser1.dymm.module.base.nbt;

import net.minecraft.nbt.NBTBase;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum NbtFactoryRegistry {
    INSTANCE;

    private final Map<String, NbtFactory<?>> REGISTRY = new HashMap<>();

    <T extends NBTBase> void registerFactory(@Nonnull final String mnemonic, @Nonnull final NbtFactory<T> type) {
        if (REGISTRY.containsKey(mnemonic)) {
            throw new IllegalStateException("The given mnemonic '" + mnemonic + "' had a factory assigned already");
        }
        REGISTRY.put(mnemonic, type);
    }

    @SuppressWarnings("unchecked")
    public <T extends NBTBase> NbtFactory<T> getForType(@Nonnull final String mnemonic) {
        try {
            return (NbtFactory<T>) Objects.requireNonNull(REGISTRY.get(mnemonic));
        } catch (@Nonnull final NullPointerException e) {
            throw new IllegalArgumentException("The given mnemonic '" + mnemonic + "' is not a valid NBT type", e);
        }
    }
}
