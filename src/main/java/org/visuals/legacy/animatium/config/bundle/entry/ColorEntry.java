package org.visuals.legacy.animatium.config.bundle.entry;

import dev.isxander.yacl3.api.Option;
import org.visuals.legacy.animatium.config.category.Category;

import java.awt.*;
import java.util.Optional;
import java.util.function.BiConsumer;

public record ColorEntry(String name,
                         Optional<BiConsumer<Option<Color>, Color>> listener) implements OptionEntrySupplier<Color> {
    @Override
    public Option<Color> create(final Category defaults, final Category config) {
        final Category.OptionBuilder<Color> option = Category.OptionBuilder.of(this.name, Category.OptionType.COLOR);
        this.listener.ifPresent(it -> option.instant().listener((BiConsumer<Option<?>, ?>) (Object) it));
        return option.build(defaults, config);
    }

    @Override
    public EntryType type() {
        return EntryType.COLOR;
    }
}
