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

package btw.mixces.animatium.mixins.v1.general.outlines;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.RenderUtils;
import btw.mixces.animatium.util.Utils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    // Old Block Outline
    @Inject(method = "renderBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDLnet/minecraft/client/renderer/state/BlockOutlineRenderState;I)V", shift = At.Shift.BEFORE))
    private void animatium$setBlockOutlineWidth$on(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean bl, LevelRenderState levelRenderState, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().blockOutlineRendering) {
            RenderUtils.setLineWidth(2.0F);
        }
    }

    @Inject(method = "renderBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDLnet/minecraft/client/renderer/state/BlockOutlineRenderState;I)V", shift = At.Shift.BEFORE))
    private void animatium$setBlockOutlineWidth$off(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean bl, LevelRenderState levelRenderState, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().blockOutlineRendering) {
            RenderUtils.setLineWidth(-1.0F);
        }
    }

    @WrapOperation(method = "renderHitOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/state/BlockOutlineRenderState;shape()Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private VoxelShape animatium$blockOutlineRendering(BlockOutlineRenderState instance, Operation<VoxelShape> original) {
        VoxelShape shape = original.call(instance);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().blockOutlineRendering) {
            return Utils.expandVoxelShape(shape, 0.0020000000949949026F);
        } else {
            return shape;
        }
    }

    // Remove Entity Outline
    @Unique
    private GpuTexture animatium$blankTexture = null;

    @Unique
    private GpuTextureView animatium$blankTextureView = null;

    @WrapOperation(method = "doEntityOutline", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;blitAndBlendToTexture(Lcom/mojang/blaze3d/textures/GpuTextureView;)V"))
    private void animatium$entityGlowOutline(RenderTarget instance, GpuTextureView gpuTextureView, Operation<Void> original) {
        GpuTextureView textureView = gpuTextureView;
        if (AnimatiumClient.isEnabled() && !AnimatiumConfig.instance().entityGlowOutline && RenderSystem.getDevice() instanceof GlDevice glDevice) {
            if (this.animatium$blankTexture == null) {
                this.animatium$blankTexture = glDevice.createTexture(() -> "Blank", 15, TextureFormat.RGBA8, 1, 1, 1, 1);
            }

            if (this.animatium$blankTextureView == null) {
                this.animatium$blankTextureView = glDevice.createTextureView(this.animatium$blankTexture);
            }

            textureView = this.animatium$blankTextureView;
        }

        original.call(instance, textureView);
    }
}