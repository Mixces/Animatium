package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(BipedEntityModel.class)
public abstract class MixinBipedEntityModel<T extends BipedEntityRenderState> extends EntityModel<T> {
    protected MixinBipedEntityModel(ModelPart modelPart, Function<Identifier, RenderLayer> function) {
        super(modelPart, function);
    }

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart head;

    @WrapOperation(method = "positionBlockingArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F"))
    private float animatium$lockBlockingArmRotation(float value, float min, float max, Operation<Float> original) {
        if (AnimatiumConfig.lockBlockingArmRotation) {
            return 0.0F;
        } else {
            return original.call(value, min, max);
        }
    }

    @Inject(method = "setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V", at = @At(value = "CONSTANT", args = "floatValue=0.0", ordinal = 1))
    private void animatium$fixBowArmMovement(T bipedEntityRenderState, CallbackInfo ci) {
        if (AnimatiumConfig.fixBowArmMovement) {
            final BipedEntityModel.ArmPose BOW_AND_ARROW = BipedEntityModel.ArmPose.BOW_AND_ARROW;
            BipedEntityModel.ArmPose armPose = bipedEntityRenderState.leftArmPose;
            BipedEntityModel.ArmPose armPose2 = bipedEntityRenderState.rightArmPose;
            final boolean isRightArmPose = armPose2 == BOW_AND_ARROW;
            final boolean isLeftArmPose = armPose == BOW_AND_ARROW;
            if (isRightArmPose || isLeftArmPose) {
                if (isRightArmPose) {
                    rightArm.roll = 0.0F;
                    rightArm.yaw = -0.1F + head.yaw;
                    leftArm.yaw = 0.1F + head.yaw + 0.4F;
                }

                if (isLeftArmPose) {
                    leftArm.roll = 0.0F;
                    rightArm.yaw = -0.1F + head.yaw - 0.4F;
                    leftArm.yaw = 0.1F + head.yaw;
                }

                rightArm.pitch = (float) (-Math.PI / 2) + head.pitch;
                leftArm.pitch = (float) (-Math.PI / 2) + head.pitch;
            }
        }
    }

    @WrapOperation(method = "animateArms", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;leftArm:Lnet/minecraft/client/model/ModelPart;", opcode = Opcodes.GETFIELD, ordinal = 3))
    public ModelPart animatium$fixMirrorArmSwing$field(BipedEntityModel<?> instance, Operation<ModelPart> original, @Local ModelPart modelPart) {
        if (AnimatiumConfig.fixMirrorArmSwing) {
            return modelPart;
        } else {
            return original.call(instance);
        }
    }

    @ModifyExpressionValue(method = "animateArms", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;sin(F)F", ordinal = 5))
    public float animatium$fixMirrorArmSwing$sin(float original, @Local Arm arm) {
        if (AnimatiumConfig.fixMirrorArmSwing) {
            return (arm == Arm.LEFT ? -1 : 1) * original;
        } else {
            return original;
        }
    }
}
