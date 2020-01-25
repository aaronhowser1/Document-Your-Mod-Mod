package com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.naming;

import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.AssertNotNullExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.ElvisExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.kotlin.bridge.expression.IfExpression;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Context;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.IdentifierBuilder;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.api.loader.Location;
import com.aaronhowser1.dymm.shade.net.thesilkminer.mc.boson.prefab.loader.CCK;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.Nonnull;

public final class DefaultIdentifierBuilder implements IdentifierBuilder {
    private final boolean removeExtension;

    private DefaultIdentifierBuilder(final boolean removeExtension) {
        this.removeExtension = removeExtension;
    }

    @Nonnull
    public static DefaultIdentifierBuilder create(final boolean removeExtension) {
        return new DefaultIdentifierBuilder(removeExtension);
    }

    @Nonnull
    public static DefaultIdentifierBuilder create() {
        return create(false);
    }

    @Nonnull
    @Override
    public NameSpacedString makeIdentifier(@Nonnull final Location location, @Nonnull final Nullable<Context> globalContext, @Nonnull final Nullable<Context> phaseContext) {
        return NameSpacedString.invoke(
                ElvisExpression.create(
                        location.getAdditionalContext().ifPresent(it -> it.get(CCK.MOD_ID_CONTEXT_KEY.invoke())),
                        () -> Nullable.get(Loader.instance().activeModContainer()).ifPresent(it -> AssertNotNullExpression.create(it.getModId()).invoke())
                ).invoke(),
                IfExpression.build(
                        this.removeExtension,
                        () -> FilenameUtils.removeExtension(location.getPath().toString()),
                        () -> location.getPath().toString()
                ).invoke().replaceAll("\\\\", "/")
        );
    }
}
