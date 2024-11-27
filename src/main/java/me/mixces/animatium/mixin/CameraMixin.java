package me.mixces.animatium.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private float cameraY;

    @Shadow
    private Entity focusedEntity;

    @WrapOperation(
            method = "updateEyeHeight",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/render/Camera;cameraY:F"
            )
    )
    private void animatium$oldSneak(Camera instance, float value, Operation<Void> original) {
        if (focusedEntity.getStandingEyeHeight() < cameraY) {
            cameraY = focusedEntity.getStandingEyeHeight();
        } else {
            original.call(instance, value);
        }
    }
}
