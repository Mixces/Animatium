package me.mixces.animatium.mixin;

import me.mixces.animatium.Animatium;
import me.mixces.animatium.duck.PlayerPitchInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
            method = "bobView",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void animatium$addOldPitchRotation(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (Animatium.CONFIG.OLD_VIEW_BOBBING) {
            final float prevPlayerPitch = ((PlayerPitchInterface) client.getCameraEntity()).animatium$getPrevPlayerPitch();
            final float playerPitch = ((PlayerPitchInterface) client.getCameraEntity()).animatium$getPlayerPitch();
            final float h = MathHelper.lerp(tickDelta, prevPlayerPitch, playerPitch);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(h));
        }
    }
}
