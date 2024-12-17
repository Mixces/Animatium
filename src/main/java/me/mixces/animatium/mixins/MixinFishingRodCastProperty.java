package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.render.item.property.bool.FishingRodCastProperty;
import net.minecraft.item.ModelTransformationMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingRodCastProperty.class)
public abstract class MixinFishingRodCastProperty {
    @ModifyReturnValue(method = "getValue", at = @At(value = "RETURN", ordinal = 0))
    private boolean animatium$getValue(boolean original, @Local(argsOnly = true) ModelTransformationMode modelTransformationMode) {
        if (AnimatiumConfig.disableItemUsingTextureInGui && modelTransformationMode == ModelTransformationMode.GUI) {
            return false;
        } else {
            return AnimatiumConfig.oldFishingRodTextureStackCheck || original;
        }
    }
}
