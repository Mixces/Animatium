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

package org.visuals.legacy.animatium.util.rendering.lighting.lightmap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

public class LegacyLightmapExtractor {
    private boolean needsUpdate;
    private float blockLightRed;

    public void tick(final float blockLightFlicker) {
        this.blockLightRed = this.blockLightRed + (blockLightFlicker - this.blockLightRed);
        this.needsUpdate = true;
    }

    public void extract(final Minecraft minecraft, final LegacyLightmapState state, final float tickDelta) {
        state.needsUpdate = this.needsUpdate;
        if (this.needsUpdate) {
            final ClientLevel level = minecraft.level;
            final LocalPlayer player = minecraft.player;
            if (level != null && player != null) {
                final ProfilerFiller profiler = Profiler.get();
                profiler.push("lightmap");
                state.skyDarken = getSkyDarken(level);
                state.skyFlicker = state.skyDarken * 0.95F + 0.05F;
                state.blockFlicker = this.blockLightRed;
                state.skyDarkness = minecraft.gameRenderer.bossOverlayWorldDarkening(tickDelta);
                if (player.hasEffect(MobEffects.NIGHT_VISION)) {
                    state.nightVisionScale = GameRenderer.nightVisionScale(player, tickDelta);
                }

                state.gamma = minecraft.options.gamma().get().floatValue();
                state.useBrightLightmap = ClientLevel.END.equals(level.dimension());
                profiler.pop();
                this.needsUpdate = false;
            }
        }
    }

    private float getSkyDarken(final ClientLevel level) {
        float value = 1.0F - (Mth.cos(getTimeOfDay(level, 1.0F) * (float) (Math.PI * 2)) * 2.0F + 0.2F);
        value = Mth.clamp(value, 0.0F, 1.0F);
        value = 1.0F - value;
        value *= 1.0F - level.getRainLevel(1.0F) * 5.0F / 16.0F;
        value *= 1.0F - level.getThunderLevel(1.0F) * 5.0F / 16.0F;
        return value * 0.8F + 0.2F;
    }

    private float getTimeOfDay(final ClientLevel level, final float tickDelta) {
        long dayTime = level.getOverworldClockTime();
        if (dayTime == 0) {
            dayTime = 1; // 1.8 never lets the tick time be 0
        }

        if (level.dimensionType().hasFixedTime()) {
            final ResourceKey<Level> dimension = level.dimension();
            if (Level.NETHER.equals(dimension)) {
                dayTime = 18000L;
            } else if (Level.END.equals(dimension)) {
                dayTime = 6000L;
            }
        }

        final int time = (int) (dayTime % 24000L);
        float frac = ((float) time + tickDelta) / 24000.0F - 0.25F;
        if (frac < 0.0F) {
            ++frac;
        }

        if (frac > 1.0F) {
            --frac;
        }

        final float mul = 1.0F - (float) ((Math.cos((double) frac * Math.PI) + 1.0) / 2.0);
        frac += (mul - frac) / 3.0F;
        return frac;
    }
}
