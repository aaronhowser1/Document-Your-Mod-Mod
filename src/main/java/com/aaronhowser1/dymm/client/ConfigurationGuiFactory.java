package com.aaronhowser1.dymm.client;

import com.aaronhowser1.dymm.Constants;
import com.aaronhowser1.dymm.L;
import com.aaronhowser1.dymm.api.ApiBindings;
import com.aaronhowser1.dymm.api.configuration.ConfigurationManager;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiEditArrayEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class ConfigurationGuiFactory implements IModGuiFactory {
    private static final class ConfigurationGuiElement implements IConfigElement {
        private final String configurationName;
        private final String configurationCategoryName;
        private final Property property;

        private ConfigurationGuiElement(@Nonnull final String configurationName, @Nonnull final String configurationCategoryName, @Nonnull final Property property) {
            this.configurationName = Objects.requireNonNull(configurationName);
            this.configurationCategoryName = Objects.requireNonNull(configurationCategoryName);
            this.property = Objects.requireNonNull(property);
        }

        @Override
        public boolean isProperty() {
            return true;
        }

        @Nullable
        @Override
        public Class<? extends GuiConfigEntries.IConfigEntry> getConfigEntryClass() {
            return null;
        }

        @Nullable
        @Override
        public Class<? extends GuiEditArrayEntries.IArrayEntry> getArrayEntryClass() {
            return null;
        }

        @Nonnull
        @Override
        public String getName() {
            return this.property.getName();
        }

        @Nonnull
        @Override
        public String getQualifiedName() {
            return this.getName();
        }

        @Nonnull
        @Override
        public String getLanguageKey() {
            return this.property.getLanguageKey().isEmpty()?
                    "dymm.mod.configuration." + this.configurationName + "." + this.configurationCategoryName + "." + this.property.getName() :
                    this.property.getLanguageKey();
        }

        @Nonnull
        @Override
        public String getComment() {
            return this.property.getComment();
        }

        @Nonnull
        @Override
        public List<IConfigElement> getChildElements() {
            return new ArrayList<>();
        }

        @Nonnull
        @Override
        public ConfigGuiType getType() {
            return this.toConfigGuiType(this.property.getType());
        }

        @Override
        public boolean isList() {
            return this.property.isList();
        }

        @Override
        public boolean isListLengthFixed() {
            return this.property.isListLengthFixed();
        }

        @Override
        public int getMaxListLength() {
            return this.property.getMaxListLength();
        }

        @Override
        public boolean isDefault() {
            return this.property.isDefault();
        }

        @Nullable
        @Override
        public Object getDefault() {
            return this.property.getDefault();
        }

        @Nullable
        @Override
        public Object[] getDefaults() {
            return this.property.getDefaults();
        }

        @Override
        public void setToDefault() {
            this.property.setToDefault();
        }

        @Override
        public boolean requiresWorldRestart() {
            return this.property.requiresWorldRestart();
        }

        @Override
        public boolean showInGui() {
            return this.property.showInGui();
        }

        @Override
        public boolean requiresMcRestart() {
            return this.property.requiresMcRestart();
        }

        @Nullable
        @Override
        public Object get() {
            return this.property.getString();
        }

        @Nullable
        @Override
        public Object[] getList() {
            return this.property.getStringList();
        }

        @Override
        public void set(@Nonnull final Object value) {
            switch (this.property.getType()) {
                case INTEGER:
                    this.property.set(Integer.parseInt(value.toString()));
                    break;
                case BOOLEAN:
                    this.property.set(Boolean.parseBoolean(value.toString()));
                    break;
                case DOUBLE:
                    this.property.set(Double.parseDouble(value.toString()));
                    break;
                default:
                    this.property.set(value.toString());
            }
        }

        @Override
        public void set(@Nonnull final Object[] aVal) {
            switch (this.property.getType()) {
                case INTEGER:
                    this.property.set(Ints.toArray(Arrays.stream(aVal).map(Object::toString).map(Integer::valueOf).collect(Collectors.toList())));
                    break;
                case BOOLEAN:
                    this.property.set(Booleans.toArray(Arrays.stream(aVal).map(Object::toString).map(Boolean::valueOf).collect(Collectors.toList())));
                    break;
                case DOUBLE:
                    this.property.set(Doubles.toArray(Arrays.stream(aVal).map(Object::toString).map(Double::valueOf).collect(Collectors.toList())));
                    break;
                default:
                    break;
            }
        }

        @Nonnull
        @Override
        public String[] getValidValues() {
            return this.property.getValidValues();
        }

        @Nullable
        @Override
        public Object getMinValue() {
            return this.property.getMinValue();
        }

        @Nullable
        @Override
        public Object getMaxValue() {
            return this.property.getMaxValue();
        }

        @Nullable
        @Override
        public Pattern getValidationPattern() {
            return this.property.getValidationPattern();
        }

        @Nonnull
        private ConfigGuiType toConfigGuiType(@Nonnull final Property.Type type) {
            switch (type) {
                case STRING: return ConfigGuiType.STRING;
                case INTEGER: return ConfigGuiType.INTEGER;
                case BOOLEAN: return ConfigGuiType.BOOLEAN;
                case DOUBLE: return ConfigGuiType.DOUBLE;
                case COLOR: return ConfigGuiType.COLOR;
                case MOD_ID: return ConfigGuiType.MOD_ID;
            }
            throw new IllegalArgumentException("Not a valid type '" + type + "'");
        }
    }

    private static final L LOG = L.create(Constants.MOD_NAME, "Configuration GUI");
    private Map<String, Configuration> configurationMap;

    @Override
    public void initialize(@Nonnull final Minecraft minecraftInstance) {
        LOG.info("GUI Factory initialized");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public GuiScreen createConfigGui(@Nonnull final GuiScreen parentScreen) {
        final List<IConfigElement> configurationTargets = this.obtainConfigurationTargets();
        if (configurationTargets == null) {
            return new GuiErrorScreen(I18n.format("dymm.mod.configuration.error.title"), I18n.format("dymm.mod.configuration.error.message"));
        }
        return new GuiConfig(parentScreen, configurationTargets, Constants.MOD_ID, false, false, Constants.MOD_NAME);
    }

    @Nullable
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Nullable
    private List<IConfigElement> obtainConfigurationTargets() {
        final List<IConfigElement> elements = new ArrayList<>();
        final Map<String, Configuration> configurations = this.getMapOrElse();
        if (configurations.isEmpty()) {
            return null;
        }
        configurations.forEach((name, configuration) ->
                elements.add(new DummyConfigElement.DummyCategoryElement(name + ".cfg", "dymm.mod.configuration." + name, this.getCategories(name, configuration))));
        return elements;
    }

    @Nonnull
    private Map<String, Configuration> getMapOrElse() {
        if (this.configurationMap != null) {
            return this.configurationMap;
        }
        try {
            return this.configurationMap = this.reflectMap();
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
        LOG.info("Attempting to reflectively load configuration map");
        try {
            final ConfigurationManager manager = ApiBindings.getMainApi().getConfigurationManager();
            final Field map = manager.getClass().getDeclaredField("CONFIGURATION_CACHE");
            map.setAccessible(true);
            return (Map<String, Configuration>) map.get(manager);
        } catch (@Nonnull final ReflectiveOperationException e) {
            throw new RuntimeException("Unable to obtain configurations", e);
        }
    }

    @Nonnull
    private List<IConfigElement> getCategories(@Nonnull final String name, @Nonnull final Configuration configuration) {
        final List<IConfigElement> elements = new ArrayList<>();
        configuration.getCategoryNames()
                .stream()
                .map(configuration::getCategory)
                .filter(it -> !it.isChild())
                .forEach(it -> elements.add(this.toConfigElement(name, it)));
        return elements;
    }

    @Nonnull
    private IConfigElement toConfigElement(@Nonnull final String name, @Nonnull final ConfigCategory category) {
        final List<ConfigCategory> children = new ArrayList<>(category.getChildren());
        final List<Property> properties = new ArrayList<>(category.getOrderedValues());
        final List<IConfigElement> elements = children.stream().map(it -> this.toConfigElement(name, it)).collect(Collectors.toList());
        elements.addAll(properties.stream().map(it -> this.toConfigElement(name, category.getName(), it)).collect(Collectors.toList()));
        return new DummyConfigElement.DummyCategoryElement(
                category.getName(),
                "dymm.mod.configuration." + name + "." + category.getName(),
                new ArrayList<>(elements));
    }

    @Nonnull
    private IConfigElement toConfigElement(@Nonnull final String name, @Nonnull final String categoryName, @Nonnull final Property property) {
        return new ConfigurationGuiElement(name, categoryName, property);
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(@Nonnull final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (this.configurationMap == null) return;
        this.configurationMap.values().forEach(Configuration::save);
        this.configurationMap.values().forEach(Configuration::load);
    }
}
