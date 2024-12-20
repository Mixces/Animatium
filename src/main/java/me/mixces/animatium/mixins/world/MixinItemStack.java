package me.mixces.animatium.mixins.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @WrapOperation(method = "getFormattedName", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getRarity()Lnet/minecraft/util/Rarity;"))
    private Rarity animatium$oldItemRarities$getFormattedName(ItemStack instance, Operation<Rarity> original) {
        if (AnimatiumConfig.getInstance().oldItemRarities) {
            return ItemUtils.getOldItemRarity((ItemStack) (Object) this);
        } else {
            return original.call(instance);
        }
    }

    @WrapOperation(method = "toHoverableText", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getRarity()Lnet/minecraft/util/Rarity;"))
    private Rarity animatium$oldItemRarities$toHoverableText(ItemStack instance, Operation<Rarity> original) {
        if (AnimatiumConfig.getInstance().oldItemRarities) {
            return ItemUtils.getOldItemRarity((ItemStack) (Object) this);
        } else {
            return original.call(instance);
        }
    }
}
