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

package org.visuals.legacy.animatium.mixins.v1.rendering.sky.the_void;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.mixins.accessor.ClientLevelDataAccessor;

@Mixin(ClientLevel.class)
public abstract class MixinClientLevel_VoidParticles {
    @WrapOperation(method = "doAnimateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"))
    private void animatium$voidParticles(Block instance, BlockState blockState, Level level, BlockPos blockPos, RandomSource random, Operation<Void> original) {
        // TODO: Fix minY
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.voidParticles && blockState.isAir() && random.nextInt(8) > blockPos.getY() && !animatium$hasFog((ClientLevel) level)) {
            // NOTE: Depth Suspend particle as a thing doesn't exist anymore
            // But its class still does as it's used by HappyVillager/Composter/Etc
            // And MYCELIUM is literally just Depth Suspend
            // as it's just the raw provider!
            level.addParticle(ParticleTypes.MYCELIUM, blockPos.getX() + random.nextFloat(), blockPos.getY() + random.nextFloat(), blockPos.getZ() + random.nextFloat(), 0.0, 0.0, 0.0);
        } else {
            original.call(instance, blockState, level, blockPos, random);
        }
    }

    @Unique
    private boolean animatium$hasFog(ClientLevel level) {
        final ClientLevelDataAccessor levelDataAccessor = (ClientLevelDataAccessor) level.getLevelData();
        return !levelDataAccessor.animatium$isFlatWorld() && !level.dimensionType().ultraWarm(); // NOTE: From checking, ultraWarm is the same/equivalent to "isDark" in 1.8.9/etc
    }
}
