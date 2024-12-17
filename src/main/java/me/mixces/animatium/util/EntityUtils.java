package me.mixces.animatium.util;

import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class EntityUtils {
    private static final Map<EntityRenderState, Entity> STATE_TO_ENTITY = new HashMap<>();

    public static void setEntityByState(EntityRenderState state, Entity entity) {
        STATE_TO_ENTITY.put(state, entity);
    }

    public static Optional<Entity> getEntityByState(EntityRenderState state) {
        return Optional.ofNullable(STATE_TO_ENTITY.getOrDefault(state, null));
    }
}
