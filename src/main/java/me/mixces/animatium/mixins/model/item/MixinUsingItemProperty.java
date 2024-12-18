package me.mixces.animatium.mixins.model.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.render.item.property.bool.UsingItemProperty;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(UsingItemProperty.class)
public abstract class MixinUsingItemProperty {
    @ModifyReturnValue(method = "getValue", at = @At(value = "RETURN"))
    private boolean animatium$getValue(boolean original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) ModelTransformationMode modelTransformationMode) {
        if (AnimatiumConfig.getInstance().disableItemUsingTextureInGui &&
                (stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem) &&
                modelTransformationMode == ModelTransformationMode.GUI) {
            return false;
        } else {
            return original;
        }
    }
}
