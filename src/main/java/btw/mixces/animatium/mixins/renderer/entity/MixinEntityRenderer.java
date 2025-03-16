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
import btw.mixces.animatium.util.EntityUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private <T extends Entity, S extends EntityRenderState> void animatium$saveEntityByState(T entity, S state, float tickDelta, CallbackInfo ci) {
        EntityUtils.setEntityByState(state, entity);
    }

    @WrapOperation(method = "renderNameTag", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;isDiscrete:Z"))
    private boolean animatium$sneakAnimationWhileFlying(EntityRenderState instance, Operation<Boolean> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().sneakAnimationWhileFlying && instance instanceof LivingEntityRenderState livingEntityRenderState) {
            return livingEntityRenderState.isDiscrete || livingEntityRenderState.hasPose(Pose.CROUCHING);
        } else {
            return original.call(instance);
        }
    }

    @WrapOperation(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getBackgroundOpacity(F)F"))
    private float animatium$hideNameTagBackground(Options instance, float fallback, Operation<Float> original) {
        if (!AnimatiumClient.isEnabled() && AnimatiumConfig.instance().nameTagBackground) {
            return 0F;
        } else {
            return original.call(instance, fallback);
        }
    }

    @ModifyArg(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/network/chat/Component;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I"), index = 4)
    private boolean animatium$applyTextShadowToNametag(boolean shadow) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().nameTagTextShadow) {
            return true;
        } else {
            return shadow;
        }
    }
}
