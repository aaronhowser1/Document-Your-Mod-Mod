package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.prefab.loader;

import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Lazy;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.ContextKey;

public final class CCK {
    public static final Lazy<ContextKey<String>> MOD_ID_CONTEXT_KEY = Lazy.lazy(() -> ContextKey.invoke("modId", KClass.get(String.class)));

    private CCK() {}
}
