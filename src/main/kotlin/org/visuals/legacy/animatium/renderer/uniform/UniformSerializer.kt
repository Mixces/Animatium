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
                calculator.putInt()
            }
        }

        val VECTOR2F: UniformSerializer<Vector2f> = object : UniformSerializer<Vector2f>() {
            override fun put(buffer: ByteBuffer, value: Vector2f) {
                value.get(buffer)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putInt()
            }
        }

        val VECTOR2I: UniformSerializer<Vector2i> = object : UniformSerializer<Vector2i>() {
            override fun put(buffer: ByteBuffer, value: Vector2i) {
                value.get(buffer)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putInt()
            }
        }

        val VECTOR3F: UniformSerializer<Vector3f> = object : UniformSerializer<Vector3f>() {
            override fun put(buffer: ByteBuffer, value: Vector3f) {
                value.get(buffer)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putInt()
            }
        }

        val VECTOR3I: UniformSerializer<Vector3i> = object : UniformSerializer<Vector3i>() {
            override fun put(buffer: ByteBuffer, value: Vector3i) {
                value.get(buffer)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putInt()
            }
        }

        val VECTOR4F: UniformSerializer<Vector4f> =
            object : UniformSerializer<Vector4f>() {
                override fun put(buffer: ByteBuffer, value: Vector4f) {
                    value.get(buffer)
                }

                override fun size(calculator: Std140SizeCalculator) {
                    calculator.putInt()
                }
            }

        val VECTOR4I: UniformSerializer<Vector4i> = object : UniformSerializer<Vector4i>() {
            override fun put(buffer: ByteBuffer, value: Vector4i) {
                value.get(buffer)
            }

            override fun size(calculator: Std140SizeCalculator) {
                calculator.putInt()
            }
        }
    }

    abstract fun put(buffer: ByteBuffer, value: T)

    abstract fun size(calculator: Std140SizeCalculator)
}