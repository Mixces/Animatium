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

package org.visuals.legacy.animatium.mixins.v1.gui.screen_tweaks;

import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@IfModAbsent(value = "viafabricplus")
@Mixin(Font.PreparedTextBuilder.class)
public abstract class MixinFontPreparedTextBuilder_FixStrikethroughStyle {
    @Unique
    private static final float animatium$strikethroughOffset = 0.5F;

    @ModifyArg(method = "accept(ILnet/minecraft/network/chat/Style;Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/font/glyphs/EffectGlyph;createEffect(FFFFFIIF)Lnet/minecraft/client/gui/font/TextRenderable;", ordinal = 0), index = 1)
    private float animatium$fixTextStrikethroughStyle$minY(final float minY) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().fixes.fixTextStrikethroughStyle) {
            return minY - animatium$strikethroughOffset;
        } else {
            return minY;
        }
    }

    @ModifyArg(method = "accept(ILnet/minecraft/network/chat/Style;Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/font/glyphs/EffectGlyph;createEffect(FFFFFIIF)Lnet/minecraft/client/gui/font/TextRenderable;", ordinal = 0), index = 3)
    private float animatium$fixTextStrikethroughStyle$maxY(final float maxY) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().fixes.fixTextStrikethroughStyle) {
            return maxY - animatium$strikethroughOffset;
        } else {
            return maxY;
        }
    }
}
