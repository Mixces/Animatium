package me.mixces.animatium.mixin;

import me.mixces.animatium.duck.EntityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityInterface {

    @Shadow
    public double prevX;

    @Shadow
    public double prevY;

    @Shadow
    public double prevZ;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

//    @ModifyReturnValue(
//            method = "getPose",
//            at = @At(
//                    value = "RETURN"
//            )
//    )
//    public EntityPose animatium$revertSwimPose(EntityPose original) {
//        return EntityPose.STANDING;
//    }

    @Override
    public Vec3d animatium$getCameraPosVec(float tickDelta, float eyeHeight) {
        final double d = MathHelper.lerp(tickDelta, prevX, getX());
        final double e = MathHelper.lerp(tickDelta, prevY, getY()) + (double) eyeHeight;
        final double f = MathHelper.lerp(tickDelta, prevZ, getZ());
        return new Vec3d(d, e, f);
    }
}
