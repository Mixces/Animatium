package org.visuals.legacy.animatium.mixins.v1.rendering.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.WeightedVariants;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

import java.util.List;

@Mixin(WeightedVariants.Unbaked.class)
public abstract class MixinWeightedVariantsUnbaked_DisableRandomBlockRotations {
	@WrapOperation(method = "bake", at = @At(value = "NEW", target = "(Lnet/minecraft/util/random/WeightedList;)Lnet/minecraft/client/resources/model/WeightedVariants;"))
	private WeightedVariants animatium$disableRandomBlockRotations(final WeightedList<?> list, final Operation<WeightedVariants> original) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().other.disableRandomBlockRotations) {
			final List<? extends Weighted<?>> items = list.unwrap();
			return new WeightedVariants(WeightedList.of(List.of((Weighted<BlockStateModel>) items.getFirst())));
		} else {
			return original.call(list);
		}
	}
}
