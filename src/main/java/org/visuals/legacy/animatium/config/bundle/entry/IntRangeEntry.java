package org.visuals.legacy.animatium.config.bundle.entry;

import dev.isxander.yacl3.api.Option;
import org.visuals.legacy.animatium.config.category.Category;

import java.util.Optional;
import java.util.function.BiConsumer;

public record IntRangeEntry(String name, Optional<BiConsumer<Option<Integer>, Integer>> listener, int min, int max,
                            int step) implements OptionEntrySupplier<Integer> {
    @Override
    public Option<Integer> create(final Category defaults, final Category config) {
        final Category.OptionBuilder<Float> option = Category.OptionBuilder.of(this.name, Category.OptionType.INT);
        option.slider(this.min, this.max, this.step);
        this.listener.ifPresent(it -> option.instant().listener((BiConsumer<Option<?>, ?>) (Object) it));
        return option.build(defaults, config);
    }

    @Override
    public EntryType type() {
        return EntryType.INT;
    }
}
