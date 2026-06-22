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

package org.visuals.legacy.animatium.util.rendering.lighting.lightmap

import btw.lowercase.renderer.Renderer
import btw.lowercase.renderer.buffer.Geometry
import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.buffers.Std140Builder
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import com.mojang.blaze3d.textures.GpuTextureView
import net.minecraft.client.renderer.MappableRingBuffer
import net.minecraft.util.profiling.Profiler
import org.visuals.legacy.animatium.util.rendering.AnimatiumPipelines

class LegacyLightmapRenderer : AutoCloseable {
    companion object {
        private val BASE_GEOMETRY = Geometry.Basic(0, 3);
        private val LIGHTMAP_UBO_SIZE = Std140SizeCalculator()
            .putFloat() // SkyDarken
            .putFloat() // SkyDarkness
            .putFloat() // BlockLightRed
            .putFloat() // NightVisionScale
            .putFloat() // Gamma
            .putInt() // UseBrightLightmap
            .get()
    }

    private val ubo = MappableRingBuffer(
        { "Legacy Lightmap UBO" },
        GpuBuffer.USAGE_UNIFORM or GpuBuffer.USAGE_MAP_WRITE,
        LIGHTMAP_UBO_SIZE
    )

    fun render(state: LegacyLightmapState, textureView: GpuTextureView) {
        if (state.needsUpdate) {
            val profiler = Profiler.get()
            profiler.push("lightmap")

            this.ubo.currentBuffer().map(false, true).use { mappedView ->
                Std140Builder.intoBuffer(mappedView.data())
                    .putFloat(state.skyDarken)
                    .putFloat(state.skyDarkness)
                    .putFloat(state.blockLightRed)
                    .putFloat(state.nightVisionScale)
                    .putFloat(state.gamma)
                    .putInt(if (state.useBrightLightmap) 1 else 0)
            }

            Renderer.of({ "Legacy Lightmap Update" }, textureView).use { renderer ->
                renderer.setPipeline(AnimatiumPipelines.LEGACY_LIGHTMAP)
                renderer.setUniform("LightmapInfo", this.ubo.currentBuffer())
                renderer.draw(BASE_GEOMETRY)
            }

            this.ubo.rotate()
            profiler.pop()
        }
    }

    override fun close() = this.ubo.close()
}