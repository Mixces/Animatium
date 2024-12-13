package me.mixces.animatium.mixins.accessor;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(PlayerEntity.class)
public interface PlayerEntityAccessor {
    @Accessor("STANDING_DIMENSIONS")
    static EntityDimensions getStandingDimensions() {
        return null;
    }

    @Accessor("POSE_DIMENSIONS")
    static Map<EntityPose, EntityDimensions> getPoseDimensions() {
        return null;
    }
}
