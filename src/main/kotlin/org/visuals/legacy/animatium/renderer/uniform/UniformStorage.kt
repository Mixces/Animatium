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

package org.visuals.legacy.animatium.renderer.uniform

import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import com.mojang.blaze3d.systems.CommandEncoder
import com.mojang.blaze3d.systems.RenderSystem
import org.lwjgl.system.MemoryStack

class UniformStorage : AutoCloseable {
    private val name: String
    private val ubo: GpuBuffer
    private val keys: MutableList<UniformKey<*>>
    private val values = LinkedHashMap<UniformKey<*>, Any?>()
    private val size: Int

    private var dirty = true
    private var closed = false

    private constructor(name: String, keys: List<UniformKey<*>>, size: Int) {
        this.name = name
        this.ubo = RenderSystem.getDevice().createBuffer(
            { "Dynamic Uniform Storage (${this.name})" },
            GpuBuffer.USAGE_UNIFORM or GpuBuffer.USAGE_COPY_DST,
            size.toLong()
        )
        this.keys = keys.toMutableList()
        this.size = size
        for (key in this.keys) {
            this.values[key] = null
        }
    }

    companion object {
        fun builder(name: String) = Builder(name)
    }

    fun <T> set(key: UniformKey<T>, value: T): UniformStorage {
        if (this.closed) {
            throw RuntimeException("Cannot set value in uniform storage \"" + this.name + "\" as it has been closed!")
        } else if (!this.keys.contains(key)) {
            throw UnsupportedOperationException("Uniform storage does not contain key '" + key.name() + "'!")
        } else {
            val currentValue = this.values[key]
            if (currentValue != values) {
                this.dirty = true
                this.values[key] = value
            }

            return this
        }
    }

    fun <T> get(key: UniformKey<T>): T? =
        if (!this.keys.contains(key)) {
            null
        } else {
            this.values[key] as T?
        }

    fun update(commandEncoder: CommandEncoder) {
        if (this.closed) {
            throw RuntimeException("Cannot update uniform storage \"" + this.name + "\" as it has been closed!")
        } else if (this.dirty) {
            val slice = this.ubo.slice()
            MemoryStack.stackPush().use { stack ->
                val buffer = stack.malloc(this.size)
                for (entry in this.values) {
                    val key = entry.key
                    val value = entry.value
                    if (value == null) {
                        throw RuntimeException("Failed to bind \"" + key.name() + "\" in Uniform Storage (" + this.name + ") as value is not set!")
                    } else {
                        (key.serializer() as UniformSerializer<Any>).put(buffer, value)
                    }
                }

                commandEncoder.writeToBuffer(slice, buffer)
            }

            this.dirty = false
        }
    }

    fun update() = this.update(RenderSystem.getDevice().createCommandEncoder())

    fun slice() = this.ubo.slice()

    fun isClose() = this.closed

    override fun close() {
        if (!this.closed) {
            this.closed = true
            this.ubo.close()
            this.keys.clear()
            this.values.clear()
        }
    }

    class Builder(private val name: String) {
        private val keys = arrayListOf<UniformKey<*>>()
        private val calculator = Std140SizeCalculator()

        fun with(key: UniformKey<*>): Builder {
            this.keys.add(key)
            key.serializer().size(this.calculator)
            return this
        }

        fun build(): UniformStorage {
            val size = this.calculator.get()
            if (size == 0) {
                throw RuntimeException("Cannot build Uniform Storage (" + this.name + ") as it contains no uniforms!")
            } else {
                return UniformStorage(this.name, this.keys, size)
            }
        }
    }
}