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
 */

package btw.mixces.animatium.util;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.mixins.accessor.ClientLevelDataAccessor;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public final class RenderUtils {
    private static float LINE_WIDTH = -1.0F;
    private static Vector3f GLINT_COLOR = new Vector3f(0.5019607843137255F, 0.25098039215686274F, 0.8F);

    public static float getLineWidth(Float def) {
        if (LINE_WIDTH == -1.0F) {
            return def == null ? RenderSystem.getShaderLineWidth() : def;
        } else {
            return LINE_WIDTH;
        }
    }

    public static void setLineWidth(float width) {
        LINE_WIDTH = width;
    }

    public static Vector3f getGlintColor() {
        return GLINT_COLOR;
    }

    public static void setGlintColor(Vector3f vector3f) {
        GLINT_COLOR = vector3f;
    }

    public static void setGlintColor(float red, float green, float blue) {
        GLINT_COLOR = new Vector3f(red, green, blue);
    }

    public static double getLevelHorizonHeight(ClientLevel level) {
        if (AnimatiumConfig.instance().skyHorizonHeight) {
            if (((ClientLevelDataAccessor) level.getLevelData()).isFlatWorld()) {
                return 0.0D;
            } else {
                return 63.0D;
            }
        } else {
            return level.getLevelData().getHorizonHeight(level);
        }
    }

    public static void fillVerticalLine(GuiGraphics context, int x, int y, int length, int color) {
        context.fill(x, y, x + 1, y + length, color);
    }

    public static void fillVerticalGradientLine(GuiGraphics context, int x, int y, int length, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + length, startColor, endColor);
    }

    public static void fillHorizontalLine(GuiGraphics context, int x, int y, int length, int color) {
        context.fill(x, y, x + length, y + 1, color);
    }

    public static void fillRectangle(GuiGraphics context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public static void buildSkyHalf(VertexConsumer vertexConsumer, float y, boolean bottom) {
        final int width = 64;
        for (int k = -384; k <= 384; k += width) {
            for (int l = -384; l <= 384; l += width) {
                float g = (float) k;
                float h = (float) (k + 64);
                if (bottom) {
                    // Swap them
                    float b = g;
                    g = h;
                    h = b;
                }

                vertexConsumer.addVertex(g, y, (float) l);
                vertexConsumer.addVertex(h, y, (float) l);
                vertexConsumer.addVertex(h, y, (float) (l + width));
                vertexConsumer.addVertex(g, y, (float) (l + width));
            }
        }
    }

    public static void renderBlueVoidSky(Minecraft minecraft, ClientLevel level, GpuBuffer blueVoidSkyBuffer, int skyColor, double depth) {
        Vector3f skyColorVec = ARGB.vector3fFromRGB24(skyColor);
        RenderSystem.setShaderColor(skyColorVec.x * 0.2F + 0.04F, skyColorVec.y * 0.2F + 0.04F, skyColorVec.z * 0.6F + 0.1F, 1.0F);

        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.translate(0.0F, -((float) (depth - 16.0)), 0.0F);

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(minecraft.getMainRenderTarget().getColorTexture(), OptionalInt.empty(), minecraft.getMainRenderTarget().getDepthTexture(), OptionalDouble.empty())) {
            RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
            renderPass.setPipeline(AnimatiumClient.LEGACY_SKY_PIPELINE);
            renderPass.setVertexBuffer(0, blueVoidSkyBuffer);
            renderPass.setIndexBuffer(autoStorageIndexBuffer.getBuffer(6), autoStorageIndexBuffer.type());
            renderPass.drawIndexed(0, 1014);
        }

        modelViewStack.popMatrix();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
