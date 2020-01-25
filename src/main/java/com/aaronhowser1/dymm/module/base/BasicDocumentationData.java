package com.aaronhowser1.dymm.module.base;

import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BasicDocumentationData implements DocumentationData {
    private final ResourceLocation type;
    private final List<String> data;

    private BasicDocumentationData(@Nonnull final ResourceLocation type, @Nonnull final List<String> data) {
        this.type = type;
        this.data = new ArrayList<>(data);
    }

    @Nonnull
    public static DocumentationData buildFrom(@Nonnull final ResourceLocation type, @Nonnull final List<String> data) {
        return new BasicDocumentationData(Objects.requireNonNull(type), Objects.requireNonNull(data));
    }

    @Nonnull
    @Override
    public ResourceLocation getType() {
        return this.type;
    }

    @Nonnull
    @Override
    public List<String> getData() {
        return new ArrayList<>(this.data);
    }
}
