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

package btw.mixces.animatium.mixins.v0.item;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.ItemUtils;
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
    @ModifyArg(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderQuadList(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;[III)V"), index = 2)
    private static List<BakedQuad> animatium$itemDrops2D(List<BakedQuad> quads) {
        final ItemStackRenderState itemStackRenderState = ItemUtils.getRenderState();
        if (AnimatiumClient.isEnabled() && animatium$isTransformationModeValid() && itemStackRenderState != null && !itemStackRenderState.usesBlockLight()) {
            return quads.stream().filter(baked -> baked.direction() == Direction.SOUTH).collect(Collectors.toList());
        } else {
            return quads;
        }
    }

    @Unique
    private static boolean animatium$isTransformationModeValid() {
        final ItemDisplayContext displayContext = ItemUtils.getDisplayContext();
        boolean itemDrops2D = AnimatiumConfig.instance().itemDrops2D;
        boolean itemFramed2D = AnimatiumConfig.instance().itemFramed2D;
        return (itemDrops2D && displayContext == ItemDisplayContext.GROUND) || (itemFramed2D && displayContext == ItemDisplayContext.FIXED);
    }
}
