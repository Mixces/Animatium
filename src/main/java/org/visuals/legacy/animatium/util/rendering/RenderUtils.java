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

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix3x2fStack;

@UtilityClass
public class RenderUtils {
    @Getter
    private final LineState lineState = new LineState();

    public void fillVerticalLine(GuiGraphics context, int x, int y, int length, int color) {
        context.fill(x, y, x + 1, y + length, color);
    }

    public void fillVerticalGradientLine(GuiGraphics context, int x, int y, int length, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + length, startColor, endColor);
    }

    public void fillHorizontalLine(GuiGraphics context, int x, int y, int length, int color) {
        context.fill(x, y, x + length, y + 1, color);
    }

    public void fillRectangle(GuiGraphics context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public void fillFrameGradient(GuiGraphics guiGraphics, int x, int y, int width, int height, int startColor, int endColor) {
        fillVerticalGradientLine(guiGraphics, x, y, height - 2, startColor, endColor);
        fillVerticalGradientLine(guiGraphics, x + width - 1, y, height - 2, startColor, endColor);
        fillHorizontalLine(guiGraphics, x, y - 1, width, startColor);
        fillHorizontalLine(guiGraphics, x, y - 1 + height - 1, width, endColor);
    }

    public void drawScaledText(GuiGraphics guiGraphics, Font font, String text, int x, int y, float scale) {
        final Matrix3x2fStack stack = guiGraphics.pose();
        stack.pushMatrix();
        final float originX = stack.m20;
        final float originY = stack.m21;
        stack.setTranslation(0.0F, 0.0F);
        stack.scale(scale, scale);
        stack.setTranslation(originX, originY);
        guiGraphics.drawCenteredString(font, text, (int) (x / scale), (int) (y / scale), 0xFFFFFFFF);
        stack.popMatrix();
    }
}