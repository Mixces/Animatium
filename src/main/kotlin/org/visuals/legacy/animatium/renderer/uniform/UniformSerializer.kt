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

import com.mojang.blaze3d.buffers.Std140SizeCalculator
import org.joml.*
import java.nio.ByteBuffer

abstract class UniformSerializer<T> {
    companion object {
        val INTEGER: UniformSerializer<Int> = object : UniformSerializer<Int>() {
            override fun put(buffer: ByteBuffer, value: Int) {
                buffer.putInt(value)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putInt()
            }
        }

        val FLOAT: UniformSerializer<Float> = object : UniformSerializer<Float>() {
            override fun put(buffer: ByteBuffer, value: Float) {
                buffer.putFloat(value)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putFloat()
            }
        }

        val MATRIX4F: UniformSerializer<Matrix4f> = object : UniformSerializer<Matrix4f>() {
            override fun put(buffer: ByteBuffer, value: Matrix4f) {
                value.get(buffer)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putMat4f()
            }
        }

        val VECTOR2F: UniformSerializer<Vector2f> = object : UniformSerializer<Vector2f>() {
            override fun put(buffer: ByteBuffer, value: Vector2f) {
                value.get(buffer)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putVec2()
            }
        }

        val VECTOR2I: UniformSerializer<Vector2i> = object : UniformSerializer<Vector2i>() {
            override fun put(buffer: ByteBuffer, value: Vector2i) {
                value.get(buffer)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putIVec2()
            }
        }

        val VECTOR3F: UniformSerializer<Vector3f> = object : UniformSerializer<Vector3f>() {
            override fun put(buffer: ByteBuffer, value: Vector3f) {
                value.get(buffer)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putVec3()
            }
        }

        val VECTOR3I: UniformSerializer<Vector3i> = object : UniformSerializer<Vector3i>() {
            override fun put(buffer: ByteBuffer, value: Vector3i) {
                value.get(buffer)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putIVec3()
            }
        }

        val VECTOR4F: UniformSerializer<Vector4f> =
            object : UniformSerializer<Vector4f>() {
                override fun put(buffer: ByteBuffer, value: Vector4f) {
                    value.get(buffer)
                }

                override fun size(calculator: Std140SizeCalculator) {
                    calculator.putVec4()
                }
            }

        val VECTOR4I: UniformSerializer<Vector4i> = object : UniformSerializer<Vector4i>() {
            override fun put(buffer: ByteBuffer, value: Vector4i) {
                value.get(buffer)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putIVec4()
            }
        }
    }

    abstract fun put(buffer: ByteBuffer, value: T)

    abstract fun size(calculator: Std140SizeCalculator)
}