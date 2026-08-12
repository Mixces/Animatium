package org.visuals.legacy.animatium.config.bundle.entry;

import dev.isxander.yacl3.api.Option;
import org.visuals.legacy.animatium.config.category.Category;

import java.util.Optional;
import java.util.function.BiConsumer;

public record EnumEntry<S extends Enum<S>>(String name, Optional<BiConsumer<Option<Enum<S>>, Enum<S>>> listener,
                                           Class<?> enumClass) implements OptionEntrySupplier<Enum<S>> {
    @Override
    public Option<Enum<S>> create(final Category defaults, final Category config) {
        final Category.OptionBuilder<Enum<S>> option = Category.OptionBuilder.ofEnum(this.name, (Class<S>) this.enumClass);
        this.listener.ifPresent(it -> option.instant().listener((BiConsumer<Option<?>, ?>) (Object) it));
        return option.build(defaults, config);
    }

    @Override
    public EntryType type() {
        return EntryType.ENUM;
    }
}
