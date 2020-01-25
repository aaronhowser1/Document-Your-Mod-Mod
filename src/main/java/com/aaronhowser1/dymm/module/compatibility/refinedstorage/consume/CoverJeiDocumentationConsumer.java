package com.aaronhowser1.dymm.module.compatibility.refinedstorage.consume;

import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.consume.DocumentationDataConsumer;
import com.aaronhowser1.dymm.api.documentation.DocumentationData;
import com.aaronhowser1.dymm.api.documentation.Target;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class CoverJeiDocumentationConsumer implements DocumentationDataConsumer {
    final static List<Pair<Target, DocumentationData>> docData = new ArrayList<>();

    @Nonnull
    @Override
    public List<ResourceLocation> getCompatibleTypes() {
        final List<ResourceLocation> locations = new ArrayList<>();
        locations.add(new ResourceLocation("refinedstorage", "cover_jei_information"));
        return locations;
    }

    @Override
    public void consumeData(@Nonnull final DocumentationData data, @Nonnull final Set<Target> targets) {
        targets.forEach(it -> docData.add(ImmutablePair.of(it, data)));
    }
}
