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

package btw.mixces.animatium.mixins.renderer.item;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.LegacyGlintType;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.ItemUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @WrapOperation(method = "getSpecialFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;glint()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType animatium$legacyGlintRendering$compassGlintLayer1(Operation<RenderType> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().glintRendering) {
            return LegacyGlintType.ITEM_GLINT_LAYER;
        } else {
            return original.call();
        }
    }

    @WrapOperation(method = "getSpecialFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;", ordinal = 0))
    private static VertexConsumer animatium$legacyGlintRendering$compassGlintLayer2(MultiBufferSource instance, RenderType renderType, Operation<VertexConsumer> original, @Local(argsOnly = true) MultiBufferSource multiBufferSource) {
        final VertexConsumer finalConsumer = original.call(instance, renderType);
        ItemDisplayContext displayContext = ItemUtils.getDisplayContext();
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().glintRendering && displayContext != ItemDisplayContext.GUI) {
            return VertexMultiConsumer.create(multiBufferSource.getBuffer(LegacyGlintType.ITEM_GLINT_2ND_LAYER), finalConsumer);
        } else {
            return finalConsumer;
        }
    }

    @WrapOperation(method = "getFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;glint()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType animatium$legacyGlintRendering$glintLayer1(Operation<RenderType> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().glintRendering) {
            return LegacyGlintType.ITEM_GLINT_LAYER;
        } else {
            return original.call();
        }
    }

    @WrapOperation(method = "getFoilBuffer", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexMultiConsumer;create(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lcom/mojang/blaze3d/vertex/VertexConsumer;)Lcom/mojang/blaze3d/vertex/VertexConsumer;", ordinal = 1))
    private static VertexConsumer animatium$legacyGlintRendering$glintLayer2(VertexConsumer leftConsumer, VertexConsumer rightConsumer, Operation<VertexConsumer> original, @Local(argsOnly = true) MultiBufferSource multiBufferSource, @Local(argsOnly = true, ordinal = 0) boolean bl) {
        final VertexConsumer finalConsumer = original.call(leftConsumer, rightConsumer);
        ItemDisplayContext displayContext = ItemUtils.getDisplayContext();
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().glintRendering && displayContext != ItemDisplayContext.GUI && bl) {
            return VertexMultiConsumer.create(multiBufferSource.getBuffer(LegacyGlintType.ITEM_GLINT_2ND_LAYER), finalConsumer);
        } else {
            return finalConsumer;
        }
    }

    @WrapOperation(method = "getFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entityGlint()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType animatium$legacyGlintRendering$entityGlint(Operation<RenderType> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().glintRendering) {
            return LegacyGlintType.ENTITY_GLINT_LAYER;
        } else {
            return original.call();
        }
    }

    @ModifyArg(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderQuadList(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;[III)V"), index = 2)
    private static List<BakedQuad> animatium$itemDrops2D(List<BakedQuad> quads) {
        ItemStackRenderState itemStackRenderState = ItemUtils.getRenderState();
        if (AnimatiumClient.isEnabled() && animatium$isTransformationModeValid() && itemStackRenderState != null && !itemStackRenderState.usesBlockLight()) {
            return quads.stream().filter(baked -> baked.direction() == Direction.SOUTH).collect(Collectors.toList());
        } else {
            return quads;
        }
    }

    @Unique
    private static boolean animatium$isTransformationModeValid() {
        ItemDisplayContext displayContext = ItemUtils.getDisplayContext();
        boolean itemDrops2D = AnimatiumConfig.instance().itemDrops2D;
        boolean itemFramed2D = AnimatiumConfig.instance().itemFramed2D;
        return (itemDrops2D && displayContext == ItemDisplayContext.GROUND) || (itemFramed2D && displayContext == ItemDisplayContext.FIXED);
    }
}
