/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2025 lowercasebtw
 * Copyright (C) 2024-2025 mixces
 * Copyright (C) 2024-2025 Contributors to the project retain their copyright
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package btw.mixces.animatium.mixins.v0.entity;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.PlayerUtils;
import btw.mixces.animatium.util.states.UtilityRenderState;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(HumanoidModel.class)
public abstract class MixinHumanoidModel<T extends HumanoidRenderState> extends EntityModel<T> {
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
    public ModelPart body;

    @Shadow
    @Final
    public ModelPart rightLeg;

    @Shadow
    @Final
    public ModelPart leftLeg;

    protected MixinHumanoidModel(ModelPart modelPart, Function<ResourceLocation, RenderType> function) {
        super(modelPart, function);
    }

    @WrapOperation(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;isCrouching:Z"))
    private boolean animatium$sneakingFeetPosition(HumanoidRenderState instance, Operation<Boolean> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().sneakingFeetPosition && instance.isCrouching) {
            // Values sourced from older versions
            body.xRot = 0.5F;
            rightArm.xRot += 0.4F;
            leftArm.xRot += 0.4F;
            rightLeg.z = 4.0F;
            leftLeg.z = 4.0F;
            rightLeg.y = 9.0F;
            leftLeg.y = 9.0F;
            head.y = 1.0F;
            return false;
        } else {
            return original.call(instance);
        }
    }

    @WrapOperation(method = "setupAttackAnimation", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/model/geom/ModelPart;xRot:F", ordinal = 0))
    public void animatium$fixMirrorArmSwing$field(ModelPart instance, float value, Operation<Void> original, @Local HumanoidArm arm) {
        if (AnimatiumConfig.instance().fixMirrorArmSwing && arm == HumanoidArm.LEFT) {
            this.rightArm.xRot -= this.body.yRot;
        } else {
            original.call(instance, value);
        }
    }

    @ModifyExpressionValue(method = "setupAttackAnimation", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;sin(F)F", ordinal = 5))
    public float animatium$fixMirrorArmSwing$sin(float original, @Local HumanoidArm arm) {
        if (AnimatiumConfig.instance().fixMirrorArmSwing) {
            return PlayerUtils.getArmMultiplier(arm) * original;
        } else {
            return original;
        }
    }

    @WrapOperation(method = "poseBlockingArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F"))
    private float animatium$lockBlockingArmRotation(float value, float min, float max, Operation<Float> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().lockBlockingArmRotation) {
            return 0.0F;
        } else {
            return original.call(value, min, max);
        }
    }

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At(value = "CONSTANT", args = "floatValue=0.0", ordinal = 1))
    private void animatium$bowArmMovement(T humanoidRenderState, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().bowArmMovement) {
            HumanoidModel.ArmPose leftArmPose = humanoidRenderState.leftArmPose;
            HumanoidModel.ArmPose rightArmPose = humanoidRenderState.rightArmPose;
            final boolean isRightArmPose = rightArmPose == HumanoidModel.ArmPose.BOW_AND_ARROW;
            final boolean isLeftArmPose = leftArmPose == HumanoidModel.ArmPose.BOW_AND_ARROW;
            if (isRightArmPose || isLeftArmPose) {
                if (isRightArmPose) {
                    rightArm.zRot = 0.0F;
                    rightArm.yRot = -0.1F + head.yRot;
                    leftArm.yRot = 0.1F + head.yRot + 0.4F;
                }

                if (isLeftArmPose) {
                    leftArm.zRot = 0.0F;
                    rightArm.yRot = -0.1F + head.yRot - 0.4F;
                    leftArm.yRot = 0.1F + head.yRot;
                }

                rightArm.xRot = (float) (-Math.PI / 2) + head.xRot;
                leftArm.xRot = (float) (-Math.PI / 2) + head.xRot;
            }
        }
    }

    @WrapOperation(method = {"poseLeftArm", "poseRightArm"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HumanoidModel;poseBlockingArm(Lnet/minecraft/client/model/geom/ModelPart;Z)V"))
    private void animatium$oldSwordBlockArm(HumanoidModel<?> instance, ModelPart arm, boolean rightArm, Operation<Void> original, @Local(argsOnly = true) T humanoidRenderState) {
        original.call(instance, arm, rightArm);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().thirdPersonSwordBlockingPosition) {
            ItemStack stack = ((UtilityRenderState) humanoidRenderState).animatium$getItemHeldByArm(rightArm ? HumanoidArm.RIGHT : HumanoidArm.LEFT);
            if (!(stack.getItem() instanceof ShieldItem)) {
                arm.xRot = arm.xRot * 0.5F - ((float) Math.PI / 10F) * 2F;
                arm.yRot = 0;
            }
        }
    }

    @ModifyExpressionValue(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;isUsingItem:Z", ordinal = 0))
    private boolean animatium$fixOffHandUsingPose(boolean original) {
        return (!AnimatiumClient.isEnabled() || !AnimatiumConfig.instance().fixOffHandUsingPose) && original;
    }
}
