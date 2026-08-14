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

package org.visuals.legacy.animatium.renderer

import com.mojang.blaze3d.ProjectionType
import com.mojang.blaze3d.buffers.GpuBufferSlice
import com.mojang.blaze3d.opengl.GlStateManager
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.pipeline.RenderPipeline.UniformDescription
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.GpuSampler
import com.mojang.blaze3d.textures.GpuTextureView
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.PerspectiveProjectionMatrixBuffer
import net.minecraft.resources.Identifier
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11
import org.visuals.legacy.animatium.renderer.buffer.Geometry
import org.visuals.legacy.animatium.renderer.texture.TextureAndSampler
import org.visuals.legacy.animatium.util.compatibility.IrisPipeline
import org.visuals.legacy.animatium.util.compatibility.IrisUtil
import java.util.function.Supplier

class Renderer : AutoCloseable {
    companion object {
        @JvmStatic
        fun of(descriptor: RenderDescriptor) = Renderer(descriptor)

        @JvmStatic
        fun of(
            label: Supplier<String>,
            colorTextureView: GpuTextureView,
            depthTextureView: GpuTextureView?,
            area: RenderDescriptor.Area
        ) = of(
            RenderDescriptor.builder(label)
                .withColorTexture(colorTextureView)
                .withDepthTexture(depthTextureView)
                .withArea(area)
                .build()
        )

        @JvmStatic
        fun of(label: Supplier<String>, colorTextureView: GpuTextureView, depthTextureView: GpuTextureView?) =
            of(label, colorTextureView, depthTextureView, RenderDescriptor.Area(colorTextureView))

        @JvmStatic
        fun of(label: Supplier<String>, colorTextureView: GpuTextureView) = of(label, colorTextureView, null)

        @JvmStatic
        fun of(label: Supplier<String>, renderTarget: RenderTarget, area: RenderDescriptor.Area) = of(
            RenderDescriptor.builder(label)
                .withRenderTarget(renderTarget, false)
                .withArea(area)
                .build()
        )

        @JvmStatic
        fun of(label: Supplier<String>, renderTarget: RenderTarget) =
            of(label, renderTarget, RenderDescriptor.Area(renderTarget))

        @JvmStatic
        fun of(label: Supplier<String>): Renderer = of(label, Minecraft.getInstance().mainRenderTarget)
    }

    // Data
    private val textures = Object2ObjectOpenHashMap<String, TextureAndSampler>()
    private val uniforms = Object2ObjectOpenHashMap<String, GpuBufferSlice>()

    private val name: Supplier<String>
    private val descriptor: RenderDescriptor
    private var pipeline: RenderPipeline? = null
    private var projectionMatrix: Matrix4f? = null

    // Internal
    private var projectionMatrixBuffer: PerspectiveProjectionMatrixBuffer? = null

    private constructor(descriptor: RenderDescriptor) {
        this.name = descriptor.name
        this.descriptor = descriptor
    }

    fun setPipeline(pipeline: RenderPipeline, irisPipeline: IrisPipeline): Renderer {
        this.pipeline = pipeline
        IrisUtil.assignPipeline(pipeline, irisPipeline)
        return this
    }

    fun setPipeline(pipeline: RenderPipeline): Renderer {
        val samplers = pipeline.samplers
        return this.setPipeline(
            pipeline,
            if (samplers.contains("Sampler0")) {
                IrisPipeline.TEXTURED
            } else {
                IrisPipeline.BASIC
            }
        )
    }

    fun setTexture(name: String, textureAndSampler: TextureAndSampler): Renderer {
        this.textures[name] = textureAndSampler
        return this
    }

    fun setTexture(name: String, textureView: GpuTextureView, sampler: GpuSampler) =
        this.setTexture(name, TextureAndSampler(textureView, sampler))

    fun setTexture(name: String, location: Identifier) =
        this.setTexture(name, TextureAndSampler.get(location))

    fun setUniform(name: String, data: GpuBufferSlice): Renderer {
        this.uniforms[name] = data
        return this
    }

