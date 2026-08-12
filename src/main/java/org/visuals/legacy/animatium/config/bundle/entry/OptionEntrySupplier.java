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

package org.visuals.legacy.animatium.config.bundle.entry;

import dev.isxander.yacl3.api.Option;
import org.visuals.legacy.animatium.config.category.Category;

public interface OptionEntrySupplier<T> {
    Option<T> create(final Category defaults, final Category config);

    String name();

    EntryType type();

    default T value() {
        throw new UnsupportedOperationException("The supplier used has not been bootstrapped yet!");
    }

    static <T> OptionEntrySupplier<T> bootstrap(final Class<? extends Category> clazz, final Category category, final OptionEntrySupplier<T> supplier) {
        return new OptionEntrySupplier<>() {
            @Override
            public Option<T> create(final Category defaults, final Category config) {
                return supplier.create(defaults, config);
            }

            @Override
            public String name() {
                return supplier.name();
            }

            @Override
            public EntryType type() {
                return supplier.type();
            }

            @Override
            public T value() {
                try {
                    return (T) clazz.getField(this.name()).get(category);
                } catch (final Exception exception) {
                    return null;
                }
            }
        };
    }
}
