package com.aaronhowser1.dymm.client;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.configuration.ConfigurationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public final class ConfigurationGuiFactory implements IModGuiFactory {
    private static final L LOG = L.create(Constants.MOD_NAME, "Configuration GUI");

    @Override
    public void initialize(@Nonnull final Minecraft minecraftInstance) {

    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public GuiScreen createConfigGui(@Nonnull final GuiScreen parentScreen) {
        return new GuiConfig(parentScreen, this.obtainConfigurationTargets(), Constants.MOD_ID, false, false, Constants.MOD_NAME);
    }

    @Nullable
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Nonnull
    private List<IConfigElement> obtainConfigurationTargets() {
        final List<IConfigElement> elements = new ArrayList<>();
        final Map<String, Configuration> configurations = this.getMapOrElse();
        if (configurations.isEmpty()) {
            elements.add(new DummyConfigElement.DummyCategoryElement("An error has occurred", "dymm.mod.configuration.error", new ArrayList<>()));
            return elements;
        }
        configurations.forEach((name, configuration) -> {
            final Set<String> categoryNames = configuration.getCategoryNames();
            final List<IConfigElement> categoryElements = new ArrayList<>();
            categoryNames.forEach(categoryName -> {
                final ConfigCategory category = configuration.getCategory(categoryName);
                if (category.isChild()) return;
                final DummyConfigElement.DummyCategoryElement element =
                        new DummyConfigElement.DummyCategoryElement(category.getName(), category.getLanguagekey(), new ConfigElement(category).getChildElements());
                element.setRequiresMcRestart(category.requiresMcRestart());
                element.setRequiresWorldRestart(category.requiresWorldRestart());
                categoryElements.add(element);
            });
            elements.add(new DummyConfigElement.DummyCategoryElement(name + ".cfg", "", categoryElements));
        });
        return elements;
    }

    @Nonnull
    private Map<String, Configuration> getMapOrElse() {
        try {
            return this.reflectMap();
        } catch (@Nonnull final RuntimeException e) {
            final StringWriter builder = new StringWriter();
            e.printStackTrace(new PrintWriter(builder));
            LOG.bigError("An error has occurred while attempting to obtain the registered configurations!\nSubstituting with an empty map.\n" +
                            "The stacktrace is the following:\n" + builder.toString(),
                    L.DumpStackBehavior.DO_NOT_DUMP);
            return new HashMap<>();
        }
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    private Map<String, Configuration> reflectMap() {
        try {
            final ConfigurationManager manager = ApiBindings.getMainApi().getConfigurationManager();
            final Field map = manager.getClass().getDeclaredField("CONFIGURATION_CACHE");
            map.setAccessible(true);
            return (Map<String, Configuration>) map.get(manager);
        } catch (@Nonnull final ReflectiveOperationException e) {
            throw new RuntimeException("Unable to obtain configurations", e);
        }
    }
}
