package com.aaronhowser1.documentmod.proxy;


import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;

public class ClientProxy extends CommonProxy {
    @Override
    public boolean canTranslate(@Nonnull final String key) {
        return I18n.hasKey(key);
    }
}
