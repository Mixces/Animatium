/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2027 lowercasebtw
 * Copyright (C) 2024-2027 mixces
 * Copyright (C) 2024-2027 Contributors to the project retain their copyright
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

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.textures.GpuTexture;
import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.UvMapping;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.handler.rendering.pipeline.AnimatiumRenderTypes;

@IfModAbsent("ichor")
@Mixin(EquipmentLayerRenderer.class)
public abstract class MixinEquipmentLayerRenderer_DamageTintArmor {
    @Unique
    private static final String RENDER_LAYERS_TARGET = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V";

    @Unique
    private static final int animatium$DAMAGE_UV = 196608;

    @WrapOperation(method = RENDER_LAYERS_TARGET, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;armorCutoutNoCull(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private <S> RenderType animatium$renderLayerArmorTint(final Identifier texture, final Operation<RenderType> original, @Local(argsOnly = true, ordinal = 0) final S state) {
        if (this.animatium$isArmorHurt(state) && this.animatium$isVanillaProportions(texture)) {
            return RenderTypes.entityCutoutZOffset(texture);
        } else {
            return original.call(texture);
        }
    }

    @WrapOperation(method = RENDER_LAYERS_TARGET, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;armorTrim(Lnet/minecraft/resources/Identifier;Z)Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private <S> RenderType animatium$renderLayerArmorTrimTint(final Identifier texture, final boolean decal, final Operation<RenderType> original, @Local(ordinal = 0) final TextureAtlasSprite sprite, @Local(argsOnly = true, ordinal = 0) final S state) {
        if (this.animatium$isArmorHurt(state) && !decal) {
            return RenderTypes.entityCutoutZOffset(sprite.atlasLocation());
        } else {
            return original.call(texture, decal);
        }
    }

    @WrapOperation(method = RENDER_LAYERS_TARGET, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;armorCutoutNoCullGlint(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private <S> RenderType animatium$disableVanillaGlint(final Identifier texture, final Operation<RenderType> original, @Local(argsOnly = true, ordinal = 0) final S state) {
        if (Animatium.isEnabled() &&
                AnimatiumConfig.instance().other.damageTintArmor &&
                AnimatiumConfig.instance().other.glintAffectsArmorTint &&
                !Minecraft.getInstance().options.improvedTransparency().get()
        ) {
            return RenderTypes.entityCutoutZOffset(texture);
        } else {
            return original.call(texture);
        }
    }

    @WrapOperation(method = RENDER_LAYERS_TARGET, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/UvMapping;I)V", ordinal = 0))
    private <S> void animatium$useOverlayArmorGlint(final OrderedSubmitNodeCollector instance, final Model<? super S> model, final S state, final PoseStack poseStack, final RenderType renderType, final int lightCoords, final int overlayCoords, final int color, final @Nullable UvMapping uvMapping, final int outlineColor, final Operation<Void> original, @Local(name = "renderShaderGlint") final boolean renderShaderGlint) {
        original.call(instance, model, state, poseStack, renderType, lightCoords, overlayCoords, color, uvMapping, outlineColor);
        if (Animatium.isEnabled() &&
                AnimatiumConfig.instance().other.damageTintArmor &&
                AnimatiumConfig.instance().other.glintAffectsArmorTint &&
                !Minecraft.getInstance().options.improvedTransparency().get() &&
                renderShaderGlint) {
            original.call(instance, model, state, poseStack, AnimatiumRenderTypes.ARMOR_GLINT, lightCoords, overlayCoords, color, uvMapping, outlineColor);
        }
    }

    @ModifyExpressionValue(method = RENDER_LAYERS_TARGET, at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;NO_OVERLAY:I", opcode = Opcodes.GETSTATIC))
    private <S> int animatium$applyOverlayUV(final int original, @Local(argsOnly = true, ordinal = 0) final S state) {
        return this.animatium$isArmorHurt(state) ? animatium$DAMAGE_UV : original;
    }

    @Unique
    private <S> boolean animatium$isArmorHurt(final S state) {
        return Animatium.isEnabled() &&
                AnimatiumConfig.instance().other.damageTintArmor &&
                state instanceof LivingEntityRenderState livingEntityRenderState &&
                livingEntityRenderState.hasRedOverlay;
    }

    // Patches (https://github.com/Legacy-Visuals-Project/Animatium/issues/76) temporarily
    // Prevents damage tint on armor if the respective armor texture proportions do not match vanilla.
    // TODO/Revisit to patch better/nicer as it currently disables the overlay entirely which is not ideal
    @Unique
    private boolean animatium$isVanillaProportions(final Identifier location) {
        final GpuTexture texture = Minecraft.getInstance().getTextureManager().getTexture(location).getTexture();
        return texture.getWidth(0) == texture.getHeight(0) * 2;
    }
}