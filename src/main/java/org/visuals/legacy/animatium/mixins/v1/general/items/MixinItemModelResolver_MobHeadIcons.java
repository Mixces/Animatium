package org.visuals.legacy.animatium.mixins.v1.general.items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.AnimatiumConstants;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(ItemModelResolver.class)
public abstract class MixinItemModelResolver_MobHeadIcons {
    @WrapOperation(method = {"appendItemLayers", "shouldPlaySwapAnimation"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"))
    private Object animatium$mobHeadIcons(ItemStack instance, DataComponentType<?> dataComponentType, Operation<Object> original) {
        final ResourceLocation mobHeadLocation = AnimatiumConstants.getMobHeadLocation(instance.getItem());
        if (Animatium.ENABLED && AnimatiumConfig.instance().items.mobHeadIcons && mobHeadLocation != null) {
            return mobHeadLocation;
        } else {
            return original.call(instance, dataComponentType);
        }
    }
}
