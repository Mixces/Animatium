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

package btw.mixces.animatium.util

import btw.mixces.animatium.config.AnimatiumConfig
import btw.mixces.animatium.mixins.accessor.ClientLevelDataAccessor
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.multiplayer.ClientLevel

object RenderUtils {
    private var lineWidth: Float = -1.0F

    @JvmStatic
    fun getLineWidth(): Float {
        return if (lineWidth == -1.0F) RenderSystem.getShaderLineWidth() else lineWidth
    }

    @JvmStatic
    fun getLineWidth(default: Float): Float {
        return if (lineWidth == -1.0F) default else lineWidth
    }

    @JvmStatic
    fun setLineWidth(width: Float) {
        lineWidth = width
    }

    @JvmStatic
    fun getLevelHorizonHeight(level: ClientLevel): Double {
        return if (AnimatiumConfig.instance().oldSkyHorizonHeight) {
            if ((level.getLevelData() as ClientLevelDataAccessor).isFlatWorld())
                0.0
            else
                63.0
        } else {
            level.levelData.getHorizonHeight(level)
        }
    }

    @JvmStatic
    fun fillVerticalLine(context: GuiGraphics, startX: Int, startY: Int, endX: Int, endY: Int, color: Int) {
        context.fill(startX, startY, startX + 1, startY + endX, endY, color);
    }

    @JvmStatic
    fun fillVerticalGradientLine(context: GuiGraphics, i: Int, j: Int, k: Int, l: Int, startColor: Int, endColor: Int) {
        context.fillGradient(i, j, i + 1, j + k, l, startColor, endColor);
    }

    @JvmStatic
    fun fillHorizontalLine(context: GuiGraphics, startX: Int, startY: Int, endX: Int, endY: Int, color: Int) {
        context.fill(startX, startY, startX + endX, startY + 1, endY, color);
    }

    @JvmStatic
    fun fillRectangle(context: GuiGraphics, startX: Int, startY: Int, endX: Int, endY: Int, padding: Int, color: Int) {
        context.fill(startX, startY, startX + endX, startY + endY, padding, color);
    }

    @JvmStatic
    fun buildSkyHalf(vertexConsumer: VertexConsumer, y: Float, bottom: Boolean) {
        val width = 64
        for (k in -384..384 step width) {
            for (l in -384..384 step width) {
                var g = k.toFloat()
                var h = (k + 64).toFloat()
                if (bottom) {
                    // Swap them
                    var b = g
                    g = h
                    h = b
                }

                vertexConsumer.addVertex(g, y, l.toFloat())
                vertexConsumer.addVertex(h, y, l.toFloat())
                vertexConsumer.addVertex(h, y, (l + width).toFloat())
                vertexConsumer.addVertex(g, y, (l + width).toFloat())
            }
        }
    }
}