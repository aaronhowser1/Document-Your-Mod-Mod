package com.aaronhowser1.dymm.api.consume;

import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import com.aaronhowser1.dymm.api.documentation.Target;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public interface DocumentationDataConsumer {
    @Nonnull List<ResourceLocation> getCompatibleTypes();
    void consumeData(@Nonnull final DocumentationData data, @Nonnull final Set<Target> targets);

    default void onCreation() {}
}
