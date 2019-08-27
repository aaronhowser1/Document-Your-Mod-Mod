package com.aaronhowser1.documentmod.utility;

import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;

public enum TranslationUtility {
    INSTANCE;

    private static final boolean hasI18n = checkI18n();

    private static boolean checkI18n() {
        try {
            I18n.hasKey("");
            return true;
        } catch (@Nonnull final NoClassDefFoundError error) {
            return false;
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean canTranslate(@Nonnull final String key) {
        if (!hasI18n) return true;
        return key.trim().isEmpty() || this.canTranslate0(key.trim());
    }

    private boolean canTranslate0(@Nonnull final String key) {
        return I18n.hasKey(key);
    }
}
