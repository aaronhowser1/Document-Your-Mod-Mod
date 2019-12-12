package com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api;

import com.aaronhowser1.dym.L;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Lazy;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Nullable;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.Unit;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.expression.ElvisExpression;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.expression.IfExpression;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KClass;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction0;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction1;
import com.aaronhowser1.dym.shade.net.thesilkminer.kotlin.bridge.reflect.KFunction2;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.id.NameSpacedString;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.ContextKey;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.Loader;
import com.aaronhowser1.dym.shade.net.thesilkminer.mc.boson.api.loader.LoaderBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public final class ApiBindings {
    private static final L LOG = L.create("[DYMM Shade] Boson API", "API Bindings");

    public static final Lazy<BosonApi> BOSON_API = Lazy.lazy(() -> loadWithService(KClass.get(BosonApi.class), () -> new BosonApi() {
        @Nonnull
        @Override
        public NameSpacedString constructNameSpacedString(@Nonnull final Nullable<String> nameSpace, @Nonnull final String path) {
            return new NameSpacedString() {
                @Nonnull
                @Override
                public String getNameSpace() {
                    return ElvisExpression.create(nameSpace, () -> "null").invoke();
                }

                @Nonnull
                @Override
                public String getPath() {
                    return path;
                }

                @Override
                public int compareTo(@Nonnull final NameSpacedString o) {
                    return IfExpression.build(this.getNameSpace().compareTo(o.getNameSpace()) == 0, () -> 0, () -> this.getPath().compareTo(o.getPath())).invoke();
                }
            };
        }

        @Nonnull
        @Override
        public <T> ContextKey<T> createLoaderContextKey(@Nonnull final String name, @Nonnull final KClass<T> type) {
            return new ContextKey<T>() {
                @Nonnull
                @Override
                public String getName() {
                    return name;
                }

                @Nonnull
                @Override
                public KClass<T> getType() {
                    return type;
                }
            };
        }

        @Nonnull
        @Override
        public Loader buildLoader(@Nonnull final LoaderBuilder builder) {
            return new Loader() {
                @Nonnull
                @Override
                public Unit load() {
                    return Unit.UNIT;
                }
            };
        }
    }));

    private ApiBindings() {}

    @Nonnull
    private static <T> T loadWithService(@Nonnull final KClass<T> lookUpInterface, @Nonnull final KFunction0<T> defaultProvider) {
        final KFunction2<KClass<T>, KFunction0<T>, T> load = (load$lookUpInterface, load$defaultProvider) -> {
            final List<T> foundImplementations = toList(ServiceLoader.load(load$lookUpInterface.java()).iterator());

            if (foundImplementations.size() > 1) {
                LOG.warn("Found multiple implementations for " + load$lookUpInterface.getSimpleName() + " API Bindings: trying to find our own");
                final Nullable<T> bosonImpl = firstOrNull(foundImplementations, it -> it.getClass().getName().contains("mc.boson.implementation"));
                if (bosonImpl.unwrap() != null) return bosonImpl.unwrap();
                LOG.bigWarn("Unable to find Boson implementation for " + load$lookUpInterface.getSimpleName() + " API Binding!");
                return foundImplementations.get(0);
            }

            final Nullable<T> impl = firstOrNull(foundImplementations, it -> true);
            if (impl.unwrap() == null) {
                LOG.bigError("No API Binding found! Replacing with dummy implementation.\nNote that this may lead to future errors");
                return load$defaultProvider.invoke();
            }
            return impl.unwrap();
        };

        final T value = load.invoke(lookUpInterface, defaultProvider);
        LOG.info("Using " + KClass.get(value.getClass()).getQualifiedName() + " as API Binding for Boson class " + lookUpInterface.getQualifiedName());
        return value;
    }

    @Nonnull
    private static <T> List<T> toList(@Nonnull final Iterator<T> $this$receiver) {
        final List<T> list = new ArrayList<>();
        $this$receiver.forEachRemaining(list::add);
        return list;
    }

    @Nonnull
    private static <T> Nullable<T> firstOrNull(@Nonnull final List<T> $this$receiver, @Nonnull final KFunction1<T, Boolean> predicate) {
        for (@Nonnull final T element : $this$receiver) if (predicate.invoke(element)) return Nullable.get(element);
        return Nullable.get(null);
    }
}
