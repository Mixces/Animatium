package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownEntity.class)
public abstract class MixinThrownEntity {
    @ModifyExpressionValue(method = "shouldRender", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/projectile/thrown/ThrownEntity;age:I"))
    private int animatium$disableProjectileAgeCheck(int original) {
        return original + (AnimatiumConfig.disableProjectileAgeCheck ? 2 : 0);
    }
}
