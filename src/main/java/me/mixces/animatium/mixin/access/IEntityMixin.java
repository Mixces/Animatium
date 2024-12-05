package me.mixces.animatium.mixin.access;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface IEntityMixin {

    @Invoker
    Vec3d invokeGetVelocity();

    @Invoker
    boolean invokeIsOnGround();
}
