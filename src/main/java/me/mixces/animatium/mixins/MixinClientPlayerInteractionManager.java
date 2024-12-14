package me.mixces.animatium.mixins;

import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager {
    @Shadow
    private float currentBreakingProgress;

    @Inject(method = "getBlockBreakingProgress", at = @At(value = "RETURN"), cancellable = true)
    private void animatium$oldBlockMiningProgress(CallbackInfoReturnable<Integer> cir) {
        if (AnimatiumConfig.oldBlockMiningProgress && currentBreakingProgress > 0.0F) {
            cir.setReturnValue((int) (this.currentBreakingProgress * 10.0f));
        }
    }
}
