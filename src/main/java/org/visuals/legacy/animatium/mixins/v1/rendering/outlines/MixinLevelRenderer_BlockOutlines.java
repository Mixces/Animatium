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
 * <p>
 * "MINECRAFT" LINKING EXCEPTION TO THE GPL
 */

package org.visuals.legacy.animatium.mixins.v1.rendering.outlines;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.UtilsKt;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer_BlockOutlines {
    @ModifyArg(method = "submitBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;submitHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;IFZ)V", ordinal = 1), index = 5)
    private float animatium$oldBlockOutline(final float lineWidth) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.blockOutlineRendering) {
            return 2.0F;
        } else {
            return lineWidth;
        }
    }

    @WrapOperation(method = "submitHitOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;shape()Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private VoxelShape animatium$blockOutlineRendering(final BlockOutlineRenderState instance, final Operation<VoxelShape> original) {
        final VoxelShape shape = original.call(instance);
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.blockOutlineRendering) {
            return UtilsKt.expandVoxelShape(shape, 0.0020000000949949026D); // Value sourced from older minecraft version
        } else {
            return shape;
        }
    }
}