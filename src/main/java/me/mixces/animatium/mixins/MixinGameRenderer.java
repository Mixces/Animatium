package me.mixces.animatium.mixins;

import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.ViewBobbingStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "bobView", at = @At("TAIL"))
    private void fixVerticalBobbingTilt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (AnimatiumConfig.fixVerticalBobbingTilt && this.client.getCameraEntity() instanceof PlayerEntity playerEntity) {
            ViewBobbingStorage bobbingAccessor = (ViewBobbingStorage) playerEntity;
            float j = MathHelper.lerp(tickDelta, bobbingAccessor.animatium$getPreviousBobbingTilt(), bobbingAccessor.animatium$getBobbingTilt());
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(j));
        }
    }
}
