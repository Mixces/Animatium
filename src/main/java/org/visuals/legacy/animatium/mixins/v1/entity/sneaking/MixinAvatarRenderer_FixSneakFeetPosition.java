package org.visuals.legacy.animatium.mixins.v1.entity.sneaking;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(AvatarRenderer.class)
public abstract class MixinAvatarRenderer_FixSneakFeetPosition<AvatarLikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarLikeEntity, AvatarRenderState, PlayerModel> {
	public MixinAvatarRenderer_FixSneakFeetPosition(final EntityRendererProvider.Context context, final PlayerModel model, final float shadowRadius) {
		super(context, model, shadowRadius);
	}

	@WrapOperation(method = "getRenderOffset(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)Lnet/minecraft/world/phys/Vec3;", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;isCrouching:Z"))
	private boolean animatium$fixSneakingFeetPosition(AvatarRenderState instance, Operation<Boolean> original) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().fixes.fixSneakingFeetPosition) {
			return false;
		} else {
			return original.call(instance);
		}
	}
}
