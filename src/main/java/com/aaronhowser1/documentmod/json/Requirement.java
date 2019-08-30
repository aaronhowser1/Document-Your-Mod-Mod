package com.aaronhowser1.documentmod.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class Requirement {

    public enum Ordering {
        BEFORE, AFTER
    }

    private final Ordering ordering;
    private final ResourceLocation referredRegistryName;
    private final boolean isRequired;

    private Requirement(@Nonnull final Ordering ordering, @Nonnull final ResourceLocation referredRegistryName, final boolean isRequired) {
        this.ordering = ordering;
        this.referredRegistryName = referredRegistryName;
        this.isRequired = isRequired;
    }

    @Nonnull
    static Requirement buildRequirement(@Nonnull final JsonObject object, @Nonnull final ResourceLocation parentName) {
        final Ordering ordering = parseOrdering(JsonUtils.getString(object, "order"), parentName);
        final ResourceLocation location = new ResourceLocation(JsonUtils.getString(object, "registry_name"));
        final boolean isRequired = object.has("required") && JsonUtils.getBoolean(object, "required");
        return new Requirement(ordering, location, isRequired);
    }

    @Nonnull
    private static Ordering parseOrdering(@Nonnull final String string, @Nonnull final ResourceLocation parentName) {
        switch (string) {
            case "before": return Ordering.BEFORE;
            case "after": return Ordering.AFTER;
        }
        throw new JsonSyntaxException("In entry '" + parentName + "', the string '" + string + "' is not a valid ordering. It must be either 'before' or 'after'");
    }

    @Nonnull
    public Ordering getOrdering() {
        return this.ordering;
    }

    @Nonnull
    public ResourceLocation getReferredRegistryName() {
        return this.referredRegistryName;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isRequired() {
        return this.isRequired;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final Requirement that = (Requirement) o;
        return this.isRequired == that.isRequired &&
                this.ordering == that.ordering &&
                this.referredRegistryName.equals(that.referredRegistryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ordering, this.referredRegistryName, this.isRequired);
    }

    @Override
    public String toString() {
        return "Requirement{" +
                "ordering=" + this.ordering +
                ", referredRegistryName=" + this.referredRegistryName +
                ", isRequired=" + this.isRequired +
                '}';
    }
}
