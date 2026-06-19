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

package org.visuals.legacy.animatium.util.rendering;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.visuals.legacy.animatium.util.Utils;

public final class LegacyFogDarkness {
    public static final LegacyFogDarkness INSTANCE = new LegacyFogDarkness();

    private float prevDarkness;
    private float darkness;

    LegacyFogDarkness() {
    }

    public void tick(final Entity entity, final int viewDistance) {
        this.prevDarkness = this.darkness;
        this.darkness = Mth.lerp(0.1F, this.darkness, Mth.lerp(Utils.getBrightness(entity), viewDistance / 32.0F, 1.0F));
    }

    public float getDarkness(final float tickDelta) {
        return Mth.lerp(tickDelta, this.prevDarkness, this.darkness);
    }
}
