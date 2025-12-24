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

package org.visuals.legacy.animatium.mixins.v1.entity.particles;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

// TODO: 3.1
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity_FallParticles extends Entity {
	public MixinLivingEntity_FallParticles(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	/*@Shadow
	public abstract double getAttributeValue(Holder<Attribute> attribute);

    @Definition(id = "ServerLevel", type = ServerLevel.class)
    @Expression("? instanceof ServerLevel")
    @ModifyExpressionValue(method = "checkFallDamage", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private <T extends ParticleOptions> boolean animatium$oldFallParticlePhysics$disableServer(boolean original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.oldFallParticlePhysics) {
            return false;
        } else {
            return original;
        }
    }

    // Code sourced from 14w26a and modified/shortened
    @Inject(method = "checkFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;checkFallDamage(DZLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", shift = At.Shift.BEFORE))
    private void animatium$oldFallParticlePhysics(double y, boolean onGround, BlockState state, BlockPos pos, CallbackInfo ci) {
        final double safeFallDist = this.getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.oldFallParticlePhysics && this.fallDistance > safeFallDist && onGround && !state.isAir()) {
            final double scale = Math.min(Math.min(0.2F + Mth.ceil(this.fallDistance - safeFallDist) / 15.0F, 10.0F), 2.5);
            for (int particle = 0; particle < (int) (150.0 * scale); particle++) {
                final float angle = Mth.nextFloat(this.random, 0.0F, (float) (Math.PI * 2));
                final double variance = Mth.nextFloat(this.random, 0.75F, 1.0F);
                this.level().addParticle(
                        new BlockParticleOption(ParticleTypes.BLOCK, state),
                        pos.getX() + 0.5F,
                        pos.getY() + 1.0F,
                        pos.getZ() + 0.5F,
                        Mth.cos(angle) * 0.2F * variance * variance * (scale + 0.2),
                        0.2F + scale / 100.0,
                        Mth.sin(angle) * 0.2F * variance * variance * (scale + 0.2)
                );
            }
        }
    }*/
}
