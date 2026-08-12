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

package org.visuals.legacy.animatium.config.category;

import dev.isxander.yacl3.api.Binding;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.StateManager;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.animatium.AnimatiumConstants;
import org.visuals.legacy.animatium.config.bundle.EntryBundle;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Category {
    public enum OptionType {
        BOOLEAN(false),
        INT(true),
        FLOAT(true),
        ENUM(false),
        COLOR(false);

        private final boolean sliderCapable;

        OptionType(final boolean sliderCapable) {
            this.sliderCapable = sliderCapable;
        }
    }

    public static class OptionBuilder<T> {
        private final String name;
        private final OptionType type;

        private BiConsumer<Option<?>, ?> listener = null;

        private boolean instant = false;
        private boolean slider = false;
        private Object min = null;
        private Object max = null;
        private Object step = null;

        private Class<?> enumClazz;

        OptionBuilder(final String name, final OptionType type) {
            this.name = name;
            this.type = type;
        }

        public static <T> OptionBuilder<T> of(final String name, final OptionType type) {
            return new OptionBuilder<>(name, type);
        }

        public static <S extends Enum<S>> OptionBuilder<Enum<S>> ofEnum(final String name, final Class<S> enumClazz) {
            final OptionBuilder<Enum<S>> builder = new OptionBuilder<>(name, OptionType.ENUM);
            builder.enumClazz = enumClazz;
            return builder;
        }

        public <S> OptionBuilder<T> slider(final S min, final S max, final S step) {
            if (this.type == null || !this.type.sliderCapable) {
                throw new RuntimeException("Option doesn't allow slider.");
            } else {
                this.slider = true;
                this.min = min;
                this.max = max;
                this.step = step;
                return this;
            }
        }

        public OptionBuilder<T> listener(final BiConsumer<Option<?>, ?> listener) {
            this.listener = listener;
            return this;
        }

        public OptionBuilder<T> instant() {
            this.instant = true;
            return this;
        }

        public <CategoryLike extends Category, K> Option<K> build(final CategoryLike defaults, final CategoryLike current) {
            final Function<Option<K>, ControllerBuilder<K>> controllerBuilder = switch (this.type) {
                case BOOLEAN -> (opt) ->
                        (ControllerBuilder<K>) TickBoxControllerBuilder.create((Option<Boolean>) opt);

                case INT -> (opt) -> {
                    if (this.slider) {
                        return (ControllerBuilder<K>) IntegerSliderControllerBuilder
                                .create((Option<Integer>) opt)
                                .range((int) this.min, (int) this.max)
                                .step((int) this.step);
                    } else {
                        throw new RuntimeException("TODO: Int non-slider");
                    }
                };

                case FLOAT -> (opt) -> {
                    if (this.slider) {
                        return (ControllerBuilder<K>) FloatSliderControllerBuilder
                                .create((Option<Float>) opt)
                                .range((float) this.min, (float) this.max)
                                .step((float) this.step);
                    } else {
                        throw new RuntimeException("TODO: Float non-slider");
                    }
                };

                case ENUM -> (opt) ->
                        EnumControllerBuilder
                                .create((Option<? extends Enum>) opt)
                                .enumClass(enumClazz)
                                .formatValue(it -> Component.translatable(AnimatiumConstants.MOD_ID + ".enum." + enumClazz.getSimpleName() + "." + ((Enum<?>) it).name()));

                case COLOR -> (opt) ->
                        (ControllerBuilder<K>) ColorControllerBuilder.create((Option<Color>) opt)
                                .allowAlpha(true);
            };

            final Reference<K> reference = Reference.get(this.name, defaults, current);
            final Binding<K> binding = Binding.generic(reference.defaultValue, () -> {
                try {
                    return (K) reference.currentField.get(current);
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                    return reference.defaultValue;
                }
            }, (newVal) -> {
                try {
                    reference.currentField.set(current, newVal);
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            });

            final Option.Builder<K> builder = Option.createBuilder();
            final String id = AnimatiumConstants.MOD_ID + "." + this.name;
            builder.name(Component.translatable(id));
            builder.description(OptionDescription.of(Component.translatable(id + ".description")));
            builder.controller(controllerBuilder);
            if (this.listener != null) {
                builder.listener((BiConsumer<Option<K>, K>) (Object) this.listener);
            }

            if (this.instant) {
                builder.stateManager(StateManager.createInstant(binding));
            } else {
                builder.binding(binding);
            }

            return builder.build();
        }
    }

    public abstract EntryBundle bundle();

    private static class Reference<S> {
        public Field defaultField;
        public Field currentField;
        public S defaultValue;

        public static <T extends Category, S> Reference<S> get(final String fieldName, final T defaults, final T current) {
            final Reference<S> reference = new Reference<>();

            final Class<?> defaultsClazz = defaults.getClass();
            try {
                reference.defaultField = defaultsClazz.getField(fieldName);
                reference.defaultValue = (S) reference.defaultField.get(defaults);
            } catch (NoSuchFieldException | IllegalAccessException exception) {
                exception.printStackTrace();
            }

            final Class<?> currentClazz = current.getClass();
            try {
                reference.currentField = currentClazz.getField(fieldName);
            } catch (NoSuchFieldException exception) {
                exception.printStackTrace();
            }

            return reference;
        }
    }
}
