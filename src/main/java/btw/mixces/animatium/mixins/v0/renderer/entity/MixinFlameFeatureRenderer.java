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

package btw.mixces.animatium.mixins.v0.renderer.entity;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.PlayerUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

@Mixin(FlameFeatureRenderer.class)
public abstract class MixinFlameFeatureRenderer {
    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;boundingBoxWidth:F"))
    private float animatium$flameWidth(float original, @Local(argsOnly = true) EntityRenderState entityRenderState) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().flameDimensions && entityRenderState instanceof AvatarRenderState) {
            return 0.6F;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;boundingBoxHeight:F"))
    private float animatium$flameHeight(float original, @Local(argsOnly = true) EntityRenderState entityRenderState) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().flameDimensions && entityRenderState instanceof AvatarRenderState) {
            return 1.8F;
        } else {
            return original;
        }
    }

    @ModifyArg(method = "renderFlame", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack$Pose;translate(FFF)Lorg/joml/Matrix4f;", ordinal = 0), index = 1)
    private float animatium$flameOffset(float original, @Local(argsOnly = true) EntityRenderState entityRenderState) {
        if (AnimatiumClient.isEnabled() && entityRenderState instanceof AvatarRenderState avatarRenderState) {
            boolean shouldSyncPlayerModelWithEyeHeight = AnimatiumConfig.instance().syncPlayerModelWithEyeHeight;
            if (shouldSyncPlayerModelWithEyeHeight) {
                original = (Player.STANDING_DIMENSIONS.eyeHeight() * avatarRenderState.scale) - PlayerUtils.lerpCameraPosition(Objects.requireNonNull(Minecraft.getInstance().getEntityRenderDispatcher().camera));
            }

            if (AnimatiumConfig.instance().flameOffset) {
                original += ((shouldSyncPlayerModelWithEyeHeight && avatarRenderState.isCrouching ? 0.140625F : 0.296875F) * avatarRenderState.scale);
            }
        }

        return original;
    }
}
