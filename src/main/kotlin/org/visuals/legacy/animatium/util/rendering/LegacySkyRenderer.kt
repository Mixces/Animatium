package org.visuals.legacy.animatium.util.rendering

import btw.lowercase.renderer.Renderer
import btw.lowercase.renderer.buffer.DynamicTransforms
import btw.lowercase.renderer.buffer.Geometry
import btw.lowercase.renderer.vertex.VertexLayouts
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.util.ARGB
import org.joml.Vector4f
import org.visuals.legacy.animatium.config.AnimatiumConfig
import org.visuals.legacy.animatium.util.compatibility.IrisPipeline
import org.visuals.legacy.animatium.util.compatibility.IrisUtil

object LegacySkyRenderer {
    private val GET_VOID_BOX_GEOMETRY = { offset: Float ->
        Geometry.Indexed.compile(VertexLayouts.POSITIONED_COLOR_QUAD, 20) { vertexConsumer ->
            val color = ARGB.opaque(0)
            vertexConsumer.apply {
                // Left
                addVertex(-1.0F, offset, 1.0F).setColor(color)
                addVertex(1.0F, offset, 1.0F).setColor(color)
                addVertex(1.0F, -1.0F, 1.0F).setColor(color)
                addVertex(-1.0F, -1.0F, 1.0F).setColor(color)

                // Right
                addVertex(-1.0F, -1.0F, -1.0F).setColor(color)
                addVertex(1.0F, -1.0F, -1.0F).setColor(color)
                addVertex(1.0F, offset, -1.0F).setColor(color)
                addVertex(-1.0F, offset, -1.0F).setColor(color)

                // Back
                addVertex(1.0F, -1.0F, -1.0F).setColor(color)
                addVertex(1.0F, -1.0F, 1.0F).setColor(color)
                addVertex(1.0F, offset, 1.0F).setColor(color)
                addVertex(1.0F, offset, -1.0F).setColor(color)

                // Front
                addVertex(-1.0F, offset, -1.0F).setColor(color)
                addVertex(-1.0F, offset, 1.0F).setColor(color)
                addVertex(-1.0F, -1.0F, 1.0F).setColor(color)
                addVertex(-1.0F, -1.0F, -1.0F).setColor(color)

                // Bottom
                addVertex(-1.0F, -1.0F, -1.0F).setColor(color)
                addVertex(-1.0F, -1.0F, 1.0F).setColor(color)
                addVertex(1.0F, -1.0F, 1.0F).setColor(color)
                addVertex(1.0F, -1.0F, -1.0F).setColor(color)
            }
        }
    }

    @JvmField
    val TOP_GEOMETRY: Geometry.Indexed =
        Geometry.Indexed.compilePersistent(VertexLayouts.POSITIONED_QUAD, 676) { vertexConsumer ->
            buildSkyHalf(
                vertexConsumer,
                16.0F,
                false
            )
        }

    @JvmField
    val BOTTOM_GEOMETRY: Geometry.Indexed =
        Geometry.Indexed.compilePersistent(VertexLayouts.POSITIONED_QUAD, 676) { vertexConsumer ->
            buildSkyHalf(
                vertexConsumer,
                -16.0F,
                true
            )
        }

    init {
        IrisUtil.assignPipeline(
            IrisPipeline.SKY_BASIC,
            AnimatiumPipelines.LEGACY_SKY,
            AnimatiumPipelines.LEGACY_SKY_PLANAR_FOG
        )
    }

    @JvmStatic
    fun renderBlueVoid(skyColor: Int, depth: Double) {
        Renderer.of { "Blue void sky disc" }.use { renderer ->
            val pipeline = if (AnimatiumConfig.instance().other.planarSkyFog)
                AnimatiumPipelines.LEGACY_SKY_PLANAR_FOG
            else
                AnimatiumPipelines.LEGACY_SKY

            val matrix = RenderSystem.getModelViewMatrixCopy().translate(
                0.0F,
                if (AnimatiumConfig.instance().extras.dontMoveBlueVoid) 12.0F else -((depth - 16.0).toFloat()),
                0.0F
            )

            val color = Vector4f(
                ARGB.redFloat(skyColor) * 0.2F + 0.04F,
                ARGB.greenFloat(skyColor) * 0.2F + 0.04F,
                ARGB.blueFloat(skyColor) * 0.6F + 0.1F,
                1.0F
            )

            renderer.setPipeline(pipeline)
            renderer.setUniform(
                DynamicTransforms.KEY,
                DynamicTransforms.builder()
                    .withModelViewMatrix(matrix)
                    .withShaderColor(color)
                    .build()
            )
            renderer.draw(BOTTOM_GEOMETRY)
        }
    }

    @JvmStatic
    fun getHorizonEyeHeight(level: ClientLevel, tickDelta: Float): Double =
        Minecraft.getInstance().player!!.getEyePosition(tickDelta).y - level.getLevelData().getHorizonHeight(level)

    // TODO/NOTE: Figure out why its rendering differently than in 18w07a (last snapshot to have it)
    @JvmStatic
    fun renderVoidBox(depth: Double) {
        Renderer.of { "Player Void Box" }.use { renderer ->
            renderer.setPipeline(AnimatiumPipelines.VOID_BOX)
            renderer.draw(GET_VOID_BOX_GEOMETRY(-((depth + 65.0).toFloat())))
        }
    }

    private fun buildSkyHalf(vertexConsumer: VertexConsumer, y: Float, bottom: Boolean) {
        val width = 64
        for (k in -384..384 step width) {
            for (l in -384..384 step width) {
                var g = k
                var h = k + width
                if (bottom) {
                    // Swap them
                    val b = g
                    g = h
                    h = b
                }

                vertexConsumer.addVertex(g.toFloat(), y, l.toFloat())
                vertexConsumer.addVertex(h.toFloat(), y, l.toFloat())
                vertexConsumer.addVertex(h.toFloat(), y, (l + width).toFloat())
                vertexConsumer.addVertex(g.toFloat(), y, (l + width).toFloat())
            }
        }
    }

    @JvmStatic
    fun close() {
        TOP_GEOMETRY.close()
        BOTTOM_GEOMETRY.close()
    }
}