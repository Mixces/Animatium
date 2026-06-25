package org.visuals.legacy.animatium.renderer.uniform

import btw.lowercase.renderer.uniform.UniformSerializer
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