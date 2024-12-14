package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 1))
    private void animatium$preBowTransform(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (AnimatiumConfig.tiltItemPositions) {
            final Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            final int direction = arm == Arm.RIGHT ? 1 : -1;
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * -335));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * -50.0F));
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 1, shift = At.Shift.AFTER))
    private void animatium$postBowTransform(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (AnimatiumConfig.tiltItemPositions) {
            final Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            final int direction = arm == Arm.RIGHT ? 1 : -1;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 50.0F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * 335));
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 1))
    private void animatium$tiltItemPositions(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (AnimatiumConfig.tiltItemPositions && !(stack.getItem() instanceof BlockItem)) {
            final Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            final int direction = arm == Arm.RIGHT ? 1 : -1;
            final float scale = 0.7585F / 0.86F;
            matrices.scale(scale, scale, scale);
            matrices.translate(direction * -0.084F, 0.059F, 0.08F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 5.0F));
        }
    }

    @Inject(method = "resetEquipProgress", at = @At("HEAD"), cancellable = true)
    private void animatium$removeEquipAnimationOnItemUse(Hand hand, CallbackInfo ci) {
        ClientPlayerEntity player = this.client.player;
        if (AnimatiumConfig.removeEquipAnimationOnItemUse && player != null && player.isUsingItem()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFirstPersonItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", shift = At.Shift.AFTER),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/item/consume/UseAction;"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 6)
            ))
    private void animatium$applyItemSwingUsage(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, @Local Arm arm) {
        if (AnimatiumConfig.applyItemSwingUsage) {
            applySwingOffset(matrices, arm, swingProgress);
        }
    }
}
