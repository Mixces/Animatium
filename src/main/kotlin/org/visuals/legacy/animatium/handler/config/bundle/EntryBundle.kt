/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2027 lowercasebtw
 * Copyright (C) 2024-2027 mixces
 * Copyright (C) 2024-2027 Contributors to the project retain their copyright
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

package org.visuals.legacy.animatium.handler.config.bundle

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionGroup
import net.minecraft.network.chat.Component
import org.visuals.legacy.animatium.handler.config.bundle.entry.*
import org.visuals.legacy.animatium.handler.config.category.Category
import java.awt.Color
import java.util.*
import java.util.function.BiConsumer

open class EntryBundle(protected val category: Category, private val name: String) : Bundle() {
    protected val entries = LinkedHashSet<OptionEntrySupplier<*>>()
    protected val groups = LinkedHashSet<GroupBundle>()
    protected val categoryClass = category::class.java

    override fun install(builder: ConfigCategory.Builder, defaults: Category, config: Category) {
        for (group in this.groups) {
            val groupBuilder = OptionGroup.createBuilder()
            groupBuilder.name(Component.translatable("animatium.category." + this.name + ".group." + group.name()))
            group.install(groupBuilder, defaults, config)
            builder.group(groupBuilder.build())
        }

        for (entry in this.entries) {
            builder.option(entry.create(defaults, config))
        }
    }

    override fun booleanEntry(name: String, listener: BiConsumer<Option<Boolean>, Boolean>): Bundle {
        this.entries.add(this.bootstrap(BooleanEntry(name, Optional.of(listener))))
        return this
    }

    override fun intRange(name: String, min: Int, max: Int, step: Int): Bundle {
        this.entries.add(this.bootstrap(IntRangeEntry(name, Optional.empty(), min, max, step)))
        return this
    }

    override fun floatRange(name: String, min: Float, max: Float, step: Float): Bundle {
        this.entries.add(this.bootstrap(FloatRangeEntry(name, Optional.empty(), min, max, step)))
        return this
    }

    override fun <S : Enum<S>> enumEntry(name: String, enumClazz: Class<S>, listener: BiConsumer<Option<S>, S>): Bundle {
        this.entries.add(this.bootstrap(EnumEntry(name, Optional.of(listener), enumClazz)))
        return this
    }

    override fun colorEntry(name: String, listener: BiConsumer<Option<Color>, Color>): Bundle {
        this.entries.add(this.bootstrap(ColorEntry(name, Optional.of(listener))))
        return this
    }

    open fun group(name: String): GroupBundle {
        val group = GroupBundle(this.category, name)
        this.groups.add(group)
        return group
    }

    fun entries(): Collection<OptionEntrySupplier<*>> {
        val entries = ArrayList<OptionEntrySupplier<*>>(this.entries)
        for (group in this.groups) {
            entries.addAll(group.entries)
        }

        return entries
    }

    fun name() = this.name

    private fun <T> bootstrap(supplier: OptionEntrySupplier<T>) = OptionEntrySupplier.bootstrap(this.categoryClass, this.category, supplier)
}