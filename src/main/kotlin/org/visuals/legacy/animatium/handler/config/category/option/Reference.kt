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

package org.visuals.legacy.animatium.handler.config.category.option

import org.visuals.legacy.animatium.handler.config.category.Category
import java.lang.reflect.Field

data class Reference<S>(
    @JvmField
    val defaultField: Field?,

    @JvmField
    val currentField: Field?,

    @JvmField
    val defaultValue: S?
) {
    companion object {
        @JvmStatic
        fun <T : Category, S> get(name: String, defaults: T, config: T): Reference<S> {
            var defaultField: Field? = null
            var currentField: Field? = null
            var defaultValue: S? = null

            val defaultsClazz = defaults::class.java
            try {
                defaultField = defaultsClazz.getField(name)
                defaultValue = defaultField?.get(defaults) as S?
            } catch (exception: ReflectiveOperationException) {
                exception.printStackTrace()
            }

            val currentClazz = config::class.java
            try {
                currentField = currentClazz.getField(name)
            } catch (exception: ReflectiveOperationException) {
                exception.printStackTrace()
            }

            return Reference(defaultField, currentField, defaultValue)
        }
    }
}