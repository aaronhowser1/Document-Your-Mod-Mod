package com.aaronhowser1.dymm.api.consume;

import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import com.aaronhowser1.dymm.api.documentation.Target;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public interface DocumentationDataConsumer {
    @Nonnull List<ResourceLocation> getCompatibleTypes();
    void consumeData(@Nonnull final DocumentationData data, @Nonnull final List<Target> targets);

    default void onCreation() {}
}
