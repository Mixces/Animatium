package me.mixces.animatium.mixins.model.item;

import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.ItemUtils;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderState.LayerRenderState.class)
public abstract class MixinItemRenderLayerState {
    @Shadow
    abstract Transformation getTransformation();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/Transformation;apply(ZLnet/minecraft/client/util/math/MatrixStack;)V"))
    private void animatium$tiltItemPositionsRod(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().getTiltItemPositions()) {
            ItemStack stack = ItemUtils.getStack();
            if (stack != null && ItemUtils.isFishingRodItem(stack)) {
                ModelTransformationMode mode = ItemUtils.getTransformMode();
                if (mode != null) {
                    boolean isFirstPerson = mode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND || mode == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND;
                    if (isFirstPerson) {
                        Transformation transform = getTransformation();
                        float x = transform.translation.x();
                        float y = transform.translation.y();
                        float z = transform.translation.z();
                        matrices.translate(0.070625, 0.1, 0.020625);
                        matrices.translate(x, y, z);
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                        matrices.translate(-x, -y, -z);
                    }
                }
            }
        }
    }
}
