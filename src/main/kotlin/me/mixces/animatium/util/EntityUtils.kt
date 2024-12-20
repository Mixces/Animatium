package me.mixces.animatium.util

import net.minecraft.client.render.entity.state.EntityRenderState
import net.minecraft.entity.Entity
import java.util.Optional

abstract class EntityUtils {
    companion object {
        val STATE_TO_ENTITY = hashMapOf<EntityRenderState, Entity>()

        @JvmStatic
        fun setEntityByState(state: EntityRenderState, entity: Entity) {
            STATE_TO_ENTITY.put(state, entity)
        }

        @JvmStatic
        fun getEntityByState(state: EntityRenderState): Optional<Entity> {
            return Optional.ofNullable(STATE_TO_ENTITY.getOrDefault(state, null))
        }
    }
}