package me.mixces.animatium.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.Animatium;
import me.mixces.animatium.duck.PlayerPitchInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements PlayerPitchInterface {

    @Shadow
    public abstract float getHeadYaw();

    @Shadow
    public float bodyYaw;

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    protected ItemStack activeItemStack;

    @Shadow
    public float headYaw;

    @Shadow public abstract void remove(RemovalReason reason);

    @Unique
    public float animatium$prevCameraPitch;

    @Unique
    public float animatium$cameraPitch;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "isBlocking",
            at = @At("HEAD"),
            cancellable = true
    )
    private void animatium$removeShieldDelay(CallbackInfoReturnable<Boolean> cir) {
        if (Animatium.CONFIG.NO_BLOCKING_DELAY) {
            final UseAction action = activeItemStack.getItem().getUseAction(activeItemStack);
            cir.setReturnValue(isUsingItem() && action == UseAction.BLOCK);
        }
    }

    @Inject(
            method = "baseTick",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/entity/LivingEntity;hurtTime:I",
                    ordinal = 0
            )
    )
    private void animatium$setPrevCameraPitch(CallbackInfo ci) {
        animatium$prevCameraPitch = animatium$cameraPitch;
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;abs(F)F"
            )
    )
    private float animatium$bypassRotationBounds(float value, Operation<Float> original) {
        return Animatium.CONFIG.OLD_BODY_ROTATION ? 0.0F : original.call(value);
    }

    @Redirect(
            method = "turnHead",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;abs(F)F"
            )
    )
    public float animatium$oldBodyInterpolation(float g) {
        if (Animatium.CONFIG.OLD_BODY_ROTATION) {
            g = MathHelper.clamp(g, -75.0F, 75.0F);
            bodyYaw = getYaw() - g;
            if (g * g > 2500.0F) {
                bodyYaw += g * 0.2F;
            }
            return Float.MIN_VALUE;
        } else {
            return Math.abs(g);
        }
    }

    @Inject(
            method = "lerpHeadYaw",
            at = @At("TAIL")
    )
    public void animatium$removeHeadYawLerp(int headTrackingIncrements, double serverHeadYaw, CallbackInfo ci) {
        if (Animatium.CONFIG.NO_HEAD_SMOOTHNESS) {
            headYaw = (float) serverHeadYaw;
        }
    }

    @Override
    public float animatium$getPrevPlayerPitch() {
        return animatium$prevCameraPitch;
    }

    @Override
    public float animatium$getPlayerPitch() {
        return animatium$cameraPitch;
    }
}
