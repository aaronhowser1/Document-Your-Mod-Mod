package com.aaronhowser1.dym.api.loading.metadata;

import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface MetadataListener {
    void processMetadata(@Nonnull final JsonObject object);
}