package com.aaronhowser1.documentmod.json.factory.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@SuppressWarnings("WeakerAccess")
public abstract class NbtTagValueRequiringFactory<T extends NBTBase> implements NbtTagFactory<T> {
    @Nonnull
    @Override
    public T parseFromJson(@Nonnull final JsonObject object, @Nonnull final ResourceLocation name, final int recursionLevel) {
        if (!object.has("value")) {
            final String type = JsonUtils.getString(object, "type");
            throw new JsonSyntaxException("NBT Tag of type '" + type + "' requires a 'value' member");
        }
        if (recursionLevel >= 10) {
            throw new JsonSyntaxException("You're more than 10 levels deep! This is a bit too much for NBT, you know?");
        }
        try {
            return this.parseFromValue(object.get("value"), name, recursionLevel);
        } catch (@Nonnull final Exception e) {
            if (e instanceof JsonParseException) throw e;
            throw new JsonParseException("Unable to parse NBT Tags for entry '" + name + "'", e);
        }
    }

    @Nonnull
    public T parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name, final int recursionLevel) {
        return this.parseFromValue(value, name);
    }

    @Nonnull
    public T parseFromValue(@Nonnull final JsonElement value, @Nonnull final ResourceLocation name) {
        throw new UnsupportedOperationException("Missing override for 'parseFromValue' in factory " + this.getClass() + "\n" +
                "Override either the two or the three arguments version WITHOUT invoking super");
    }

    protected byte tryByteNarrowing(final long value) {
        if (value >= Byte.MAX_VALUE || value < Byte.MIN_VALUE) {
            throw new JsonSyntaxException("Number '" + value + "' is too big to be represented as a byte");
        }
        return (byte) value;
    }

    protected short tryShortNarrowing(final long value) {
        if (value >= Short.MAX_VALUE || value < Short.MIN_VALUE) {
            throw new JsonSyntaxException("Number '" + value + "' is too big to be represented as a short");
        }
        return (short) value;
    }

    protected int tryIntNarrowing(final long value) {
        if (value >= Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
            throw new JsonSyntaxException("Number '" + value + "' is too big to be represented as an integer");
        }
        return (int) value;
    }

    protected float tryFloatNarrowing(final double value) {
        if (value >= Float.MAX_VALUE || value < -Float.MAX_VALUE) {
            throw new JsonSyntaxException("Number '" + value + "' is too big to represented as a float");
        }
        return (float) value;
    }

    protected long toLong(@Nonnull final JsonElement value, final boolean allowString) {
        try {
            return getLong(value, "value");
        } catch (@Nonnull final JsonSyntaxException e) {
            if (!allowString) throw e;
            try {
                return Long.parseLong(JsonUtils.getString(value, "value"));
            } catch (@Nonnull final JsonSyntaxException | NumberFormatException s) {
                final RuntimeException t = new JsonSyntaxException("Expected 'value' to be either a string or a long. Was neither", s);
                t.addSuppressed(e);
                throw t;
            }
        }
    }

    protected double toDouble(@Nonnull final JsonElement value, final boolean allowString) {
        try {
            return getDouble(value, "value");
        } catch (@Nonnull final JsonSyntaxException e) {
            if (!allowString) throw e;
            try {
                return Double.parseDouble(JsonUtils.getString(value, "value"));
            } catch (@Nonnull final JsonSyntaxException | NumberFormatException s) {
                final RuntimeException t = new JsonSyntaxException("Expected 'value' to be either a string or a double. Was neither", s);
                t.addSuppressed(e);
                throw t;
            }
        }
    }

    @Nonnull
    protected JsonArray toJsonArray(@Nonnull final JsonElement value) {
        return JsonUtils.getJsonArray(value, "value");
    }

    // Missing JsonUtils methods
    public static long getLong(@Nonnull final JsonElement json, @Nonnull final String memberName) {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
            return json.getAsLong();
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be a Long, was " + JsonUtils.toString(json));
        }
    }

    public static double getDouble(@Nonnull final JsonElement json, @Nonnull final String memberName) {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
            return json.getAsDouble();
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be a Double, was " + JsonUtils.toString(json));
        }
    }
}
