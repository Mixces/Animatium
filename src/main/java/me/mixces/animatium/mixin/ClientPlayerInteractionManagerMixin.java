package me.mixces.animatium.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow
    public abstract boolean isBreakingBlock();

    @Shadow
    private float currentBreakingProgress;

    @ModifyExpressionValue(
            method = "updateBlockBreakingProgress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isCurrentlyBreaking(Lnet/minecraft/util/math/BlockPos;)Z"
            )
    )
    public boolean animatium$fixBreakingBlockCheck(boolean original) {
        return original && isBreakingBlock();
    }

    @Inject(
            method = "getBlockBreakingProgress",
            at = @At("HEAD"),
            cancellable = true
    )
    private void animatium$oldMiningProgress(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue((int)(currentBreakingProgress * 10.0f) - 1);
    }
}
