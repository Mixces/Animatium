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

package org.visuals.legacy.animatium.util.rendering;

import btw.lowercase.renderer.Renderer;
import btw.lowercase.renderer.buffer.DynamicTransforms;
import btw.lowercase.renderer.buffer.Geometry;
import btw.lowercase.renderer.vertex.VertexLayouts;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import org.joml.Vector4f;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.compatibility.IrisPipeline;
import org.visuals.legacy.animatium.util.compatibility.IrisUtil;

import java.util.function.Function;

public final class LegacySkyRenderer {
    private static final Function<Float, Geometry> VOID_BOX_GEOMETRY = offset -> Geometry.Indexed.compile(VertexLayouts.POSITIONED_COLOR_QUAD, 20, vertexConsumer -> {
        final int color = ARGB.opaque(0);

        // Left
        vertexConsumer.addVertex(-1.0F, offset, 1.0F).setColor(color);
        vertexConsumer.addVertex(1.0F, offset, 1.0F).setColor(color);
        vertexConsumer.addVertex(1.0F, -1.0F, 1.0F).setColor(color);
        vertexConsumer.addVertex(-1.0F, -1.0F, 1.0F).setColor(color);

        // Right
        vertexConsumer.addVertex(-1.0F, -1.0F, -1.0F).setColor(color);
        vertexConsumer.addVertex(1.0F, -1.0F, -1.0F).setColor(color);
        vertexConsumer.addVertex(1.0F, offset, -1.0F).setColor(color);
        vertexConsumer.addVertex(-1.0F, offset, -1.0F).setColor(color);

        // Back
        vertexConsumer.addVertex(1.0F, -1.0F, -1.0F).setColor(color);
        vertexConsumer.addVertex(1.0F, -1.0F, 1.0F).setColor(color);
        vertexConsumer.addVertex(1.0F, offset, 1.0F).setColor(color);
        vertexConsumer.addVertex(1.0F, offset, -1.0F).setColor(color);

        // Front
        vertexConsumer.addVertex(-1.0F, offset, -1.0F).setColor(color);
        vertexConsumer.addVertex(-1.0F, offset, 1.0F).setColor(color);
        vertexConsumer.addVertex(-1.0F, -1.0F, 1.0F).setColor(color);
        vertexConsumer.addVertex(-1.0F, -1.0F, -1.0F).setColor(color);

        // Bottom
        vertexConsumer.addVertex(-1.0F, -1.0F, -1.0F).setColor(color);
        vertexConsumer.addVertex(-1.0F, -1.0F, 1.0F).setColor(color);
        vertexConsumer.addVertex(1.0F, -1.0F, 1.0F).setColor(color);
        vertexConsumer.addVertex(1.0F, -1.0F, -1.0F).setColor(color);
    });

    public static final Geometry.Indexed TOP_GEOMETRY = Geometry.Indexed.compilePersistent(VertexLayouts.POSITIONED_QUAD, 676, vertexConsumer -> buildSkyHalf(vertexConsumer, 16.0F, false));
    public static final Geometry.Indexed BOTTOM_GEOMETRY = Geometry.Indexed.compilePersistent(VertexLayouts.POSITIONED_QUAD, 676, vertexConsumer -> buildSkyHalf(vertexConsumer, -16.0F, true));

    static {
        IrisUtil.assignPipeline(IrisPipeline.SKY_BASIC, AnimatiumPipelines.LEGACY_SKY, AnimatiumPipelines.LEGACY_SKY_PLANAR_FOG);
    }

    LegacySkyRenderer() {
    }

    public static RenderPipeline getLegacySkyPipeline(final boolean planar) {
        return planar ? AnimatiumPipelines.LEGACY_SKY_PLANAR_FOG : AnimatiumPipelines.LEGACY_SKY;
    }

    public static void renderBlueVoid(final int skyColor, final double depth) {
        try (final Renderer renderer = Renderer.of(() -> "Blue void sky disc")) {
            renderer.setPipeline(getLegacySkyPipeline(AnimatiumConfig.instance().other.planarSkyFog));
            renderer.setUniform(DynamicTransforms.KEY, DynamicTransforms.builder()
                    .withModelViewMatrix(RenderSystem.getModelViewMatrixCopy()
                            .translate(0.0F, AnimatiumConfig.instance().extras.dontMoveBlueVoid ? 12.0F : -((float) (depth - 16.0)), 0.0F))
                    .withShaderColor(new Vector4f(ARGB.redFloat(skyColor) * 0.2F + 0.04F, ARGB.greenFloat(skyColor) * 0.2F + 0.04F, ARGB.blueFloat(skyColor) * 0.6F + 0.1F, 1.0F))
                    .build());
            renderer.draw(BOTTOM_GEOMETRY);
        }
    }

    public static double getHorizonEyeHeight(final ClientLevel level, final float tickDelta) {
        return Minecraft.getInstance().player.getEyePosition(tickDelta).y - level.getLevelData().getHorizonHeight(level);
    }

    // TODO/NOTE: Figure out why its rendering differently than in 18w07a (last snapshot to have it)
    public static void renderVoidBox(final double depth) {
        try (final Renderer renderer = Renderer.of(() -> "Player Void Box")) {
            renderer.setPipeline(AnimatiumPipelines.VOID_BOX);
            renderer.draw(VOID_BOX_GEOMETRY.apply(-((float) (depth + 65.0))));
        }
    }

    private static void buildSkyHalf(final VertexConsumer vertexConsumer, final float y, final boolean bottom) {
        final int width = 64;
        for (int k = -384; k <= 384; k += width) {
            for (int l = -384; l <= 384; l += width) {
                float g = k;
                float h = k + width;
                if (bottom) {
                    // Swap them
                    float b = g;
                    g = h;
                    h = b;
                }

                vertexConsumer.addVertex(g, y, l);
                vertexConsumer.addVertex(h, y, l);
                vertexConsumer.addVertex(h, y, (l + width));
                vertexConsumer.addVertex(g, y, (l + width));
            }
        }
    }

    public static void close() {
        TOP_GEOMETRY.close();
        BOTTOM_GEOMETRY.close();
    }
}
