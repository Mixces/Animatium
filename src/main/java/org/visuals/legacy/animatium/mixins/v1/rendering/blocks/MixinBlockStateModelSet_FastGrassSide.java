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

package org.visuals.legacy.animatium.mixins.v1.rendering.blocks;

import net.minecraft.client.renderer.block.BlockStateModelSet;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockStateModelSet.class)
public abstract class MixinBlockStateModelSet_FastGrassSide {
    // TODO 26.2
    /*@Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void animatium$fastGrass(final BlockState state, final CallbackInfoReturnable<BlockStateModel> cir) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.fastGrass && (state.is(Blocks.GRASS_BLOCK) && !state.getValue(GrassBlock.SNOWY))) {
            cir.setReturnValue(Minecraft.getInstance().getModelManager().getModel(AnimatiumConstants.FAST_GRASS_MODEL_KEY));
        }
    }*/
}
