package me.mixces.animatium.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.animatium.Animatium;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.Arm;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends BipedEntityRenderState> extends EntityModel<T> {

    @Shadow
    protected abstract BipedEntityModel.ArmPose getArmPose(T state, Arm arm);

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart head;

    @Shadow
    @Final
    public ModelPart rightLeg;

    @Shadow
    @Final
    public ModelPart leftLeg;

    @Shadow
    @Final
    public ModelPart body;

    protected BipedEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(
            method = "setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V",
            at = @At(
                    value = "CONSTANT",
                    args = "floatValue=0.0",
                    ordinal = 1
            )
    )
    private void animatium$fixIncorrectArmPlacement2(T bipedEntityRenderState, CallbackInfo ci) {
        final BipedEntityModel.ArmPose BOW_AND_ARROW = BipedEntityModel.ArmPose.BOW_AND_ARROW;
        BipedEntityModel.ArmPose armPose = getArmPose(bipedEntityRenderState, Arm.LEFT);
        BipedEntityModel.ArmPose armPose2 = getArmPose(bipedEntityRenderState, Arm.RIGHT);

        final boolean isRightArmPose = armPose2 == BOW_AND_ARROW;
        final boolean isLeftArmPose = armPose == BOW_AND_ARROW;
        if (!isRightArmPose && !isLeftArmPose) {
            return;
        }
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

    //todo: simplify this
    @Inject(
            method = "setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;isInSneakingPose:Z"
            )
    )
    private void animatium$oldSneakPosition(T bipedEntityRenderState, CallbackInfo ci) {
        if (Animatium.CONFIG.THIRD_PERSON_SNEAKING) {
            if (bipedEntityRenderState.isInSneakingPose) {
                body.pitch = 0.5F;
                rightArm.pitch += 0.4F;
                leftArm.pitch += 0.4F;
                rightLeg.pivotZ = 4.0F;
                leftLeg.pivotZ = 4.0F;
                rightLeg.pivotY = 9.0F;
                leftLeg.pivotY = 9.0F;
                head.pivotY = 1.0F;
            } else {
                body.pitch = 0.0F;
                rightLeg.pivotZ = 0.1F;
                leftLeg.pivotZ = 0.1F;
                rightLeg.pivotY = 12.0F;
                leftLeg.pivotY = 12.0F;
                head.pivotY = 0.0F;
            }
        }
    }

    @WrapOperation(
            method = "setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;isInSneakingPose:Z"
            )
    )
    private boolean animatium$disableSneakTranslations(BipedEntityRenderState instance, Operation<Boolean> original) {
        return !Animatium.CONFIG.OLD_EYE_HEIGHT;
    }

    @WrapOperation(
            method = "animateArms",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;leftArm:Lnet/minecraft/client/model/ModelPart;",
                    ordinal = 3
            )
    )
    public ModelPart animatium$mirrorSwing(BipedEntityModel<T> instance, Operation<ModelPart> original, @Local ModelPart modelPart) {
        return modelPart;
    }

    @ModifyExpressionValue(
            method = "animateArms",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;sin(F)F",
                    ordinal = 5
            )
    )
    public float animatium$mirrorSwing2(float original, @Local Arm arm) {
        return (arm == Arm.LEFT ? -1 : 1) * original;
    }
}