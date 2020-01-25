package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.preprocessor;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.TryExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Preprocessor;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javax.annotation.Nonnull;

public final class JsonConverterPreprocessor implements Preprocessor<String, JsonObject> {
    private static final Gson JSON_READER = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();

    private JsonConverterPreprocessor() {}

    @Nonnull
    public static JsonConverterPreprocessor create() {
        return new JsonConverterPreprocessor();
    }

    @Nonnull
    @Override
    public Nullable<JsonObject> preProcessData(@Nonnull final String content, @Nonnull final NameSpacedString identifier, @Nonnull final Nullable<Context> globalContext,
                                               @Nonnull final Nullable<Context> phaseContext) {
        return TryExpression.create(
                () -> Nullable.get(JSON_READER.fromJson(content, JsonObject.class)),
                ImmutableList.of(
                        TryExpression.CatchClause.create(
                                KClass.get(JsonSyntaxException.class),
                                e -> {
                                    throw new JsonSyntaxException("The file identified by '" + identifier + "' is not a valid JSON file. Please check your syntax", e);
                                })
                )
        ).invoke();
    }
}
