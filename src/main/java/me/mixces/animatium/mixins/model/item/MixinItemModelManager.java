package me.mixces.animatium.mixins.model.item;

import me.mixces.animatium.util.ItemUtils;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelManager.class)
public abstract class MixinItemModelManager {
    @Inject(method = "update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V", at = @At("HEAD"))
    private void animatium$setItemUtils(ItemRenderState renderState, ItemStack stack, ModelTransformationMode transformationMode, boolean leftHand, World world, LivingEntity entity, int seed, CallbackInfo ci) {
        ItemUtils.Companion.set(stack, transformationMode);
    }
}
