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
 */

package btw.mixces.animatium.mixins.renderer;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.RenderUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.opengl.GlProgram;
import com.mojang.blaze3d.opengl.Uniform;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlProgram.class)
public abstract class MixinGlProgram {
    @Shadow
    public abstract @Nullable Uniform getUniform(String string);

    @Unique
    @Nullable
    private static Uniform animatium$GLINT_COLOR;

    @Inject(method = "setDefaultUniforms", at = @At("TAIL"))
    private void animatium$setupAndApplyCustomUniforms(VertexFormat.Mode mode, Matrix4f matrix4f, Matrix4f matrix4f2, float f, float g, CallbackInfo ci) {
        if (animatium$GLINT_COLOR == null) {
            animatium$GLINT_COLOR = this.getUniform("GlintColor");
        }

        if (animatium$GLINT_COLOR != null) {
            animatium$GLINT_COLOR.set(RenderUtils.getGlintColor());
        }
    }

    @WrapOperation(method = "setDefaultUniforms", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;getShaderLineWidth()F"), remap = false)
    private float animatium$blockOutlineRendering$lineWidth(Operation<Float> original) {
        return RenderUtils.getLineWidth(original.call());
    }

    @ModifyArg(method = "setDefaultUniforms", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/opengl/Uniform;set(F)V", ordinal = 0))
    private float animatium$forceMaxGlintStrength(float original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().maxGlintProperties) {
            // 100% glint strength
            return 1.0F;
        } else {
            return original;
        }
    }
}
