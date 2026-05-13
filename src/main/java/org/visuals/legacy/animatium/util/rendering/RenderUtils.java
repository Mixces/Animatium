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

import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Matrix3x2fStack;

public final class RenderUtils {
    public static final DepthStencilState NO_DEPTH_WRITE = new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false);

    RenderUtils() {
    }

    public static void copyTextureToTexture(final GpuTexture source, final GpuTexture destination) {
        RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(source, destination, 0, 0, 0, 0, 0, source.getWidth(0), source.getHeight(0));
    }

    public static void fillVerticalLine(final GuiGraphicsExtractor context, final int x, final int y, final int length, final int color) {
        context.fill(x, y, x + 1, y + length, color);
    }

    public static void fillVerticalGradientLine(final GuiGraphicsExtractor context, final int x, final int y, final int length, final int startColor, final int endColor) {
        context.fillGradient(x, y, x + 1, y + length, startColor, endColor);
    }

    public static void fillHorizontalLine(final GuiGraphicsExtractor context, final int x, final int y, final int length, final int color) {
        context.fill(x, y, x + length, y + 1, color);
    }

    public static void fillRectangle(final GuiGraphicsExtractor context, final int x, final int y, final int width, final int height, final int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public static void fillFrameGradient(final GuiGraphicsExtractor guiGraphics, final int x, final int y, final int width, final int height, final int startColor, final int endColor) {
        fillVerticalGradientLine(guiGraphics, x, y, height - 2, startColor, endColor);
        fillVerticalGradientLine(guiGraphics, x + width - 1, y, height - 2, startColor, endColor);
        fillHorizontalLine(guiGraphics, x, y - 1, width, startColor);
        fillHorizontalLine(guiGraphics, x, y - 1 + height - 1, width, endColor);
    }

    public static void drawScaledText(final GuiGraphicsExtractor guiGraphics, final Font font, final String text, final int x, final int y, final float scale) {
        final Matrix3x2fStack stack = guiGraphics.pose();
        stack.pushMatrix();
        final float originX = stack.m20;
        final float originY = stack.m21;
        stack.setTranslation(0.0F, 0.0F);
        stack.scale(scale, scale);
        stack.setTranslation(originX, originY);
        guiGraphics.centeredText(font, text, (int) (x / scale), (int) (y / scale), 0xFFFFFFFF);
        stack.popMatrix();
    }
}