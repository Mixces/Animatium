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
 */

package btw.mixces.animatium.util;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public final class EntityUtils {
    private static final ThreadLocal<@Nullable HumanoidRenderState> HUMAN_RENDER_STATE = ThreadLocal.withInitial(() -> null);
    private static final HashMap<EntityRenderState, Entity> STATE_TO_ENTITY = new HashMap<>();

    public static void setEntityByState(EntityRenderState state, Entity entity) {
        STATE_TO_ENTITY.put(state, entity);
    }

    public static @Nullable Entity getEntityByState(EntityRenderState state) {
        return STATE_TO_ENTITY.getOrDefault(state, null);
    }

    public static void setHumanRenderState(HumanoidRenderState state) {
        HUMAN_RENDER_STATE.set(state);
    }

    public static void removeHumanRenderState() {
        HUMAN_RENDER_STATE.remove();
    }

    public static @Nullable HumanoidRenderState getHumanRenderState() {
        return HUMAN_RENDER_STATE.get();
    }
}
