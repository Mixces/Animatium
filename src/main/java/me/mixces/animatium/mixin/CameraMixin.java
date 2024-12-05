package me.mixces.animatium.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.Animatium;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private float cameraY;

    @Shadow
    private Entity focusedEntity;

    @Shadow
    protected abstract void moveBy(float f, float g, float h);

    @WrapOperation(
            method = "updateEyeHeight",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/render/Camera;cameraY:F"
            )
    )
    private void animatium$oldEyeInterp(Camera instance, float value, Operation<Void> original) {
        if (Animatium.CONFIG.OLD_SNEAKING_SPEED && focusedEntity.getStandingEyeHeight() < cameraY) {
            cameraY = focusedEntity.getStandingEyeHeight();
        } else {
            original.call(instance, value);
        }
    }

    @Inject(
            method = "update",
            at = @At("TAIL")
    )
    private void animatium$addParallax(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (Animatium.CONFIG.OLD_PARALLAX) {
            moveBy(-0.1f, 0.0f, 0.0f);
        }
    }
}
