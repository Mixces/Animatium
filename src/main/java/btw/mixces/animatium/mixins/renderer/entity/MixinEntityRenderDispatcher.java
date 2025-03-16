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
import btw.mixces.animatium.util.PlayerUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRenderDispatcher {
    @Shadow
    public Camera camera;

    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;boundingBoxWidth:F"))
    private float animatium$oldFlameWidth(float original, @Local(argsOnly = true) EntityRenderState entityRenderState) {
        Entity entity = EntityUtils.getEntityByState(entityRenderState);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().oldFlameDimensions && entity instanceof Player) {
            return 0.6F;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;boundingBoxHeight:F"))
    private float animatium$oldFlameHeight(float original, @Local(argsOnly = true) EntityRenderState entityRenderState) {
        Entity entity = EntityUtils.getEntityByState(entityRenderState);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().oldFlameDimensions && entity instanceof Player) {
            return 1.8F;
        } else {
            return original;
        }
    }

    @ModifyArg(method = "renderFlame", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 0), index = 1)
    private float animatium$oldFlameOffset(float original, @Local(argsOnly = true) EntityRenderState entityRenderState) {
        Entity entity = EntityUtils.getEntityByState(entityRenderState);
        if (AnimatiumClient.isEnabled() && entity instanceof Player player) {
            final float scale = player.getScale();
            boolean shouldSyncPlayerModelWithEyeHeight = AnimatiumConfig.instance().syncPlayerModelWithEyeHeight;
            if (shouldSyncPlayerModelWithEyeHeight) {
                original = (Player.STANDING_DIMENSIONS.eyeHeight() * scale) - PlayerUtils.lerpCameraPosition(camera);
            }

            if (AnimatiumConfig.instance().oldFlameOffset) {
                original += ((shouldSyncPlayerModelWithEyeHeight && player.isCrouching() ? 0.140625F : 0.296875F) * scale);
            }
        }

        return original;
    }
}
