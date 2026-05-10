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

package org.visuals.legacy.animatium.mixins.v1.entity.items;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.rendering.AnimatiumPipelines;

@Mixin(RenderType.class)
public abstract class MixinRenderType_ItemColors2D {
    @Shadow
    public abstract RenderPipeline pipeline();

    @ModifyExpressionValue(method = "prepare", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/rendertype/RenderSetup;pipeline:Lcom/mojang/blaze3d/pipeline/RenderPipeline;", opcode = Opcodes.GETFIELD))
    private RenderPipeline animatium$legacy2dItemColors(final RenderPipeline original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.itemColors2D) {
            if (this.pipeline() == RenderPipelines.ITEM_CUTOUT) {
                return AnimatiumPipelines.LEGACY_ITEM_CUTOUT;
            } else if (this.pipeline() == RenderPipelines.ITEM_TRANSLUCENT) {
                return AnimatiumPipelines.LEGACY_ITEM_TRANSLUCENT;
            }
        }

        return original;
    }
}
