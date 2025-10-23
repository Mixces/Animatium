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

package btw.mixces.animatium.mixins.v1.general.camera.view_bobbing;

import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.ViewBobbingStorage;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
    protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSpeed(F)V", shift = At.Shift.AFTER))
    private void animatium$updateBobbingTiltValues(CallbackInfo ci) {
        if (AnimatiumConfig.instance().fixes.fixVerticalBobbingTilt) {
            ViewBobbingStorage bobbingAccessor = (ViewBobbingStorage) this;
            final float tilt = this.onGround() || this.getHealth() <= 0.0F ? 0.0F : (float) (Math.atan(-this.getDeltaMovement().y * (double) 0.2F) * 15.0F);
            bobbingAccessor.animatium$setBobbingTilt(Mth.lerp(0.8F, bobbingAccessor.animatium$getBobbingTilt(), tilt));
        }
    }
}
