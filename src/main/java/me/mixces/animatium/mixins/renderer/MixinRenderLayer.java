package me.mixces.animatium.mixins.renderer;

import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderLayer.class)
public abstract class MixinRenderLayer {
    // TODO/NOTE: uh i couldn't find a better way to do this as everything I tried didn't work :P
//    @Unique
//    private static final RenderLayer animatium$customArmorEntityGlint = RenderLayer.of(
//            "armor_entity_glint",
//            VertexFormats.POSITION_TEXTURE,
//            VertexFormat.DrawMode.QUADS,
//            1536,
//            RenderLayer.MultiPhaseParameters.builder()
//                    .program(RenderLayer.ARMOR_ENTITY_GLINT_PROGRAM)
//                    .texture(new RenderPhase.Texture(ItemRenderer.ITEM_ENCHANTMENT_GLINT, TriState.DEFAULT, false))
//                    .writeMaskState(RenderLayer.COLOR_MASK)
//                    .cull(RenderLayer.DISABLE_CULLING)
//                    .depthTest(RenderLayer.EQUAL_DEPTH_TEST)
//                    .transparency(RenderLayer.GLINT_TRANSPARENCY)
//                    .texturing(RenderLayer.ENTITY_GLINT_TEXTURING)
//                    .layering(RenderLayer.VIEW_OFFSET_Z_LAYERING)
//                    .build(RenderLayer.OutlineMode.NONE)
//    );
//
//    @Unique
//    private static final RenderLayer animatium$customEntityGlint = RenderLayer.of(
//            "entity_glint",
//            VertexFormats.POSITION_TEXTURE,
//            VertexFormat.DrawMode.QUADS,
//            1536,
//            RenderLayer.MultiPhaseParameters.builder()
//                    .program(RenderLayer.ENTITY_GLINT_PROGRAM)
//                    .texture(new RenderPhase.Texture(ItemRenderer.ITEM_ENCHANTMENT_GLINT, TriState.DEFAULT, false))
//                    .writeMaskState(RenderLayer.COLOR_MASK)
//                    .cull(RenderLayer.DISABLE_CULLING)
//                    .depthTest(RenderLayer.EQUAL_DEPTH_TEST)
//                    .transparency(RenderLayer.GLINT_TRANSPARENCY)
//                    .texturing(RenderLayer.ENTITY_GLINT_TEXTURING)
//                    .build(RenderLayer.OutlineMode.NONE)
//    );

    // TODO/NOTE: works fine until setting is changed
//    @Inject(method = "getArmorEntityGlint", at = @At("RETURN"), cancellable = true)
//    private static void animatium$forceItemGlintOnEntityArmor(CallbackInfoReturnable<RenderLayer> cir) {
//        if (AnimatiumConfig.forceItemGlintOnEntity) {
//            cir.setReturnValue(animatium$customArmorEntityGlint);
//        }
//    }

//    @Inject(method = "getEntityGlint", at = @At("RETURN"), cancellable = true)
//    private static void animatium$forceItemGlintOnEntity(CallbackInfoReturnable<RenderLayer> cir) {
//        if (AnimatiumConfig.forceItemGlintOnEntity) {
//            cir.setReturnValue(animatium$customEntityGlint);
//        }
//    }
}
