package me.mixces.animatium.mixin;

import net.minecraft.client.input.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Input.class)
public class InputMixin {

    @Shadow
    public float movementForward;

    @Inject(
            method = "hasForwardMovement",
            at = @At("HEAD"),
            cancellable = true
    )
    private void animatium$oldMomentum(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(movementForward >= 0.8);
    }
}
