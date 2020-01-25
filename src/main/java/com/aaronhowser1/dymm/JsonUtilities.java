package com.aaronhowser1.dymm;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class JsonUtilities {
    private JsonUtilities() {}

    @Nonnull
    public static String asString(@Nonnull final JsonElement element, @Nonnull final String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) return element.getAsJsonPrimitive().getAsString();
        throw expectedType(element, name, "a string");
    }

    @Nonnull
    public static String getString(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asString(object.get(name), name);
        throw missingProperty(name, "a string");
    }

    @Nonnull
    public static String getStringOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<String> fallback) {
        return object.has(name)? getString(object, name) : fallback.get();
    }

    public static boolean asBoolean(@Nonnull final JsonElement element, @Nonnull final String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) return element.getAsJsonPrimitive().getAsBoolean();
        throw expectedType(element, name, "a boolean");
    }

    public static boolean getBoolean(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asBoolean(object.get(name), name);
        throw missingProperty(name, "a boolean");
    }

    public static boolean getBooleanOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<Boolean> fallback) {
        return object.has(name)? getBoolean(object, name) : fallback.get();
    }

    public static float asFloat(@Nonnull final JsonElement element, @Nonnull final String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) return tryFloatNarrowing(asDouble(element, name));
        throw expectedType(element, name, "a float");
    }

    public static float getFloat(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asFloat(object.get(name), name);
        throw missingProperty(name, "a float");
    }

    public static float getFloatOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<Float> fallback) {
        return object.has(name)? getFloat(object, name) : fallback.get();
    }

    public static double asDouble(@Nonnull final JsonElement element, @Nonnull final String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) return element.getAsJsonPrimitive().getAsDouble();
        throw expectedType(element, name, "a double");
    }

    public static double getDouble(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asDouble(object.get(name), name);
        throw missingProperty(name, "a double");
    }

    public static double getDoubleOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<Double> fallback) {
        return object.has(name)? getDouble(object, name) : fallback.get();
    }

    public static byte asByte(@Nonnull final JsonElement element, @Nonnull final String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) return tryByteNarrowing(asLong(element, name));
        throw expectedType(element, name, "a short");
    }

    public static byte getByte(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asByte(object.get(name), name);
        throw missingProperty(name, "an integer");
    }

    public static byte getByteOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<Byte> fallback) {
        return object.has(name)? getByte(object, name) : fallback.get();
    }

    public static short asShort(@Nonnull final JsonElement element, @Nonnull final String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) return tryShortNarrowing(asLong(element, name));
        throw expectedType(element, name, "a short");
    }

    public static short getShort(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asShort(object.get(name), name);
        throw missingProperty(name, "an integer");
    }

    public static short getShortOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<Short> fallback) {
        return object.has(name)? getShort(object, name) : fallback.get();
    }

    public static int asInt(@Nonnull final JsonElement element, @Nonnull final String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) return tryIntNarrowing(asLong(element, name));
        throw expectedType(element, name, "an integer");
    }

    public static int getInt(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asInt(object.get(name), name);
        throw missingProperty(name, "an integer");
    }

    public static int getIntOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<Integer> fallback) {
        return object.has(name)? getInt(object, name) : fallback.get();
    }

    public static long asLong(@Nonnull final JsonElement element, @Nonnull final String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) return element.getAsJsonPrimitive().getAsLong();
        throw expectedType(element, name, "a long");
    }

    public static long getLong(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asLong(object.get(name), name);
        throw missingProperty(name, "a long");
    }

    public static long getLongOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<Long> fallback) {
        return object.has(name)? getLong(object, name) : fallback.get();
    }

    @Nonnull
    public static JsonObject asJsonObject(@Nonnull final JsonElement element, @Nonnull final String name) {
        if (element.isJsonObject()) return element.getAsJsonObject();
        throw expectedType(element, name, "an object");
    }

    @Nonnull
    public static JsonObject getJsonObject(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asJsonObject(object.get(name), name);
        throw missingProperty(name, "an object");
    }

    @Nonnull
    public static JsonObject getJsonObjectOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<JsonObject> fallback) {
        return object.has(name)? getJsonObject(object, name) : fallback.get();
    }

    @Nonnull
    public static JsonArray asJsonArray(@Nonnull final JsonElement element, @Nonnull final String name) {
        if (element.isJsonArray()) return element.getAsJsonArray();
        throw expectedType(element, name, "an array");
    }

    @Nonnull
    public static JsonArray getJsonArray(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asJsonArray(object.get(name), name);
        throw missingProperty(name, "an array");
    }

    @Nonnull
    public static JsonArray getJsonArrayOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<JsonArray> fallback) {
        return object.has(name)? getJsonArray(object, name) : fallback.get();
    }

    public static boolean checkEntriesAsJsonObjects(@Nonnull final JsonArray array, @Nonnull final String name, @Nonnull final Predicate<JsonObject> matcher) {
        for (int i = 0; i < array.size(); ++i) {
            if (!matcher.test(asJsonObject(array.get(i), name + "[" + i + "]"))) return false;
        }
        return true;
    }

    public static void consumeEntriesAsJsonObjects(@Nonnull final JsonArray array, @Nonnull final String name, @Nonnull final Consumer<JsonObject> consumer) {
        for (int i = 0; i < array.size(); ++i) {
            consumer.accept(asJsonObject(array.get(i), name + "[" + i + "]"));
        }
    }

    private static byte tryByteNarrowing(final long value) {
        if (value >= Byte.MAX_VALUE || value < Byte.MIN_VALUE) throw narrowingFailed(value, "a byte");
        return (byte) value;
    }

    private static short tryShortNarrowing(final long value) {
        if (value >= Short.MAX_VALUE || value < Short.MIN_VALUE) throw narrowingFailed(value, "a short");
        return (short) value;
    }

    private static int tryIntNarrowing(final long value) {
        if (value >= Integer.MAX_VALUE || value < Integer.MIN_VALUE) throw narrowingFailed(value, "an integer");
        return (int) value;
    }

    private static float tryFloatNarrowing(final double value) {
        if (value >= Float.MAX_VALUE || value < -Float.MAX_VALUE) throw narrowingFailed(value, "a float");
        return (float) value;
    }

    @Nonnull
    private static JsonSyntaxException expectedType(@Nonnull final JsonElement element, @Nonnull final String name, @Nonnull final String expected) {
        throw new JsonSyntaxException("Expected '" + name + "' to be " + expected + ", but it was " + toErrorString(element));
    }

    @Nonnull
    private static JsonSyntaxException missingProperty(@Nonnull final String name, @Nonnull final String expected) {
        throw new JsonSyntaxException("Property '" + name + "' was missing: expected to find " + expected);
    }

    @Nonnull
    private static <T extends Number> JsonSyntaxException narrowingFailed(@Nonnull final T value, @Nonnull final String name) {
        throw new JsonSyntaxException("Number '" + value + "' is too big to be represented as " + name);
    }

    @Nonnull
    private static String toErrorString(@Nonnull final JsonElement element) {
        final String content = StringUtils.abbreviateMiddle(element.toString(), "...", 10);

        if (element.isJsonNull()) return "null";
        if (element.isJsonArray()) return "an array (" + content + ")";
        if (element.isJsonObject()) return "an object (" + content + ")";
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) return "a string (" + content + ")";
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) return "a boolean (" + content + ")";
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) return "a number (" + content + ")";

        throw new IllegalStateException("Given element " + element + " is not a valid JSON element");
    }
}
