package com.aaronhowser1.dymm.api.documentation;

import net.minecraft.util.ResourceLocation;

import java.util.List;
import javax.annotation.Nonnull;

public interface DocumentationData {
    @Nonnull ResourceLocation getType();
    @Nonnull List<String> getData();
}
