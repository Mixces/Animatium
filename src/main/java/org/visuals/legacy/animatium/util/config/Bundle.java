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

package org.visuals.legacy.animatium.util.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import org.visuals.legacy.animatium.config.category.Category;

import java.util.function.BiConsumer;

public abstract class Bundle {
    public abstract void install(final ConfigCategory.Builder builder, final Category defaults, final Category config);

    public abstract Bundle booleanEntry(final String name, final BiConsumer<Option<Boolean>, Boolean> listener);

    public Bundle booleanEntry(final String name) {
        return this.booleanEntry(name, (opt, value) -> {
        });
    }

    public abstract Bundle intRange(final String name, final int min, final int max, final int step);

    public Bundle intRange(final String name, final int min, final int max) {
        return this.intRange(name, min, max, 1);
    }

    public abstract Bundle floatRange(final String name, final float min, final float max, final float step);

    public Bundle floatRange(final String name, final float min, final float max) {
        return this.floatRange(name, min, max, 0.1F);
    }

    public abstract <S extends Enum<S>> Bundle enumEntry(final String name, final Class<S> enumClazz, final BiConsumer<Option<Enum<S>>, Enum<S>> listener);

    public <S extends Enum<S>> Bundle enumEntry(final String name, final Class<S> enumClazz) {
        return this.enumEntry(name, enumClazz, (opt, value) -> {
        });
    }
}
