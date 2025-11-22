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

package org.visuals.legacy.animatium.mixins.v1.general.items.flat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

import java.util.List;

// TODO/FIX
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @Unique
    private static ItemDisplayContext animatium$displayContext = null;

    @ModifyArg(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderQuadList(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;[III)V"), index = 2)
    private static List<BakedQuad> animatium$itemDrops2D(List<BakedQuad> quads, @Local(argsOnly = true) ItemDisplayContext displayContext, @Local(argsOnly = true) RenderType renderType) {
        animatium$displayContext = displayContext;
//        if (AnimatiumClient.ENABLED && animatium$isTransformationModeValid(displayContext) /* && !blocksLight */) {
//            return quads.stream().filter(baked -> baked.direction() == Direction.SOUTH).collect(Collectors.toList());
//        } else {
        return quads;
//        }
    }

    // TODO: this is only half of the battle + framed item 2d colors are disabled
    @WrapOperation(method = "renderQuadList", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;FFFFII)V"))
    private static void animatium$itemColors2D(VertexConsumer instance, PoseStack.Pose pose, BakedQuad bakedQuad, float f, float g, float h, float i, int j, int k, Operation<Void> original) {
        if (Animatium.ENABLED && AnimatiumConfig.instance().items.itemColors2D && animatium$displayContext == ItemDisplayContext.GROUND /* && !blocksLight */) {
            // TODO/Modify: bakedQuad.direction().getUnitVec3f();
            // return new Vector3f(vector3fc.x(), vector3fc.z(), vector3fc.y());
        }

        original.call(instance, pose, bakedQuad, f, g, h, i, j, k);
    }

    @Unique
    private static boolean animatium$isTransformationModeValid(ItemDisplayContext displayContext) {
        final boolean itemDrops2D = AnimatiumConfig.instance().items.itemDrops2D;
        final boolean itemFramed2D = AnimatiumConfig.instance().items.itemFramed2D;
        return (itemDrops2D && displayContext == ItemDisplayContext.GROUND) || (itemFramed2D && displayContext == ItemDisplayContext.FIXED);
    }
}
