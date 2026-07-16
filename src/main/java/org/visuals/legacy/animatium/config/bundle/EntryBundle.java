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
import org.visuals.legacy.animatium.config.category.Category;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class EntryBundle extends Bundle {
    protected final Set<Entry<?>> entries;
    protected final Set<Group> groups;
    private final String name;
    protected Class<? extends Category> categoryClass;
    protected Category category;

    public EntryBundle(final Category category, final String name) {
        this.entries = new LinkedHashSet<>();
        this.groups = new LinkedHashSet<>();
        this.category = category;
        this.categoryClass = category == null ? null : category.getClass();
        this.name = name;
    }

    @Override
    public void install(final ConfigCategory.Builder builder, final Category defaults, final Category config) {
        for (final Group group : this.groups) {
            final OptionGroup.Builder groupBuilder = OptionGroup.createBuilder();
            groupBuilder.name(Component.translatable("animatium.category." + this.name + ".group." + group.getName()));
            group.install(groupBuilder, defaults, config);
            builder.group(groupBuilder.build());
        }

        for (final Entry<?> entry : this.entries) {
            builder.option(entry.createOption(defaults, config));
        }
    }

    @Override
    public EntryBundle booleanEntry(final String name, final BiConsumer<Option<Boolean>, Boolean> listener) {
        this.entries.add(new BooleanEntry(name, listener));
        return this;
    }

    @Override
    public Bundle intRange(final String name, final int min, final int max, final int step) {
        this.entries.add(new IntRange(name, min, max, step));
        return this;
    }

    @Override
    public EntryBundle floatRange(final String name, final float min, final float max, final float step) {
        this.entries.add(new FloatRange(name, min, max, step));
        return this;
    }

    @Override
    public <S extends Enum<S>> EntryBundle enumEntry(final String name, final Class<S> enumClazz, final BiConsumer<Option<Enum<S>>, Enum<S>> listener) {
        this.entries.add(new EnumEntry<>(name, enumClazz, listener));
        return this;
    }

    public String getName() {
        return this.name;
    }

    public EntryBundle group(final Group group) {
        this.groups.add(group);
        group.category = this.category;
        group.categoryClass = this.categoryClass;
        return this;
    }

    public Collection<Entry<?>> entries() {
        final ArrayList<Entry<?>> entries = new ArrayList<>(this.entries);
        for (final Group group : this.groups) {
            // TODO: Find a better way to do this without losing information
            entries.addAll(group.entries);
        }

        return entries;
    }

    public enum Type {
        BOOLEAN,
        INT,
        FLOAT,
        ENUM
    }

    public static class Group extends EntryBundle {
        public Group(final String name) {
            super(null, name);
        }

        @Override
        public void install(final ConfigCategory.Builder builder, final Category defaults, final Category config) {
            for (final Entry<?> entry : this.entries) {
                builder.option(entry.createOption(defaults, config));
            }
        }

        public void install(final OptionGroup.Builder builder, final Category defaults, final Category config) {
            for (final Entry<?> entry : this.entries) {
                builder.option(entry.createOption(defaults, config));
            }
        }

        @Override
        public EntryBundle group(final Group group) {
            throw new UnsupportedOperationException();
        }
    }

    public abstract class Entry<T> {
        public final String name;
        public final Type type;
        public final BiConsumer<Option<T>, T> listener;

        public Entry(final String name, final Type type, final BiConsumer<Option<T>, T> listener) {
            this.name = name;
            this.type = type;
            this.listener = listener;
        }

        public abstract Option<T> createOption(final Category defaults, final Category config);

        public T value() {
            try {
                return (T) categoryClass.getField(this.name).get(category);
            } catch (Exception exception) {
                return null;
            }
        }
    }

    private class BooleanEntry extends Entry<Boolean> {
        public BooleanEntry(final String name, final BiConsumer<Option<Boolean>, Boolean> listener) {
            super(name, Type.BOOLEAN, listener);
        }

        @Override
        public Option<Boolean> createOption(final Category defaults, final Category config) {
            final Category.OptionBuilder<Boolean> option = Category.OptionBuilder.of(this.name);
            option.type(Category.OptionType.BOOLEAN);
            if (this.listener != null) {
                option.instant().listener((BiConsumer<Option<?>, ?>) (Object) this.listener);
            }

            return option.build(defaults, config);
        }
    }

    private class IntRange extends Entry<Integer> {
        private final int min;
        private final int max;
        private final int step;

        public IntRange(final String name, final int min, final int max, final int step) {
            super(name, Type.INT, null);
            this.min = min;
            this.max = max;
            this.step = step;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public int getStep() {
            return this.step;
        }

        @Override
        public Option<Integer> createOption(final Category defaults, final Category config) {
            final Category.OptionBuilder<Float> option = Category.OptionBuilder.of(this.name);
            option.type(Category.OptionType.INT);
            option.slider(this.min, this.max, this.step);
            if (this.listener != null) {
                option.instant().listener((BiConsumer<Option<?>, ?>) (Object) this.listener);
            }

            return option.build(defaults, config);
        }
    }

    private class FloatRange extends Entry<Float> {
        private final float min;
        private final float max;
        private final float step;

        public FloatRange(final String name, final float min, final float max, final float step) {
            super(name, Type.FLOAT, null);
            this.min = min;
            this.max = max;
            this.step = step;
        }

        public float getMin() {
            return this.min;
        }

        public float getMax() {
            return this.max;
        }

        public float getStep() {
            return this.step;
        }

        @Override
        public Option<Float> createOption(final Category defaults, final Category config) {
            final Category.OptionBuilder<Float> option = Category.OptionBuilder.of(this.name);
            option.type(Category.OptionType.FLOAT);
            option.slider(this.min, this.max, this.step);
            if (this.listener != null) {
                option.instant().listener((BiConsumer<Option<?>, ?>) (Object) this.listener);
            }

            return option.build(defaults, config);
        }
    }

    private class EnumEntry<S extends Enum<S>> extends Entry<Enum<S>> {
        private final Class<?> enumClass;

        public EnumEntry(final String name, final Class<S> enumClass, final BiConsumer<Option<Enum<S>>, Enum<S>> listener) {
            super(name, Type.ENUM, listener);
            this.enumClass = enumClass;
        }

        public Class<?> getEnumClass() {
            return this.enumClass;
        }

        @Override
        public Option<Enum<S>> createOption(final Category defaults, final Category config) {
            final Category.OptionBuilder<Enum<S>> option = Category.OptionBuilder.ofEnum(this.name, (Class<S>) this.enumClass);
            if (this.listener != null) {
                option.instant().listener((BiConsumer<Option<?>, ?>) (Object) this.listener);
            }

            return option.build(defaults, config);
        }
    }
}
