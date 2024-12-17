package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.render.item.property.bool.FishingRodCastProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingRodCastProperty.class)
public abstract class MixinFishingRodCastProperty {
    @ModifyReturnValue(method = "getValue", at = @At(value = "RETURN", ordinal = 0))
    private boolean animatium$getValue(boolean original) {
        return AnimatiumConfig.oldFishingRodTextureStackCheck || original;
    }
}
