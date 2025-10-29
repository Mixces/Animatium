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

package org.visuals.legacy.animatium.mixins.v1.render_states;

import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.util.states.UtilityRenderState;

@Mixin(ArmedEntityRenderState.class)
public abstract class MixinArmedEntityRenderState implements UtilityRenderState {
    // TODO/NOTE: 25w43a seemingly has these fields now?
    @Unique
    private ItemStack animatium$leftStack = ItemStack.EMPTY;

    @Unique
    private ItemStack animatium$rightStack = ItemStack.EMPTY;

    @Unique
    private boolean animatium$isFishing = false;

    @Unique
    private boolean animatium$isSleeping = false;

    @Inject(method = "extractArmedEntityRenderState", at = @At("TAIL"))
    private static void animatium$storeStacks(LivingEntity livingEntity, ArmedEntityRenderState armedEntityRenderState, ItemModelResolver itemModelResolver, CallbackInfo ci) {
        UtilityRenderState utilityRenderState = (UtilityRenderState) armedEntityRenderState;
        utilityRenderState.animatium$setItemHeldByArm(HumanoidArm.LEFT, livingEntity.getItemHeldByArm(HumanoidArm.LEFT));
        utilityRenderState.animatium$setItemHeldByArm(HumanoidArm.RIGHT, livingEntity.getItemHeldByArm(HumanoidArm.RIGHT));
        if (livingEntity instanceof Player player && player.fishing != null) {
            utilityRenderState.animatium$setFishing();
        }

        if (livingEntity.isSleeping()) {
            utilityRenderState.animatium$setSleeping();
        }
    }

    @Override
    public ItemStack animatium$getItemHeldByArm(HumanoidArm arm) {
        if (arm == HumanoidArm.LEFT) {
            return animatium$leftStack;
        } else if (arm == HumanoidArm.RIGHT) {
            return animatium$rightStack;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void animatium$setItemHeldByArm(HumanoidArm arm, ItemStack itemStack) {
        if (arm == HumanoidArm.LEFT) {
            animatium$leftStack = itemStack;
        } else if (arm == HumanoidArm.RIGHT) {
            animatium$rightStack = itemStack;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean animatium$isFishing() {
        return animatium$isFishing;
    }

    @Override
    public void animatium$setFishing() {
        animatium$isFishing = true;
    }

    @Override
    public boolean animatium$isSleeping() {
        return animatium$isSleeping;
    }

    @Override
    public void animatium$setSleeping() {
        animatium$isSleeping = true;
    }
}
