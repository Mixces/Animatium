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
import com.mojang.blaze3d.buffers.GpuBufferSlice
import com.mojang.blaze3d.buffers.Std140Builder
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import com.mojang.blaze3d.systems.RenderSystem

class UniformStorage : AutoCloseable {
    val name: String

    private val keys: MutableList<UniformKey<*>>
    private val values = LinkedHashMap<UniformKey<*>, Any?>()
    private val size: Int
    private var closed = false

    private constructor(name: String, keys: List<UniformKey<*>>, size: Int) {
        this.name = name
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
            throw RuntimeException("Cannot set value in Uniform Storage (${this.name}) as it has been closed!")
        } else if (!this.keys.contains(key)) {
            throw UnsupportedOperationException("Uniform storage does not contain key '${key.name}'!")
        } else {
            this.values[key] = value
            return this
        }
    }

    fun <T> get(key: UniformKey<T>): T? =
        if (!this.keys.contains(key)) {
            null
        } else {
            this.values[key] as T?
        }

    fun upload(): GpuBufferSlice {
        if (this.closed) {
            throw RuntimeException("Cannot upload Uniform Storage (${this.name}) as it has been closed!")
        } else {
            val device = RenderSystem.getDevice()
            val transientMemory = device.createCommandEncoder().transientMemory()
            val alignment = device.deviceInfo.limits.minUniformOffsetAlignment
            transientMemory.allocateGpuMapped(
                this.size.toLong(),
                alignment.toLong(),
                GpuBuffer.USAGE_UNIFORM
            ).use { view ->
                val builder = Std140Builder.intoBuffer(view.data)
                for (entry in this.values) {
                    val key = entry.key
                    val value = entry.value
                        ?: throw RuntimeException("Failed to bind \"${key.name}\" in Uniform Storage (${this.name}) as value is not set!")
                    (key.serializer as UniformSerializer<Any>).put(builder, value)
                }

                return view.slice
            }
        }
    }

    fun isClosed() = this.closed

    override fun close() {
        if (!this.closed) {
            this.closed = true
            this.keys.clear()
            this.values.clear()
        }
    }

    class Builder(private val name: String) {
        private val keys = arrayListOf<UniformKey<*>>()
        private val calculator = Std140SizeCalculator()

        fun with(key: UniformKey<*>): Builder {
            this.keys.add(key)
            key.serializer.size(this.calculator)
            return this
        }

        fun build(): UniformStorage {
            val size = this.calculator.get()
            if (size == 0) {
                throw RuntimeException("Cannot build Uniform Storage (${this.name}) as it contains no uniforms!")
            } else {
                return UniformStorage(this.name, this.keys, size)
            }
        }
    }
}