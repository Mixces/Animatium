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
import btw.mixces.animatium.LegacyGlintType;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.MathUtils;
import btw.mixces.animatium.util.RenderUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Nullable
    private ClientLevel level;

    @Unique
    private final VertexBuffer animatium$blueVoidSkyBuffer = VertexBuffer.uploadStatic(
            VertexFormat.Mode.QUADS,
            DefaultVertexFormat.POSITION,
            (vertexConsumer) -> RenderUtils.buildSkyHalf(vertexConsumer, -16.0F, true));

    @Inject(method = "method_62215", at = @At("TAIL"))
    private void animatium$blueVoidSky(FogParameters fogParameters, DimensionSpecialEffects.SkyType skyType, float tickDelta, DimensionSpecialEffects dimensionSpecialEffects, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().blueVoidSky && skyType != DimensionSpecialEffects.SkyType.END && this.level != null && this.minecraft.player != null) {
            int skyColor = this.level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getPosition(), tickDelta);
            this.animatium$renderSkyBlueVoid(skyColor, this.minecraft.player.getEyePosition(tickDelta).y - RenderUtils.getLevelHorizonHeight(this.level));
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

    @WrapOperation(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/DimensionSpecialEffects;getCloudHeight()F"))
    private float animatium$cloudHeight(DimensionSpecialEffects instance, Operation<Float> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().cloudHeight) {
            // TODO/FIX: Clouds showing in the nether/end supposedly?
            return instance.skyType() == DimensionSpecialEffects.SkyType.END ? 8.0F : 128.0F;
        } else {
            return original.call(instance);
        }
    }

    @Unique
    private void animatium$renderSkyBlueVoid(int skyColor, double depth) {
        assert this.level != null;
        Vector3f skyColorVec = ARGB.vector3fFromRGB24(skyColor);
        if (this.level.effects().hasGround()) {
            RenderSystem.setShaderColor(skyColorVec.x * 0.2F + 0.04F, skyColorVec.y * 0.2F + 0.04F, skyColorVec.z * 0.6F + 0.1F, 1.0F);
        } else {
            RenderSystem.setShaderColor(skyColorVec.x, skyColorVec.y, skyColorVec.z, 1.0F);
        }

        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        // TODO: AnimatiumConfig.instance().dontMoveBlueVoid
//        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().dontMoveBlueVoid) {
//            modelViewStack.translate(0.0F, 12.0F, 0.0F);
//        } else {
        modelViewStack.translate(0.0F, -((float) (depth - 16.0)), 0.0F);
//        }

        animatium$blueVoidSkyBuffer.drawWithRenderType(RenderType.sky());
        modelViewStack.popMatrix();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
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

    @WrapOperation(method = "method_62214", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch(Lnet/minecraft/client/renderer/RenderType;)V", ordinal = 16))
    private void animatium$legacyGlintRendering$endBatch(MultiBufferSource.BufferSource instance, RenderType renderType, Operation<Void> original) {
        original.call(instance, renderType);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().glintRendering) {
            instance.endBatch(LegacyGlintType.ITEM_GLINT_LAYER);
            instance.endBatch(LegacyGlintType.ITEM_GLINT_2ND_LAYER);
            instance.endBatch(LegacyGlintType.ITEM_GLINT_TRANSLUCENT_LAYER);
            instance.endBatch(LegacyGlintType.ITEM_GLINT_TRANSLUCENT_2ND_LAYER);
            instance.endBatch(LegacyGlintType.ENTITY_GLINT_LAYER);
            instance.endBatch(LegacyGlintType.ENTITY_ARMOR_GLINT_LAYER);
        }
    }

    // TODO/NOTE: The reason we redirect width/height instead of changing the outcome of shouldShowEntityOutlines
    // TODO/NOTE: is that it caused issues with Iris/shaders. As simple as that. Until that is fixed/we find another way
    // TODO/NOTE: it will stay like this. Sorry!
    @WrapOperation(method = "doEntityOutline", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getWidth()I"))
    private int animatium$entityGlowOutline$width(Window instance, Operation<Integer> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().entityGlowOutline) {
            return 0;
        } else {
            return original.call(instance);
        }
    }

    @WrapOperation(method = "doEntityOutline", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getHeight()I"))
    private int animatium$entityGlowOutline$height(Window instance, Operation<Integer> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().entityGlowOutline) {
            return 0;
        } else {
            return original.call(instance);
        }
    }
}