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

package btw.mixces.animatium.mixins.v1.entity.particles;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    @Shadow
    @Final
    private Map<Holder<MobEffect>, MobEffectInstance> activeEffects;

    @Shadow
    @Final
    private static EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID;

    @Unique
    private static final EntityDataAccessor<Integer> animatium$DATA_EFFECT_COLOR_ID =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);

    public MixinLivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @WrapWithCondition(method = "tickEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private boolean animatium$hideFirstPersonParticles(Level instance, ParticleOptions particle, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        final Minecraft client = Minecraft.getInstance();
        return !AnimatiumClient.ENABLED || !AnimatiumConfig.instance().extras.disableFirstPersonParticles || this.getId() != client.player.getId() || !client.options.getCameraType().isFirstPerson();
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void animatium$defineEffectColorID(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(animatium$DATA_EFFECT_COLOR_ID, 0);
    }

    @Inject(method = "updateSynchronizedMobEffectParticles", at = @At("TAIL"))
    private void animatium$setEffectData(CallbackInfo ci, @Local List<ParticleOptions> particleOptions) {
        if (AnimatiumClient.ENABLED && AnimatiumConfig.instance().other.restoreParticleBlending) {
            this.entityData.set(animatium$DATA_EFFECT_COLOR_ID, animatium$getPotionColor(this.activeEffects.values()));
        }
    }

    @WrapOperation(method = "tickEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void animatium$blendParticles(Level instance, ParticleOptions particle, double x, double y, double z, double r, double g, double b, Operation<Void> original) {
        int color = this.entityData.get(animatium$DATA_EFFECT_COLOR_ID);
        if (AnimatiumClient.ENABLED && AnimatiumConfig.instance().other.restoreParticleBlending) {
            boolean hasAmbience = this.entityData.get(DATA_EFFECT_AMBIENCE_ID);
            boolean showParticle = this.isInvisible() ? this.random.nextInt(15) == 0 : this.random.nextBoolean();
            if (hasAmbience) {
                showParticle &= this.random.nextInt(5) == 0;
            }

            if (!showParticle || color == 0) {
                return;
            }

            particle = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.color(hasAmbience ? 0.15F : 1.0F, color));
            r = ARGB.redFloat(color);
            g = ARGB.greenFloat(color);
            b = ARGB.blueFloat(color);
        }

        original.call(instance, particle, x, y, z, r, g, b);
    }

    @Inject(method = "removeEffectParticles", at = @At("TAIL"))
    private void animatium$clearEffectData(CallbackInfo ci) {
        if (AnimatiumClient.ENABLED && AnimatiumConfig.instance().other.restoreParticleBlending) {
            this.entityData.set(DATA_EFFECT_AMBIENCE_ID, false); // TODO/NOTE: Find out why these removed this/if it's needed
            this.entityData.set(animatium$DATA_EFFECT_COLOR_ID, 0);
        }
    }

    @Unique
    private static int animatium$getPotionColor(Collection<MobEffectInstance> effects) {
        return effects.isEmpty() ? 3694022 : PotionContents.getColorOptional(effects).orElse(0);
    }
}

