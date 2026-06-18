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

package org.visuals.legacy.animatium.config.category;

import dev.isxander.yacl3.api.ConfigCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.animatium.mixins.accessor.GameRendererAccessor;
import org.visuals.legacy.animatium.util.compatibility.Mods;
import org.visuals.legacy.animatium.util.config.EntryBundle;
import org.visuals.legacy.animatium.util.enums.DamageTintSetting;
import org.visuals.legacy.animatium.util.enums.VoidFogSetting;
import org.visuals.legacy.animatium.util.rendering.lighting.LegacyDiffuseLighting;

public final class OtherConfigCategory extends Category {
    // Sky
    public boolean blueVoidSky = false;
    public boolean planarSkyFog = false;
    public boolean cloudHeight = false;
    public boolean playerVoidBox = false;
    // Other
    public boolean thirdPersonSwordBlockingPosition = false;
    public boolean lockBlockingArmRotation = false;
    public boolean projectileAgeCheck = false;
    public boolean blockMiningProgress = false;
    public boolean disableInventoryEntityScissor = false;
    public boolean blockOutlineRendering = false;
    public boolean disableModelWhilstSleeping = false;
    public boolean damageTintArmor = false;
    public DamageTintSetting damageTintStyle = DamageTintSetting.VANILLA;
    public boolean itemGlintOnEntity = false;
    public boolean maxGlintProperties = false;
    public boolean restoreParticleBlending = false;
    public boolean heldItemArmLogic = false;
    public boolean flameDimensions = false;
    public boolean flameOffset = false;
    public boolean persistentBlockOutline = false;
    public boolean oldCloudRendering = false;
    public boolean fastGrass = false;
    public boolean oldY0Height = false;
    public boolean oldWaterOverlayOpacity = false;
    public boolean oldWaterColorFog = false;
    public boolean disableRandomBlockRotations = false;
    public boolean legacyDiffuseLighting = false;
    public boolean legacyLightmap = false;
    public boolean legacyFogDarkness = false;
    public boolean legacySplashPosition = false;
    public VoidFogSetting voidFog = VoidFogSetting.OFF;

    public static ConfigCategory create(final OtherConfigCategory defaults, final OtherConfigCategory config) {
        final ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.other"));
        config.bundle().install(category, defaults, config);
        return category.build();
    }

    @Override
    public EntryBundle bundle() {
        final EntryBundle bundle = new EntryBundle(this, "other");

        bundle.group((EntryBundle.Group) new EntryBundle.Group("sky")
                .booleanEntry("blueVoidSky")
                .booleanEntry("planarSkyFog")
                .booleanEntry("cloudHeight")
                .booleanEntry("playerVoidBox"));

        bundle.booleanEntry("thirdPersonSwordBlockingPosition");
        bundle.booleanEntry("lockBlockingArmRotation");
        bundle.booleanEntry("projectileAgeCheck");
        bundle.booleanEntry("blockMiningProgress");
        bundle.booleanEntry("disableInventoryEntityScissor");
        bundle.booleanEntry("blockOutlineRendering");
        bundle.booleanEntry("disableModelWhilstSleeping");
        if (!Mods.HAS_LUNAR_CLIENT) {
            bundle.booleanEntry("damageTintArmor");
        }

        bundle.enumEntry("damageTintStyle", DamageTintSetting.class, (option, event) -> ((GameRendererAccessor) Minecraft.getInstance().gameRenderer).animatium$setOverlayTexture(new OverlayTexture()));
        bundle.booleanEntry("itemGlintOnEntity");
        bundle.booleanEntry("maxGlintProperties");
        bundle.booleanEntry("restoreParticleBlending");
        bundle.booleanEntry("heldItemArmLogic");
        bundle.booleanEntry("flameDimensions");
        bundle.booleanEntry("flameOffset");
        bundle.booleanEntry("persistentBlockOutline");
        bundle.booleanEntry("oldCloudRendering");
        bundle.booleanEntry("fastGrass");
        bundle.booleanEntry("oldY0Height");
        bundle.booleanEntry("oldWaterOverlayOpacity");
        bundle.booleanEntry("oldWaterColorFog");
        bundle.booleanEntry("disableRandomBlockRotations");
        bundle.booleanEntry("legacyDiffuseLighting", (option, value) -> LegacyDiffuseLighting.refresh());
        bundle.booleanEntry("legacyLightmap");
        bundle.booleanEntry("legacyFogDarkness");
        bundle.booleanEntry("legacySplashPosition");
        bundle.enumEntry("voidFog", VoidFogSetting.class);

        return bundle;
    }
}
