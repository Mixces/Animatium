package me.mixces.animatium.mixins.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.EntityUtils;
import me.mixces.animatium.util.MathUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ItemEntityRenderer.class)
public abstract class MixinItemEntityRenderer {
    @WrapOperation(method = "render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;getRotation(FF)F"))
    private float animatium$itemDropsFaceCamera(float age, float uniqueOffset, Operation<Float> original, @Local(argsOnly = true) ItemEntityRenderState itemEntityRenderState, @Local(argsOnly = true) MatrixStack matrixStack) {
        if (AnimatiumConfig.getInstance().getItemDropsFaceCamera()) {
            Optional<Entity> optionalEntity = EntityUtils.getEntityByState(itemEntityRenderState);
            if (optionalEntity.isPresent()) {
                ItemEntity entity = (ItemEntity) optionalEntity.get();
                ItemStack itemStack = entity.getStack();
                if (!(itemStack.getItem() instanceof BlockItem) && !itemEntityRenderState.itemRenderState.hasDepth()) {
                    Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
                    return MathUtils.toRadians(180F - camera.getYaw());
                }
            }
        }

        return original.call(age, uniqueOffset);
    }
}
