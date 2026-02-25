/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2025 lowercasebtw
 * Copyright (C) 2024-2025 mixces
 * Copyright (C) 2024-2025 Contributors to the project retain their copyright
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * "MINECRAFT" LINKING EXCEPTION TO THE GPL
 */

package org.visuals.legacy.animatium.mixins.v1.entity.armor_hurt;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@IfModAbsent("ichor")
@Mixin(EquipmentLayerRenderer.class)
public abstract class MixinEquipmentLayerRenderer {
	@WrapOperation(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/ResourceLocation;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;armorCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
	private RenderType animatium$renderLayerArmorTint(ResourceLocation resourceLocation, Operation<RenderType> original) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().other.entityArmorHurtTint) {
			return RenderType.entityCutoutNoCullZOffset(resourceLocation);
		} else {
			return original.call(resourceLocation);
		}
	}

	@WrapOperation(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/ResourceLocation;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Sheets;armorTrimsSheet(Z)Lnet/minecraft/client/renderer/RenderType;"))
	private RenderType animatium$renderLayerArmorTrimTint(boolean bl, Operation<RenderType> original, @Local TextureAtlasSprite textureAtlasSprite) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().other.entityArmorHurtTint) {
			return RenderType.entityCutoutNoCullZOffset(textureAtlasSprite.atlasLocation());
		} else {
			return original.call(bl);
		}
	}

	@ModifyArg(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/ResourceLocation;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"), index = 5)
	private <S> int animatium$modifyUVArmorTint(int original, @Local(argsOnly = true) S entityRenderState) {
		return animatium$getPackUv(original, (EntityRenderState) entityRenderState);
	}

	@ModifyArg(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/ResourceLocation;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"), index = 5)
	private <S> int animatium$modifyUVTrimTint(int original, @Local(argsOnly = true) S entityRenderState) {
		return animatium$getPackUv(original, (EntityRenderState) entityRenderState);
	}

	@WrapOperation(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/ResourceLocation;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
	private <S> void animatium$armorHurtRendering(OrderedSubmitNodeCollector instance, Model<? super S> model, S renderState, PoseStack poseStack, RenderType renderType, int light, int overlay, int color, TextureAtlasSprite textureAtlasSprite, int i, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original) {
		original.call(instance, model, renderState, poseStack, renderType, light, overlay, color, textureAtlasSprite, i, crumblingOverlay);
		if (Animatium.isEnabled() &&
				AnimatiumConfig.instance().other.entityArmorHurtTint &&
				AnimatiumConfig.instance().other.armorHurtRendering &&
				renderState instanceof LivingEntityRenderState livingEntityRenderState) {
			final boolean isHurt = livingEntityRenderState.hasRedOverlay;
			if (isHurt) {
				// TODO: Check if this code even does anything at all
				// TODO: Too strong? & glint needs to be tinted hurt color
				GlStateManager._enableBlend();
				GlStateManager._blendFuncSeparate(GlConst.toGl(SourceFactor.SRC_ALPHA), GlConst.toGl(DestFactor.ONE_MINUS_SRC_ALPHA), GlConst.toGl(SourceFactor.SRC_ALPHA), GlConst.toGl(DestFactor.ONE_MINUS_SRC_ALPHA));
				original.call(instance, model, renderState, poseStack, renderType, light, overlay, color, textureAtlasSprite, i, crumblingOverlay);
				GlStateManager._disableBlend();
			}
		}
	}

	@Unique
	private int animatium$getPackUv(final int original, final EntityRenderState entityRenderState) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().other.entityArmorHurtTint && entityRenderState instanceof LivingEntityRenderState livingEntityRenderState) {
			return OverlayTexture.pack(OverlayTexture.u(0.0F), OverlayTexture.v(livingEntityRenderState.hasRedOverlay));
		} else {
			return original;
		}
	}
}