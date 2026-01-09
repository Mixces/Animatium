package org.visuals.legacy.animatium.mixins.v1.entity.items;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinItemInHandRenderer_HeldItemArmLogic {
	@Inject(method = "renderPlayerArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/ClientAsset$Texture;texturePath()Lnet/minecraft/resources/ResourceLocation;", shift = At.Shift.AFTER))
	private void animatium$extractArmState(final PoseStack poseStack, final SubmitNodeCollector nodeCollector, final int packedLight, final float equippedProgress, final float swingProgress, final HumanoidArm arm, final CallbackInfo ci, @Local AvatarRenderer<AbstractClientPlayer> avatarRenderer) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().other.heldItemArmLogic) {
			final Minecraft minecraft = Minecraft.getInstance();
			avatarRenderer.extractRenderState(minecraft.player, new AvatarRenderState(), minecraft.getDeltaTracker().getGameTimeDeltaTicks());
		}
	}
}
