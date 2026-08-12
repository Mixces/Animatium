/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2025 lowercasebtw
 * Copyright (C) 2024-2025 mixces
 * Copyright (C) 2024-2025 Contributors to the project retain their copyright
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * "MINECRAFT" LINKING EXCEPTION TO THE GPL
 */

package org.visuals.legacy.animatium.config.bundle;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.animatium.config.bundle.entry.*;
import org.visuals.legacy.animatium.config.category.Category;
import org.visuals.legacy.animatium.handler.config.bundle.Bundle;

import java.awt.*;
import java.util.*;
import java.util.function.BiConsumer;

public class EntryBundle extends Bundle {
    protected final Set<OptionEntrySupplier<?>> entries;
    protected final Set<Group> groups;
    private final String name;
    protected Class<? extends Category> categoryClass;
    protected Category category;

    public EntryBundle(final Category category, final String name) {
        this.entries = new LinkedHashSet<>();
        this.groups = new LinkedHashSet<>();
        this.name = name;
        this.categoryClass = category == null ? null : category.getClass();
        this.category = category;
    }

    public String name() {
        return this.name;
    }

    @Override
    public void install(final ConfigCategory.Builder builder, final Category defaults, final Category config) {
        for (final Group group : this.groups) {
            final OptionGroup.Builder groupBuilder = OptionGroup.createBuilder();
            groupBuilder.name(Component.translatable("animatium.category." + this.name + ".group." + group.getName()));
            group.install(groupBuilder, defaults, config);
            builder.group(groupBuilder.build());
        }

        for (final OptionEntrySupplier<?> entry : this.entries) {
            builder.option(entry.create(defaults, config));
        }
    }

    @Override
    public EntryBundle booleanEntry(final String name, final BiConsumer<Option<Boolean>, Boolean> listener) {
        this.entries.add(this.bootstrap(new BooleanEntry(name, Optional.of(listener))));
        return this;
    }

    @Override
    public Bundle intRange(final String name, final int min, final int max, final int step) {
        this.entries.add(this.bootstrap(new IntRangeEntry(name, Optional.empty(), min, max, step)));
        return this;
    }

    @Override
    public EntryBundle floatRange(final String name, final float min, final float max, final float step) {
        this.entries.add(this.bootstrap(new FloatRangeEntry(name, Optional.empty(), min, max, step)));
        return this;
    }

    @Override
    public <S extends Enum<S>> EntryBundle enumEntry(final String name, final Class<S> enumClazz, final BiConsumer<Option<Enum<S>>, Enum<S>> listener) {
        this.entries.add(this.bootstrap(new EnumEntry<>(name, Optional.of(listener), enumClazz)));
        return this;
    }

    @Override
    public Bundle colorEntry(final String name, final BiConsumer<Option<Color>, Color> listener) {
        this.entries.add(this.bootstrap(new ColorEntry(name, Optional.of(listener))));
        return this;
    }

    private <T> OptionEntrySupplier<T> bootstrap(final OptionEntrySupplier<T> supplier) {
        return OptionEntrySupplier.bootstrap(this.categoryClass, this.category, supplier);
    }

    public String getName() {
        return this.name;
    }

    public Group group(final String name) {
        final Group group = new Group(this.category, name);
        this.groups.add(group);
        return group;
    }

    public Collection<OptionEntrySupplier<?>> entries() {
        final ArrayList<OptionEntrySupplier<?>> entries = new ArrayList<>(this.entries);
        for (final Group group : this.groups) {
            // TODO: Find a better way to do this without losing information
            entries.addAll(group.entries);
        }

        return entries;
    }

    public static class Group extends EntryBundle {
        private Group(final Category category, final String name) {
            super(category, name);
        }

        @Override
        public void install(final ConfigCategory.Builder builder, final Category defaults, final Category config) {
            for (final OptionEntrySupplier<?> entry : this.entries) {
                builder.option(entry.create(defaults, config));
            }
        }

        public void install(final OptionGroup.Builder builder, final Category defaults, final Category config) {
            for (final OptionEntrySupplier<?> entry : this.entries) {
                builder.option(entry.create(defaults, config));
            }
        }

        @Override
        public Group group(final String name) {
            throw new UnsupportedOperationException("You cannot create child groups!");
        }
    }
}
