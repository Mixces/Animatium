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

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.animatium.Animatium;

import java.lang.reflect.Field;
import java.util.function.Function;

public abstract class Category {
    public static <T extends Category> Option<Boolean> booleanOption(String fieldName, T defaults, T current) {
        return option(fieldName, defaults, current, TickBoxControllerBuilder::create);
    }

    public static <T extends Category, S extends Enum<S>> Option<S> enumOption(String fieldName, T defaults, T current, Class<S> enumClazz) {
        return option(fieldName, defaults, current, (opt) -> EnumControllerBuilder.create(opt).enumClass(enumClazz).formatValue(it -> Component.translatable(Animatium.MOD_ID + ".enum." + enumClazz.getSimpleName() + "." + it.name())));
    }

    public static <T extends Category> Option<Float> floatSliderOption(String fieldName, T defaults, T current, float min, float max, float step) {
        return option(fieldName, defaults, current, (opt) -> FloatSliderControllerBuilder.create(opt).range(min, max).step(step));
    }

    public static <T extends Category, S> Option<S> option(String fieldName, T defaults, T current, Function<Option<S>, ControllerBuilder<S>> controllerBuilder) {
        final Reference<S> reference = Reference.get(fieldName, defaults, current);
        final String id = Animatium.MOD_ID + "." + fieldName;
        return Option.<S>createBuilder()
                .name(Component.translatable(id))
                .description(OptionDescription.of(Component.translatable(id + ".description")))
                .binding(reference.defaultValue,
                        () -> {
                            try {
                                return (S) reference.currentField.get(current);
                            } catch (IllegalAccessException exception) {
                                exception.printStackTrace();
                                return reference.defaultValue;
                            }
                        },
                        (newVal) -> {
                            try {
                                reference.currentField.set(current, newVal);
                            } catch (IllegalAccessException exception) {
                                exception.printStackTrace();
                            }
                        })
                .controller(controllerBuilder)
                .build();
    }

    private static class Reference<S> {
        public Field defaultField;
        public Field currentField;
        public S defaultValue;

        public static <T extends Category, S> Reference<S> get(String fieldName, T defaults, T current) {
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
