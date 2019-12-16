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
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) return element.getAsJsonPrimitive().getAsFloat();
        throw expectedType(element, name, "a float");
    }

    public static float getFloat(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asFloat(object.get(name), name);
        throw missingProperty(name, "a float");
    }

    public static float getFloatOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<Float> fallback) {
        return object.has(name)? getFloat(object, name) : fallback.get();
    }

    public static int asInt(@Nonnull final JsonElement element, @Nonnull final String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) return element.getAsJsonPrimitive().getAsInt();
        throw expectedType(element, name, "an integer");
    }

    public static int getInt(@Nonnull final JsonObject object, @Nonnull final String name) {
        if (object.has(name)) return asInt(object.get(name), name);
        throw missingProperty(name, "an integer");
    }

    public static int getIntOrElse(@Nonnull final JsonObject object, @Nonnull final String name, @Nonnull final Supplier<Integer> fallback) {
        return object.has(name)? getInt(object, name) : fallback.get();
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

    @Nonnull
    private static JsonSyntaxException expectedType(@Nonnull final JsonElement element, @Nonnull final String name, @Nonnull final String expected) {
        throw new JsonSyntaxException("Expected '" + name + "' to be " + expected + ", but it was " + toErrorString(element));
    }

    @Nonnull
    private static JsonSyntaxException missingProperty(@Nonnull final String name, @Nonnull final String expected) {
        throw new JsonSyntaxException("Property '" + name + "' was missing: expected to find " + expected);
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
