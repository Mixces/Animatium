package me.mixces.animatium.mixins.renderer.item;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.EntityUtils;
import me.mixces.animatium.util.ItemUtils;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Optional;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class MixinHeldItemFeatureRenderer<S extends ArmedEntityRenderState, M extends EntityModel<S> & ModelWithArms> extends FeatureRenderer<S, M> {
    public MixinHeldItemFeatureRenderer(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Inject(method = "renderItem", at = @At("HEAD"))
    private void animatium$setRef(S entityState, ItemRenderState itemState, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, @Share("stack") LocalRef<ItemStack> stackRef) {
        if (AnimatiumConfig.getInstance().tiltItemPositionsInThirdperson && !itemState.isEmpty()) {
            Optional<Entity> optionalLivingEntity = EntityUtils.getEntityByState(entityState);
            if (optionalLivingEntity.isPresent() && entityState instanceof ArmedEntityRenderState) {
                LivingEntity livingEntity = (LivingEntity) optionalLivingEntity.get();
                stackRef.set(livingEntity.getStackInArm(arm));
                /* why do i have to do this */
            }
        }
    }

    @ModifyArgs(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"))
    private void animatium$oldTransformTranslation(Args args, @Share("stack") LocalRef<ItemStack> stackRef) {
        if (AnimatiumConfig.getInstance().tiltItemPositionsInThirdperson && !ItemUtils.isItemBlacklisted(stackRef.get())) {
            args.setAll((float) args.get(0) * -1.0F, 0.4375F, (float) args.get(2) / 10 * -1.0F);
        }
    }

    @WrapWithCondition(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V"))
    private boolean animatium$removeTransformMultiply(MatrixStack instance, Quaternionf quaternion, @Share("stack") LocalRef<ItemStack> stackRef) {
        return !AnimatiumConfig.getInstance().tiltItemPositionsInThirdperson || ItemUtils.isItemBlacklisted(stackRef.get());
    }

    @Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V"))
    private void animatium$tiltItemPositionsThird(S entityState, ItemRenderState itemState, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().tiltItemPositionsInThirdperson) {
            Optional<Entity> optionalLivingEntity = EntityUtils.getEntityByState(entityState);
            if (optionalLivingEntity.isPresent() && entityState instanceof ArmedEntityRenderState) {
                int direction = arm == Arm.RIGHT ? 1 : -1;
                LivingEntity livingEntity = (LivingEntity) optionalLivingEntity.get();
                ItemStack stack = livingEntity.getStackInArm(arm);
                Item item = stack.getItem();
                if (!stack.isEmpty() && !ItemUtils.isItemBlacklisted(stack)) {
                    float scale;
                    if (ItemUtils.isBlock3d(stack)) {
                        scale = 0.375F;
                        matrices.translate(0.0F, 0.1875F, -0.3125F);
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(20.0F));
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 45.0F));
                        matrices.scale(-scale, -scale, scale);
                    } else if (item instanceof BowItem) {
                        scale = 0.625F;
                        matrices.translate(direction * 0.0F, 0.125F, 0.3125F);
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * -20.0F));
                        matrices.translate(direction * -0.0625F, 0.0F, 0.0F);
                        matrices.scale(scale, scale, scale);
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(100.0F));
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * -145.0F));
                    } else if (ItemUtils.isHandheldItem(stack)) {
                        scale = 0.625F;
                        if (ItemUtils.isFishingRodItem(stack)) {
                            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * 180.0F));
                            matrices.translate(0.0F, -0.125F, 0.0F);
                        }

                        if (livingEntity instanceof PlayerEntity && livingEntity.getItemUseTime() > 0 && livingEntity.isBlocking()) {
                            matrices.translate(direction * 0.05F, 0.0F, -0.1F);
                            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * -50.0F));
                            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-10.0F));
                            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * -60.0F));
                        }

                        matrices.translate(direction * -0.0625F, 0.1875F, 0.0F);
                        matrices.scale(scale, scale, scale);
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(100));
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * -145));
                    } else {
                        scale = 0.375F;
                        matrices.translate(direction * 0.25F, 0.1875F, -0.1875F);
                        matrices.scale(scale, scale, scale);
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * 60.0F));
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * 20.0F));
                    }

                    if (!ItemUtils.isBlock3d(stack)) {
                        matrices.translate(0.0F, -0.3F, 0.0F);
                        matrices.scale(1.5F, 1.5F, 1.5F);
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 50.0F));
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * 335.0F));
                        matrices.translate(direction * -0.9375F, -0.0625F, 0.0F);

                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 180.0F));
                        matrices.translate(direction * -0.5F, 0.5F, 0.03125F);
                    }

                    if (ItemUtils.isBlock3d(stack)) {
                        matrices.scale(1 / 0.375F, 1 / 0.375F, 1 / 0.375F);
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * -45.0F));
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-75.0F));
                        matrices.translate(0.0F, -2.5F * 0.0625F, 0.0F);
                    } else if (item instanceof BowItem) {
                        matrices.scale(1 / 0.9F, 1 / 0.9F, 1 / 0.9F);
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * 40.0F));
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * -260.0F));
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(80.0F));
                        matrices.translate(direction * 0.0625F, 2.0F * 0.0625F, -2.5F * 0.0625F);
                    } else if (ItemUtils.isHandheldItem(stack)) {
                        boolean isRod = ItemUtils.isFishingRodItem(stack);
                        matrices.scale(1 / 0.85F, 1 / 0.85F, 1 / 0.85F);
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction * -55.0F));
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 90.0F));
                        if (isRod) {
                            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * -180.0F));
                        }

                        matrices.translate(0.0F, -4.0F * 0.0625F, -0.5F * 0.0625F);
                        if (isRod) {
                            matrices.translate(0.0F, 0.0F, -2.0F * 0.0625F);
                        }
                    } else {
                        matrices.scale(1 / 0.55F, 1 / 0.55F, 1 / 0.55F);
                        matrices.translate(0.0F, -3.0F * 0.0625F, -1.0F * 0.0625F);
                    }
                }
            }
        }
    }
}