    fun setProjectionMatrix(matrix4f: Matrix4f): Renderer {
        if (this.projectionMatrixBuffer == null) {
            this.projectionMatrixBuffer = PerspectiveProjectionMatrixBuffer("Immediate Projection Buffer for " + this.name)
        }

        this.projectionMatrix = matrix4f
        return this
    }

    fun draw(geometry: Geometry) {
        val pipeline = this.pipeline ?: throw RuntimeException("Cannot draw without a pipeline bound!")
        if (geometry.isClosed()) {
            throw RuntimeException("Cannot draw! The geometry provided has already been closed!")
        } else {
            if (this.projectionMatrixBuffer != null && this.projectionMatrix != null) {
                val properties = this.projectionMatrix!!.properties()
                val projectionType = if ((properties and Matrix4f.PROPERTY_PERSPECTIVE.toInt()) != 0) {
                    ProjectionType.PERSPECTIVE
                } else {
                    ProjectionType.ORTHOGRAPHIC
                }

                RenderSystem.backupProjectionMatrix()
                RenderSystem.setProjectionMatrix(
                    this.projectionMatrixBuffer!!.getBuffer(this.projectionMatrix!!),
                    projectionType
                )
            }

            val dynamicTransforms = this.uniforms.getOrDefault(DynamicTransforms.KEY, DynamicTransforms.current())
            val autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(pipeline.vertexFormatMode)
            this.descriptor.createPass().use { pass ->
                pass.setPipeline(pipeline)

                var lastX: Int
                var lastY: Int
                var lastWidth: Int
                var lastHeight: Int
                this.descriptor.area.run {
                    val lastViewPort = IntArray(4)
                    GL11.glGetIntegerv(GL11.GL_VIEWPORT, lastViewPort)
                    lastX = lastViewPort[0]
                    lastY = lastViewPort[1]
                    lastWidth = lastViewPort[2]
                    lastHeight = lastViewPort[3]
                    GlStateManager._viewport(x, y, width, height)
                }

                val descriptions = pipeline.uniforms
                    .stream()
                    .map(UniformDescription::name)
                    .toList()

                RenderSystem.bindDefaultUniforms(pass)
                pass.setUniform(DynamicTransforms.KEY, dynamicTransforms)
                for (entry in this.uniforms) {
                    val name = entry.key
                    if (DynamicTransforms.KEY == name) {
                        continue // Special Handling Above
                    }

                    if (descriptions.contains(name)) {
                        pass.setUniform(entry.key, entry.value)
                    }
                }

                val samplers = pipeline.samplers
                for (entry in this.textures) {
                    val name = entry.key
                    if (samplers.contains(name)) {
                        pass.bindTexture(name, entry.value.textureView, entry.value.sampler)
                    }
                }

                geometry.bind(pass, autoStorageIndexBuffer)
                geometry.draw(pass)
                GlStateManager._viewport(lastX, lastY, lastWidth, lastHeight)
            }

            if (!geometry.persistent()) {
                geometry.close()
            }

            if (this.projectionMatrixBuffer != null && this.projectionMatrix != null) {
                RenderSystem.restoreProjectionMatrix()
            }
        }
    }

    fun drawGui(geometry: Geometry) {
        val window = Minecraft.getInstance().window
        this.setProjectionMatrix(
            Matrix4f().setOrtho(
                0.0F,
                window.width.toFloat() / window.guiScale.toFloat(),
                window.height.toFloat() / window.guiScale.toFloat(),
                0.0F,
                1000.0F,
                11000.0F
            )
        )
        this.setUniform(
            DynamicTransforms.KEY,
            DynamicTransforms.builder()
                .withModelViewMatrix(Matrix4f().setTranslation(0.0F, 0.0F, -11000.0F))
                .build()
        )
        this.draw(geometry)
    }

    override fun close() {
        this.textures.clear()
        this.uniforms.clear()
        if (this.projectionMatrixBuffer != null) {
            this.projectionMatrixBuffer?.close()
            this.projectionMatrixBuffer = null
        }
    }
}