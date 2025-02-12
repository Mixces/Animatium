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

object LegacyGlintType {
    // TODO: Entity Glint/Sold Glint

    @JvmStatic
    val glintTranslucentLayerA = makeItemGlintTranslucentLayer(
        RenderStateShard.TexturingStateShard(
            "legacy_glint_texturing",
            { setupGlintTexturing(8.0F, -50.0F, false, 3000L) },
            RenderSystem::resetTextureMatrix
        )
    )

//    TODO/NOTE: Not required? With makes it wrong.
//    @JvmStatic
//    val glintTranslucentLayerB = makeItemGlintTranslucentLayer(
//        RenderStateShard.TexturingStateShard(
//            "legacy_glint_texturing",
//            { setupGlintTexturing(8.0F, 10.0F, true, 4873L) },
//            RenderSystem::resetTextureMatrix
//        )
//    )

    private fun makeItemGlintTranslucentLayer(texturingStateShard: RenderStateShard.TexturingStateShard): RenderType {
        return RenderType.create(
            "legacy_glint_translucent",
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
                .setOutputState(RenderType.ITEM_ENTITY_TARGET)
                .createCompositeState(false)
        )
    }

    private fun setupGlintTexturing(scale: Float, angle: Float, negative: Boolean, clampedTime: Long) {
        val matrix4f = Matrix4f()
        val g = (Util.getMillis() % clampedTime) / clampedTime.toFloat() / 8.0F
        matrix4f.scale(scale)
        matrix4f.translate(if (negative) -g else g, 0.0F, 0.0F)
        matrix4f.rotateZ(MathUtils.toRadians(angle))
        RenderSystem.setTextureMatrix(matrix4f)
    }
}