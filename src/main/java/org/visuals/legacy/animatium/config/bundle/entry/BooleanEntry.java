package org.visuals.legacy.animatium.config.bundle.entry;

import dev.isxander.yacl3.api.Option;
import org.visuals.legacy.animatium.config.category.Category;

import java.util.Optional;
import java.util.function.BiConsumer;

public record BooleanEntry(String name,
                           Optional<BiConsumer<Option<Boolean>, Boolean>> listener) implements OptionEntrySupplier<Boolean> {
    @Override
    public Option<Boolean> create(final Category defaults, final Category config) {
        final Category.OptionBuilder<Boolean> option = Category.OptionBuilder.of(this.name, Category.OptionType.BOOLEAN);
        this.listener.ifPresent(it -> option.instant().listener((BiConsumer<Option<?>, ?>) (Object) it));
        return option.build(defaults, config);
    }

    @Override
    public EntryType type() {
        return EntryType.BOOLEAN;
    }
}
