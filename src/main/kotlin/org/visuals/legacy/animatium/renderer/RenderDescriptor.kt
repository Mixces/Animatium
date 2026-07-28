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

package org.visuals.legacy.animatium.renderer

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.renderpearl.api.commands.RenderPass
import com.mojang.renderpearl.api.commands.RenderPassDescriptor
import com.mojang.renderpearl.api.textures.GpuTextureView
import java.util.function.Supplier

data class RenderDescriptor(
    val name: Supplier<String>,
    val colorTexture: GpuTextureView,
    val depthTexture: GpuTextureView?,
    val area: Area
) {
    companion object {
        @JvmStatic
        fun builder(name: Supplier<String>): Builder = Builder(name)
    }

    fun createPass(): RenderPass {
        val descriptor = RenderPassDescriptor.builder(this.name)
        descriptor.withColorAttachment(this.colorTexture)
        if (this.depthTexture != null) {
            descriptor.withDepthAttachment(this.depthTexture)
        }

        descriptor.withRenderArea(this.area.vanilla())
        return RenderSystem.getDevice().createCommandEncoder().createRenderPass(descriptor.build())
    }

    data class Area(val x: Int, val y: Int, val width: Int, val height: Int) {
        constructor(width: Int, height: Int) : this(0, 0, width, height)
        constructor(renderTarget: RenderTarget) : this(0, 0, renderTarget.width, renderTarget.height)
        constructor(textureView: GpuTextureView) : this(0, 0, textureView.getWidth(0), textureView.getHeight(0))

        fun vanilla() = RenderPass.RenderArea(this.x, this.y, this.width, this.height)
    }

    class Builder(private val name: Supplier<String>) {
        private var colorTexture: GpuTextureView? = null
        private var depthTexture: GpuTextureView? = null
        private var area: Area? = null

        fun withRenderTarget(renderTarget: RenderTarget): Builder {
            this.colorTexture = renderTarget.getColorTextureView()
            this.depthTexture = if (renderTarget.hasDepth()) renderTarget.getDepthTextureView() else null
            this.area = Area(0, 0, renderTarget.width, renderTarget.height)
            return this
        }

        fun withRenderTarget(renderTarget: RenderTarget): Builder = this.withRenderTarget(renderTarget, true)

        fun withColorTexture(colorTexture: GpuTextureView): Builder {
            this.colorTexture = colorTexture
            return this
        }

        fun withDepthTexture(depthTexture: GpuTextureView?): Builder {
            this.depthTexture = depthTexture
            return this
        }

        fun withArea(area: Area): Builder {
            this.area = area
            return this
        }

        fun build(): RenderDescriptor {
            val colorTexture = this.colorTexture ?: throw RuntimeException("Color texture target must not be null!")
            val area = this.area ?: Area(0, 0, colorTexture.getWidth(0), colorTexture.getHeight(0))
            return RenderDescriptor(this.name, colorTexture, this.depthTexture, area)
        }
    }
}