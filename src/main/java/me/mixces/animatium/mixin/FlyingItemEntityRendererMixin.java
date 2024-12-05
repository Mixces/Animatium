package me.mixces.animatium.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.state.FlyingItemEntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlyingItemEntityRenderer.class)
public abstract class FlyingItemEntityRendererMixin<T extends Entity & FlyingItemEntity> extends EntityRenderer<T, FlyingItemEntityRenderState> {

    protected FlyingItemEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

//    @WrapOperation(
//            method = "render",
//            at = @At(
//                    value = "FIELD",
//                    opcode = Opcodes.GETFIELD,
//                    target = "Lnet/minecraft/entity/Entity;age:I"
//            )
//    )
//    public int legacyAnimations$disableDelay(Entity instance, Operation<Integer> operation) {
//        int original = operation.call(instance);
//        if (LegacyAnimationsSettings.getInstance().oldProjectiles) {
//            original += 2;
//        }
//        return original;
//    }
//
//    @Inject(
//            method = "render",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;I)V"
//            )
//    )
//    public void legacyAnimations$shiftProjectile(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
//        if (LegacyAnimationsSettings.getInstance().oldProjectiles) {
//            matrices.translate((!dispatcher.gameOptions.getPerspective().isFrontView() ? 1 : -1) * 0.25F, 0.0F, 0.0F);
//        }
//    }

    //todo: shit is missing in left handed mode

//    @ModifyArg(
//            method = "render",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/util/math/RotationAxis;rotationDegrees(F)Lorg/joml/Quaternionf;"
//            ),
//            index = 0
//    )
//    private float legacyAnimations$rotateProjectileAccordingly(float deg)
//    {
//        if (LegacyAnimationsSettings.getInstance().oldProjectiles)
//        {
//            return deg;
//        }
//
//        final ClientPlayerEntity player = MinecraftClient.getInstance().player;
//
//        if (player == null)
//        {
//            return deg;
//        }
//
//        final boolean isLeftHand = HandUtils.INSTANCE.isLeftHand(MinecraftClient.getInstance().player, dispatcher);
//
//        if (dispatcher.gameOptions.getPerspective().isFrontView())
//        {
//            return isLeftHand ? deg : deg - 180.0F;
//        }
//        else
//        {
//            return isLeftHand ? deg - 180.0F : deg;
//        }
//    }

}
