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

package btw.mixces.animatium.mixins.renderer;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.MathUtils;
import btw.mixces.animatium.util.RenderUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Nullable
    private ClientLevel level;

    @Unique
    private GpuBuffer animatium$blueVoidBuffer = null;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(Minecraft minecraft, EntityRenderDispatcher entityRenderDispatcher, BlockEntityRenderDispatcher blockEntityRenderDispatcher, RenderBuffers renderBuffers, CallbackInfo ci) {
        VertexFormat.Mode mode = VertexFormat.Mode.QUADS;
        BufferBuilder builder = Tesselator.getInstance().begin(mode, DefaultVertexFormat.POSITION);
        RenderUtils.buildSkyHalf(builder, -16.0F, true);
        try (MeshData meshData = builder.buildOrThrow()) {
            this.animatium$blueVoidBuffer = RenderSystem.getDevice().createBuffer(() -> "Blue void sky vertex buffer", GpuBuffer.USAGE_COPY_DST, meshData.vertexBuffer());
        }
    }

    @Inject(method = "method_62215", at = @At("TAIL"))
    private void animatium$blueVoidSky(GpuBufferSlice fogParameters, DimensionSpecialEffects.SkyType skyType, float tickDelta, DimensionSpecialEffects dimensionSpecialEffects, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().blueVoidSky && skyType != DimensionSpecialEffects.SkyType.END && this.level != null && this.minecraft.player != null) {
            int skyColor = this.level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getPosition(), tickDelta);
            RenderUtils.renderBlueVoidSky(this.minecraft, this.animatium$blueVoidBuffer, skyColor, this.minecraft.player.getEyePosition(tickDelta).y - RenderUtils.getLevelHorizonHeight(this.level));
        }
    }

    @WrapOperation(method = "shouldRenderDarkDisc", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel$ClientLevelData;getHorizonHeight(Lnet/minecraft/world/level/LevelHeightAccessor;)D"))
    private double animatium$skyHorizonHeight(ClientLevel.ClientLevelData instance, LevelHeightAccessor levelHeightAccessor, Operation<Double> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().skyHorizonHeight && this.level != null) {
            return RenderUtils.getLevelHorizonHeight(this.level);
        } else {
            return original.call(instance, levelHeightAccessor);
        }
    }

    @WrapOperation(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;cloudHeight()Ljava/util/Optional;"))
    private Optional<Integer> animatium$cloudHeight(DimensionType instance, Operation<Optional<Integer>> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().cloudHeight) {
            // TODO/FIX: Clouds showing in the nether/end supposedly?
            assert this.level != null;
            return Optional.of(this.level.effects().skyType() == DimensionSpecialEffects.SkyType.END ? 8 : 128);
        } else {
            return original.call(instance);
        }
    }

    @Inject(method = "renderBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)V", shift = At.Shift.BEFORE))
    private void animatium$setBlockOutlineWidth$on(Camera camera, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean bl, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().blockOutlineRendering) {
            RenderUtils.setLineWidth(2.0F);
        }
    }

    @Inject(method = "renderBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)V", shift = At.Shift.BEFORE))
    private void animatium$setBlockOutlineWidth$off(Camera camera, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean bl, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().blockOutlineRendering) {
            RenderUtils.setLineWidth(-1.0F);
        }
    }

    @WrapOperation(method = "renderHitOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private VoxelShape animatium$blockOutlineRendering(BlockState instance, BlockGetter blockView, BlockPos blockPos, CollisionContext collisionContext, Operation<VoxelShape> original) {
        VoxelShape shape = original.call(instance, blockView, blockPos, collisionContext);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().blockOutlineRendering) {
            return MathUtils.expandVoxelShape(shape, 0.0020000000949949026F);
        } else {
            return shape;
        }
    }

//    @WrapOperation(method = "method_62214", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch(Lnet/minecraft/client/renderer/RenderType;)V", ordinal = 16))
//    private void animatium$legacyGlintRendering$endBatch(MultiBufferSource.BufferSource instance, RenderType renderType, Operation<Void> original) {
//        original.call(instance, renderType);
//        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().glintRendering) {
//            instance.endBatch(LegacyGlintType.ITEM_GLINT_LAYER);
//            instance.endBatch(LegacyGlintType.ITEM_GLINT_2ND_LAYER);
//            instance.endBatch(LegacyGlintType.ITEM_GLINT_TRANSLUCENT_LAYER);
//            instance.endBatch(LegacyGlintType.ITEM_GLINT_TRANSLUCENT_2ND_LAYER);
//            instance.endBatch(LegacyGlintType.ENTITY_GLINT_LAYER);
//            instance.endBatch(LegacyGlintType.ENTITY_ARMOR_GLINT_LAYER);
//        }
//    }

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