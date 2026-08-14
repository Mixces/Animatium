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

package org.visuals.legacy.animatium.handler.config.bundle.entry

import dev.isxander.yacl3.api.Option
import org.visuals.legacy.animatium.handler.config.category.Category
import org.visuals.legacy.animatium.handler.config.category.option.FloatRangeOptions
import org.visuals.legacy.animatium.handler.config.category.option.OptionBuilder
import java.util.*
import java.util.function.BiConsumer

data class FloatRangeEntry(
    val name: String,
    val listener: Optional<BiConsumer<Option<Float>, Float>>,
    val min: Float,
    val max: Float,
    val step: Float
) : OptionEntrySupplier<Float> {
    override fun create(defaults: Category, config: Category): Option<Float> {
        val option = OptionBuilder(this.name, FloatRangeOptions(this.min, this.max, this.step))
        this.listener.ifPresent { option.instant().listener(it) }
        return option.build(defaults, config)
    }

    override fun name() = this.name
}