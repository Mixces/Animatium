package org.visuals.legacy.animatium.config.bundle.entry;

import dev.isxander.yacl3.api.Option;
import org.visuals.legacy.animatium.config.category.Category;

public interface OptionEntrySupplier<T> {
    Option<T> create(final Category defaults, final Category config);

    String name();

    EntryType type();

    default T value() {
        throw new UnsupportedOperationException("The supplier used has not been bootstrapped yet!");
    }

    static <T> OptionEntrySupplier<T> bootstrap(final Class<? extends Category> clazz, final Category category, final OptionEntrySupplier<T> supplier) {
        return new OptionEntrySupplier<>() {
            @Override
            public Option<T> create(final Category defaults, final Category config) {
                return supplier.create(defaults, config);
            }

            @Override
            public String name() {
                return supplier.name();
            }

            @Override
            public EntryType type() {
                return supplier.type();
            }

            @Override
            public T value() {
                try {
                    return (T) clazz.getField(this.name()).get(category);
                } catch (final Exception exception) {
                    return null;
                }
            }
        };
    }
}
