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

package org.visuals.legacy.animatium.mixins.v1.general.outlines;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.Utils;
import org.visuals.legacy.animatium.util.rendering.LineState;
import org.visuals.legacy.animatium.util.rendering.RenderUtils;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer_BlockOutlines {
    @WrapOperation(method = "renderBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDLnet/minecraft/client/renderer/state/BlockOutlineRenderState;I)V"))
    private void animatium$setBlockOutlineWidth(LevelRenderer instance, PoseStack poseStack, VertexConsumer consumer, double camX, double camY, double camZ, BlockOutlineRenderState outlineRenderState, int color, Operation<Void> original) {
        final LineState lineState = RenderUtils.getLineState();
        if (Animatium.ENABLED && AnimatiumConfig.instance().other.blockOutlineRendering) {
            lineState.setWidth(2.0F);
        }

        original.call(instance, poseStack, consumer, camX, camY, camZ, outlineRenderState, color);
        if (Animatium.ENABLED && AnimatiumConfig.instance().other.blockOutlineRendering) {
            lineState.setWidth(-1.0F); // Restores vanilla value
        }
    }

    @WrapOperation(method = "renderHitOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/state/BlockOutlineRenderState;shape()Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private VoxelShape animatium$blockOutlineRendering(BlockOutlineRenderState instance, Operation<VoxelShape> original) {
        final VoxelShape shape = original.call(instance);
        if (Animatium.ENABLED && AnimatiumConfig.instance().other.blockOutlineRendering) {
            return Utils.expandVoxelShape(shape, 0.0020000000949949026F); // Value sourced from older minecraft version
        } else {
            return shape;
        }
    }
}