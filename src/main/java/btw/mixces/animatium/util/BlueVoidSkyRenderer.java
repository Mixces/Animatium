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

package btw.mixces.animatium.util;

import btw.mixces.animatium.config.AnimatiumConfig;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.util.ARGB;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public final class BlueVoidSkyRenderer {
    private static GpuBuffer vertexBuffer = null;

    private BlueVoidSkyRenderer() {
    }

    public static GpuBuffer getGpuBuffer() {
        if (vertexBuffer == null) {
            vertexBuffer = RenderUtils.initializeSky((builder) -> RenderUtils.buildSkyHalf(builder, -16.0F, true));
        }

        return vertexBuffer;
    }

    public static void renderBlueVoid(RenderTarget renderTarget, int skyColor, double depth) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.translate(0.0F, AnimatiumConfig.instance().extras.dontMoveBlueVoid ? 12.0F : -((float) (depth - 16.0)), 0.0F);

        Vector3f skyColorVec = ARGB.vector3fFromRGB24(skyColor);
        GpuBufferSlice transforms = DynamicTransformsBuilder.of()
                .withShaderColor(new Vector3f(skyColorVec.x * 0.2F + 0.04F, skyColorVec.y * 0.2F + 0.04F, skyColorVec.z * 0.6F + 0.1F))
                .build();

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Blue void disc", renderTarget.getColorTextureView(), OptionalInt.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty())) {
            RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
            renderPass.setPipeline(AnimatiumConfig.instance().other.planarSkyFog ? RenderUtils.LEGACY_SKY_PLANAR_FOG_PIPELINE : RenderUtils.LEGACY_SKY_PIPELINE);
            renderPass.setVertexBuffer(0, getGpuBuffer());
            renderPass.setIndexBuffer(autoStorageIndexBuffer.getBuffer(6), autoStorageIndexBuffer.type());
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", transforms);
            renderPass.drawIndexed(0, 0, 1014, 1);
        }

        modelViewStack.popMatrix();
    }
}
