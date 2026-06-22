package org.visuals.legacy.animatium.renderer

import com.mojang.blaze3d.buffers.GpuBufferSlice
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.util.ARGB
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f

object DynamicTransforms {
    const val KEY = "DynamicTransforms"

    @JvmStatic
    fun builder(): Builder = Builder()

    class Builder {
        private var modelViewMatrix: Matrix4f? = null
        private var textureMatrix: Matrix4f? = null
        private var shaderColor = Vector4f(1.0F)
        private var modelOffset = Vector3f()

        fun withModelViewMatrix(matrix4f: Matrix4f): Builder {
            this.modelViewMatrix = matrix4f
            return this
        }

        fun withTextureMatrix(matrix4f: Matrix4f): Builder {
            this.textureMatrix = matrix4f
            return this
        }

        fun withShaderColor(vector4f: Vector4f): Builder {
            this.shaderColor = vector4f
            return this
        }

        fun withShaderColor(red: Float, green: Float, blue: Float, alpha: Float): Builder =
            this.withShaderColor(Vector4f(red, green, blue, alpha))

        fun withShaderColor(red: Float, green: Float, blue: Float): Builder =
            this.withShaderColor(red, green, blue, 1.0F)

        fun withShaderColor(color: Int): Builder = this.withShaderColor(
            ARGB.redFloat(color),
            ARGB.greenFloat(color),
            ARGB.blueFloat(color),
            ARGB.alphaFloat(color)
        )

        fun withModelOffset(vector3f: Vector3f): Builder {
            this.modelOffset = vector3f
            return this
        }

        fun build(): GpuBufferSlice =
            RenderSystem.getDynamicUniforms().writeTransform(
                this.modelViewMatrix ?: RenderSystem.getModelViewMatrixCopy(),
                this.shaderColor,
                this.modelOffset,
                this.textureMatrix ?: Matrix4f()
            )
    }
}