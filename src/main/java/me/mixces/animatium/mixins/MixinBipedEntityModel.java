package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(BipedEntityModel.class)
public abstract class MixinBipedEntityModel<T extends BipedEntityRenderState> extends EntityModel<T> {
    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart head;

    protected MixinBipedEntityModel(ModelPart modelPart, Function<Identifier, RenderLayer> function) {
        super(modelPart, function);
    }

    @WrapOperation(method = "positionBlockingArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F"))
    private float lockBlockingArmRotation(float value, float min, float max, Operation<Float> original) {
        if (AnimatiumConfig.lockBlockingArmRotation) {
            return 0.0F;
        } else {
            return original.call(value, min, max);
        }
    }

    @Inject(method = "setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V", at = @At(value = "CONSTANT", args = "floatValue=0.0", ordinal = 1))
    private void fixIncorrectArmPlacement(T bipedEntityRenderState, CallbackInfo ci) {
        final BipedEntityModel.ArmPose BOW_AND_ARROW = BipedEntityModel.ArmPose.BOW_AND_ARROW;
        BipedEntityModel.ArmPose armPose = bipedEntityRenderState.leftArmPose;
        BipedEntityModel.ArmPose armPose2 = bipedEntityRenderState.rightArmPose;

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
}
