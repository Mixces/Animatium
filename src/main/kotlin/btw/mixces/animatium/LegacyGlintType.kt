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

package btw.mixces.animatium

import btw.mixces.animatium.util.MathUtils
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.Util
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.util.TriState
import org.joml.Matrix4f

//    TODO/NOTE: Not required? With makes it wrong.
//    NOTE: This is layer 2
//    @JvmStatic
//    val glintTranslucentLayer2 = makeItemGlintLayer(
//        RenderStateShard.TexturingStateShard(
//            "legacy_glint_texturing",
//            { setupGlintTexturing(8.0F, 10.0F, true, 4873L) },
//            RenderSystem::resetTextureMatrix
//        ), true
//    )

object LegacyGlintType {
    // TODO: Entity Glint

    @JvmStatic
    val itemGlintLayer = makeItemGlintLayer(
        RenderStateShard.TexturingStateShard(
            "legacy_glint_texturing",
            { setupItemGlintTexturing(8.0F, -50.0F, false, 3000L) },
            RenderSystem::resetTextureMatrix
        ),
        false
    )

    @JvmStatic
    val itemGlintTranslucentLayer = makeItemGlintLayer(
        RenderStateShard.TexturingStateShard(
            "legacy_glint_texturing",
            { setupItemGlintTexturing(8.0F, -50.0F, false, 3000L) },
            RenderSystem::resetTextureMatrix
        ),
        true
    )

//    @JvmStatic
//    val entityGlintLayer = makeEntityGlintLayer(
//        RenderStateShard.TexturingStateShard(
//            "legacy_glint_texturing",
//            {  },
//            RenderSystem::resetTextureMatrix
//        ),
//        false
//    )
//
//    @JvmStatic
//    val entityArmorGlintLayer = makeEntityGlintLayer(
//        RenderStateShard.TexturingStateShard(
//            "legacy_glint_texturing",
//            {  },
//            RenderSystem::resetTextureMatrix
//        ),
//        true
//    )

    private fun makeItemGlintLayer(
        texturingStateShard: RenderStateShard.TexturingStateShard,
        translucent: Boolean,
    ): RenderType {
        return RenderType.create(
            "legacy_glint" + (if (translucent) "_translucent" else ""),
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.ShaderStateShard(AnimatiumClient.renderTypeLegacyGlintTranslucent))
                .setTextureState(
                    RenderStateShard.TextureStateShard(
                        ItemRenderer.ENCHANTED_GLINT_ITEM,
                        TriState.DEFAULT,
                        false
                    )
                )
                .setWriteMaskState(RenderType.COLOR_WRITE)
                .setCullState(RenderType.CULL)
                .setDepthTestState(RenderType.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderType.GLINT_TRANSPARENCY)
                .setTexturingState(texturingStateShard)
                .setOutputState(if (translucent) RenderType.ITEM_ENTITY_TARGET else RenderType.MAIN_TARGET)
                .createCompositeState(false)
        )
    }

    private fun makeEntityGlintLayer(
        texturingStateShard: RenderStateShard.TexturingStateShard,
        armor: Boolean,
    ): RenderType {
        return RenderType.create(
            "legacy_" + (if (armor) "armor_" else "") + "entity_glint",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                .setShaderState(if (armor) RenderType.RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER else RenderType.RENDERTYPE_ENTITY_GLINT_SHADER)
                .setTextureState(
                    RenderStateShard.TextureStateShard(
                        ItemRenderer.ENCHANTED_GLINT_ITEM, // <=1.19.3 uses item glint texture, we will to
                        TriState.DEFAULT,
                        false
                    )
                )
                .setWriteMaskState(RenderType.COLOR_WRITE)
                .setCullState(RenderType.CULL)
                .setDepthTestState(RenderType.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderType.GLINT_TRANSPARENCY)
                .setTexturingState(texturingStateShard)
                .setLayeringState(if (armor) RenderType.VIEW_OFFSET_Z_LAYERING else RenderType.NO_LAYERING)
                .createCompositeState(false)
        )
    }

    private fun setupItemGlintTexturing(scale: Float, angle: Float, negative: Boolean, clampedTime: Long) {
        val g = (Util.getMillis() % clampedTime) / clampedTime.toFloat() / 8.0F
        RenderSystem.setTextureMatrix(
            Matrix4f()
                .scale(scale)
                .translate(if (negative) -g else g, 0.0F, 0.0F)
                .rotateZ(MathUtils.toRadians(angle))
        )
    }
}