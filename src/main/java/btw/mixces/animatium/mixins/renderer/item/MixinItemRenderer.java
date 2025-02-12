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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    // tint: -8372020

    @WrapOperation(method = "getCompassFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;glint()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType animatium$legacyGlintRendering$compassGlint(Operation<RenderType> original) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getOldGlintRendering()) {
            return LegacyGlintType.getGlintLayer();
        } else {
            return original.call();
        }
    }

    // TODO: Entity Glint
    @WrapOperation(method = "getFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;glintTranslucent()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType animatium$legacyGlintRendering$glintTranslucent(Operation<RenderType> original) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getOldGlintRendering()) {
            return LegacyGlintType.getGlintTranslucentLayer();
        } else {
            return original.call();
        }
    }

    @WrapOperation(method = "getFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;glint()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType animatium$legacyGlintRendering$glint(Operation<RenderType> original) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getOldGlintRendering()) {
            return LegacyGlintType.getGlintLayer();
        } else {
            return original.call();
        }
    }

    @WrapOperation(method = "renderModelLists", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;)Ljava/util/List;", ordinal = 1))
    private static List<BakedQuad> animatium$itemDrops2D(BakedModel instance, BlockState state, Direction direction, RandomSource random, Operation<List<BakedQuad>> original) {
        List<BakedQuad> quads = original.call(instance, state, direction, random);
        if (AnimatiumClient.getEnabled() && animatium$isTransformationModeValid() && !instance.isGui3d()) {
            return quads.stream().filter(baked -> baked.getDirection() == Direction.SOUTH).collect(Collectors.toList());
        } else {
            return quads;
        }
    }

    @Unique
    private static boolean animatium$isTransformationModeValid() {
        ItemDisplayContext displayContext = ItemUtils.getDisplayContext();
        boolean itemDrops2D = AnimatiumConfig.instance().getItemDrops2D();
        boolean itemFramed2D = AnimatiumConfig.instance().getItemFramed2D();
        return (itemDrops2D && displayContext == ItemDisplayContext.GROUND) || (itemFramed2D && displayContext == ItemDisplayContext.FIXED);
    }
}
