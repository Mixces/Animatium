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

package btw.mixces.animatium.mixins.renderer.entity;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.mixins.accessor.CameraAccessor;
import btw.mixces.animatium.util.FishingRodVersion;
import btw.mixces.animatium.util.PlayerUtils;
import btw.mixces.animatium.util.RenderUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.entity.state.FishingHookRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FishingHookRenderer.class)
public abstract class MixinFishingHookRenderer extends EntityRenderer<FishingHook, FishingHookRenderState> {
    protected MixinFishingHookRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @ModifyArgs(method = "getPlayerHandPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera$NearPlane;getPointOnPlane(FF)Lnet/minecraft/world/phys/Vec3;"))
    private void animatium$moveCastLineY(Args args) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().fishingRodVersion != FishingRodVersion.LATEST) {
            FishingRodVersion version = AnimatiumConfig.instance().fishingRodVersion;
            if (version == FishingRodVersion.V1_8) {
                animatium$modifyPlanarScale(args, 0);
            }

            if (version.ordinal() <= FishingRodVersion.V1_8.ordinal()) {
                animatium$modifyPlanarScale(args, 1);
            }
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/FishingHookRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;last()Lcom/mojang/blaze3d/vertex/PoseStack$Pose;", ordinal = 1, shift = At.Shift.AFTER))
    private void animatium$fishingRodLineThickness(FishingHookRenderState fishingHookRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled()) {
            // TODO/NOTE: Seems to be ok to set it like this and not have to set -1.0F after?
            // TODO/NOTE: Might be able to do that for MixinLevelRenderer
            if (AnimatiumConfig.instance().thinFishingRodLineThickness) {
                RenderUtils.setLineWidth(1.0F);
            } else if (AnimatiumConfig.instance().fishingRodLineThickness) {
                RenderUtils.setLineWidth(2.0F);
            }
        }
    }

    @WrapOperation(method = "getPlayerHandPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getEyePosition(F)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 animatium$fishingRodLineInterpolation(Player instance, float v, Operation<Vec3> original) {
        Vec3 originalPos = original.call(instance, v);
        if (AnimatiumClient.isEnabled()) {
            CameraAccessor cameraAccessor = (CameraAccessor) entityRenderDispatcher.camera;
            float eyeHeight;
            if (AnimatiumConfig.instance().fishingRodLineInterpolation) {
                eyeHeight = Mth.lerp(v, cameraAccessor.getEyeHeightOld(), cameraAccessor.getEyeHeight());
            } else if (AnimatiumConfig.instance().fakeOldSneakEyeHeight) {
                // Non-lerped eyeheight trick
                eyeHeight = cameraAccessor.getEyeHeight();
            } else {
                return originalPos;
            }

            return PlayerUtils.getPosWithEyeHeight(instance, v, eyeHeight);
        } else {
            return originalPos;
        }
    }

    @ModifyExpressionValue(method = "getPlayerHandPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isCrouching()Z"))
    private boolean animatium$noMoveFishingRodLine(boolean original, @Local(argsOnly = true) Player player) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().noMoveFishingRodLine) {
            return false;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "getPlayerHandPos", at = @At(value = "CONSTANT", args = "doubleValue=0.8"))
    private double animatium$fishingRodLinePositionThirdPerson(double original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().fishingRodLinePositionThirdPerson) {
            return original + 0.05;
        } else {
            return original;
        }
    }

    @WrapOperation(method = "getPlayerHandPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/FishingHookRenderer;getHoldingArm(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/entity/HumanoidArm;"))
    private HumanoidArm animatium$fixCastLineCheck(Player player, Operation<HumanoidArm> original) {
        HumanoidArm value = original.call(player);
        if (AnimatiumConfig.instance().fixCastLineCheck && value != player.getMainArm() && !(player.getOffhandItem().getItem() instanceof FishingRodItem)) {
            return value.getOpposite();
        } else {
            return value;
        }
    }

    @ModifyArg(method = "extractRenderState(Lnet/minecraft/world/entity/projectile/FishingHook;Lnet/minecraft/client/renderer/entity/state/FishingHookRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/FishingHookRenderer;getPlayerHandPos(Lnet/minecraft/world/entity/player/Player;FF)Lnet/minecraft/world/phys/Vec3;"), index = 1)
    private float animatium$fixCastLineSwing(float original, @Local(argsOnly = true) FishingHook fishingHook) {
        final Player player = fishingHook.getPlayerOwner();
        if (AnimatiumConfig.instance().fixCastLineSwing && player != null) {
            return original * PlayerUtils.getHandMultiplier(player);
        } else {
            return original;
        }
    }

    @Unique
    private void animatium$modifyPlanarScale(Args args, int ordinal) {
        args.set(ordinal, ((float) args.get(ordinal)) + 0.15F);
    }
}
