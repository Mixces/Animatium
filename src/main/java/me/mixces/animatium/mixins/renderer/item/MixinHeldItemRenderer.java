package me.mixces.animatium.mixins.renderer.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.ItemUtils;
import me.mixces.animatium.util.MathUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.*;
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

    @WrapOperation(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 1))
    private void animatium$postBowTransform(MatrixStack instance, float x, float y, float z, Operation<Void> original, @Local(argsOnly = true) AbstractClientPlayerEntity player, @Local(argsOnly = true) Hand hand) {
        Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
        int direction = arm == Arm.RIGHT ? 1 : -1;
        if (AnimatiumConfig.getInstance().tiltItemPositions) {
            instance.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * -335));
            instance.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * -50.0F));
        }

        original.call(instance, x, y, z);
        if (AnimatiumConfig.getInstance().tiltItemPositions) {
            instance.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 50.0F));
            instance.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * 335));
        }
    }

    @WrapOperation(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private Item animatium$oldFirstPersonSwordBlock(ItemStack instance, Operation<Item> original, @Local(argsOnly = true) AbstractClientPlayerEntity player, @Local(argsOnly = true) Hand hand, @Local(argsOnly = true) MatrixStack matrices) {
        if (AnimatiumConfig.getInstance().tiltItemPositions && !(instance.getItem() instanceof ShieldItem)) {
            Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            int direction = arm == Arm.RIGHT ? 1 : -1;
            ItemUtils.Companion.applyLegacyFirstpersonTransforms(matrices, direction, () -> {
                matrices.translate(direction * -0.5F, 0.2F, 0.0F);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 30.0F));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80.0F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 60.0F));
            });
            return Items.SHIELD; // Cnacels the vanilla blocking code
        } else {
            return original.call(instance);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 1))
    private void animatium$tiltItemPositions(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().tiltItemPositions && !(stack.getItem() instanceof BlockItem) && !(stack.getItem() instanceof ShieldItem)) {
            Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            int direction = arm == Arm.RIGHT ? 1 : -1;
            float angle = MathUtils.Companion.toRadians(25);
            if (ItemUtils.Companion.isFishingRodItem(stack)) {
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 180.0F));
            }

            matrices.scale(0.6F, 0.6F, 0.6F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 275.0F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * 25.0F));
            matrices.translate(direction * (-0.2F * Math.sin(angle) + 0.4375F), -0.2F * Math.cos(angle) + 0.4375F, 0.03125F);

            matrices.scale(1 / 0.68F, 1 / 0.68F, 1 / 0.68F);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * -25.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 90.0F));
            matrices.translate(direction * -1.13 * 0.0625F, -3.2 * 0.0625F, -1.13 * 0.0625F);
        }
    }

    @Inject(method = "resetEquipProgress", at = @At("HEAD"), cancellable = true)
    private void animatium$removeEquipAnimationOnItemUse(Hand hand, CallbackInfo ci) {
        ClientPlayerEntity player = this.client.player;
        if (AnimatiumConfig.getInstance().removeEquipAnimationOnItemUse && player != null && player.isUsingItem()) {
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
        if (AnimatiumConfig.getInstance().applyItemSwingUsage) {
            applySwingOffset(matrices, arm, swingProgress);
        }
    }
}
