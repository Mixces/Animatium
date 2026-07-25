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

package org.visuals.legacy.animatium.handler.rendering

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.renderpearl.api.textures.GpuTexture
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor

fun copyTextureToTexture(source: GpuTexture, destination: GpuTexture) =
    RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(
        source,
        destination,
        0,
        0, 0,
        0, 0,
        source.getWidth(0), source.getHeight(0)
    )

fun GuiGraphicsExtractor.fillVerticalLine(
    x: Int, y: Int,
    length: Int,
    color: Int
) = this.fill(x, y, x + 1, y + length, color)

fun GuiGraphicsExtractor.fillVerticalGradientLine(
    x: Int, y: Int,
    length: Int,
    startColor: Int,
    endColor: Int
) = this.fillGradient(x, y, x + 1, y + length, startColor, endColor)

fun GuiGraphicsExtractor.fillHorizontalLine(
    x: Int, y: Int,
    length: Int,
    color: Int
) = this.fill(x, y, x + length, y + 1, color)

fun GuiGraphicsExtractor.fillRectangle(
    x: Int, y: Int,
    width: Int, height: Int,
    color: Int
) = this.fill(x, y, x + width, y + height, color)

fun GuiGraphicsExtractor.fillFrameGradient(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    startColor: Int,
    endColor: Int
) {
    this.fillVerticalGradientLine(x, y, height - 2, startColor, endColor)
    this.fillVerticalGradientLine(x + width - 1, y, height - 2, startColor, endColor)
    this.fillHorizontalLine(x, y - 1, width, startColor)
    this.fillHorizontalLine(x, y - 1 + height - 1, width, endColor)
}

fun GuiGraphicsExtractor.drawScaledText(font: Font, text: String, x: Int, y: Int, scale: Float) {
    val stack = this.pose()
    stack.pushMatrix()
    val originX = stack.m20
    val originY = stack.m21
    stack.setTranslation(0.0F, 0.0F)
    stack.scale(scale, scale)
    stack.setTranslation(originX, originY)
    this.centeredText(font, text, (x / scale).toInt(), (y / scale).toInt(), 0xFFFFFFFF.toInt())
    stack.popMatrix()
}