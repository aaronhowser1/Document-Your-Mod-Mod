package com.aaronhowser1.dymm.module.base;

import com.aaronhowser1.dymm.JsonUtilities;
import com.aaronhowser1.dymm.api.loading.metadata.MetadataListener;
import com.aaronhowser1.dymm.common.ModDescriptionUpdatingHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class SupportedVersionsMetadataListener implements MetadataListener {
    /*
     * Example JSON:
     *
     * {
     *   "versions": [
     *     "1.0.0",
     *     "1.1.0",
     *     "1.1.0.beta"
     *   ]
     * }
     */

    SupportedVersionsMetadataListener() {}

    @Override
    public void processMetadata(@Nonnull final JsonObject object, @Nonnull final String nameSpace) {
        final JsonArray versions = JsonUtilities.getJsonArray(object, "versions");
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < versions.size(); i++) {
            final String version = JsonUtilities.asString(versions.get(i), "versions[" + i + "]");
            if (version.isEmpty()) throw new JsonParseException("Unable to support an empty version");
            list.add(version);
        }
        ModDescriptionUpdatingHandler.addVersion(nameSpace, list);
    }
}
