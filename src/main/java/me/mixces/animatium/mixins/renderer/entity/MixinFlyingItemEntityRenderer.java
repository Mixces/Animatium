package me.mixces.animatium.mixins.renderer.entity;

import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.state.FlyingItemEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlyingItemEntityRenderer.class)
public abstract class MixinFlyingItemEntityRenderer<T extends Entity & FlyingItemEntity> extends EntityRenderer<T, FlyingItemEntityRenderState> {
    protected MixinFlyingItemEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/FlyingItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V"))
    private void animatium$oldProjectilePosition(FlyingItemEntityRenderState flyingItemEntityRenderState, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().getOldProjectilePosition()) {
            assert MinecraftClient.getInstance().player != null;
            int direction = PlayerUtils.getHandMultiplier(MinecraftClient.getInstance().player);
            matrices.translate(direction * 0.25F, 0.0F, 0.25F);
        }
    }
}
