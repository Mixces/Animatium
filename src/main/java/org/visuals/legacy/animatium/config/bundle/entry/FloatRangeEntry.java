package org.visuals.legacy.animatium.config.bundle.entry;

import dev.isxander.yacl3.api.Option;
import org.visuals.legacy.animatium.config.category.Category;

import java.util.Optional;
import java.util.function.BiConsumer;

public record FloatRangeEntry(String name, Optional<BiConsumer<Option<Float>, Float>> listener, float min, float max,
                              float step) implements OptionEntrySupplier<Float> {
    @Override
    public Option<Float> create(final Category defaults, final Category config) {
        final Category.OptionBuilder<Float> option = Category.OptionBuilder.of(this.name, Category.OptionType.FLOAT);
        option.slider(this.min, this.max, this.step);
        this.listener.ifPresent(it -> option.instant().listener((BiConsumer<Option<?>, ?>) (Object) it));
        return option.build(defaults, config);
    }

    @Override
    public EntryType type() {
        return EntryType.FLOAT;
    }
}
