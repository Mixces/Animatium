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

import org.joml.*

interface UniformKey<T> {
    companion object {
        @JvmStatic
        fun Integer(name: String): UniformKey<Int> = Impl(name, UniformSerializer.INTEGER)

        @JvmStatic
        fun Float(name: String): UniformKey<Float> = Impl(name, UniformSerializer.FLOAT)

        @JvmStatic
        fun Matrix4f(name: String): UniformKey<Matrix4f> = Impl(name, UniformSerializer.MATRIX4F)

        @JvmStatic
        fun Vector2f(name: String): UniformKey<Vector2f> = Impl(name, UniformSerializer.VECTOR2F)

        @JvmStatic
        fun Vector2i(name: String): UniformKey<Vector2i> = Impl(name, UniformSerializer.VECTOR2I)

        @JvmStatic
        fun Vector3f(name: String): UniformKey<Vector3f> = Impl(name, UniformSerializer.VECTOR3F)

        @JvmStatic
        fun Vector3i(name: String): UniformKey<Vector3i> = Impl(name, UniformSerializer.VECTOR3I)

        @JvmStatic
        fun Vector4f(name: String): UniformKey<Vector4f> = Impl(name, UniformSerializer.VECTOR4F)

        @JvmStatic
        fun Vector4i(name: String): UniformKey<Vector4i> = Impl(name, UniformSerializer.VECTOR4I)
    }

    fun name(): String

    fun serializer(): UniformSerializer<T>

    private data class Impl<T>(val name: String, val serializer: UniformSerializer<T>) : UniformKey<T> {
        override fun name(): String = this.name

        override fun serializer(): UniformSerializer<T> = this.serializer
    }
}