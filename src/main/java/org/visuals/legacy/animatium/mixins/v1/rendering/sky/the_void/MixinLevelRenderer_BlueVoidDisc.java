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

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.rendering.SkyRendererUtility;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer_BlueVoidDisc {
    @Inject(method = "lambda$addSkyPass$0", at = @At("TAIL"))
    private static void animatium$blueVoidSky(final GpuBufferSlice skyFog, final SkyRenderState skyRenderState, final CallbackInfo ci) {
        final Minecraft minecraft = Minecraft.getInstance();
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.blueVoidSky && skyRenderState.skybox != DimensionType.Skybox.END && minecraft.level != null && minecraft.player != null) {
            final float tickDelta = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);
            SkyRendererUtility.renderBlueVoid(skyRenderState.skyColor, SkyRendererUtility.getHorizonEyeHeight(minecraft.level, tickDelta));
        }
    }

    @Inject(method = "close", at = @At("TAIL"))
    private void animatium$closeSkyRenderUtility(CallbackInfo ci) {
        SkyRendererUtility.close();
    }
}
