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
import org.visuals.legacy.animatium.util.enums.ServerFeature;

import java.util.Arrays;

public class ExtrasConfigCategory extends Category {
    public boolean minimalViewBobbing = false;
    public boolean showNameTagInThirdPerson = false;
    public boolean hideNameTagBackground = false;
    public boolean nameTagTextShadow = false;
    public boolean debugHudTextColor = false;
    public boolean offhandUsageSwinging = false;
    public boolean alwaysUsageSwing = false;
    public boolean alwaysSharpParticles = false;
    public boolean disableRecipeAndTutorialToasts = false;
    public boolean showArmWhileInvisible = false;
    // TODO 3.3: public boolean damageTintItems = false;
    // TODO 3.3: public boolean damageTintCape = false;
    public boolean fakeMissPenaltySwing = false;
    public boolean dontMoveBlueVoid = false;
    public boolean disableEntityDeathTopple = false;
    public boolean deepRedHurtTint = false;
    public boolean disableParticlePhysics = false;
    public boolean disableFirstPersonParticles = false;
    public boolean dontClearChat = false;
    public boolean dontCloseChat = false;
    public boolean oldWaterColorEffects = false;
    public boolean colorBoost = false;
    public boolean alwaysBlockingHeadCap = false;
    // Item Swing
    // TODO 3.3: public float itemSwingSpeed = 0.0F;
    // TODO 3.3: public float hasteSwingSpeed = 0.0F;
    // TODO 3.3: public float miningFatigueSwingSpeed = 0.0F;
    // TODO 3.3: public boolean ignoreHasteSpeed = false;
    // TODO 3.3: public boolean ignoreMiningFatigueSpeed = false;
    // Server Features (Singleplayer Only)
    public boolean miss_penalty = false;
    public boolean left_click_item_usage = false;
    public boolean mining_item_usage = false;
    public boolean hide_rod_bobber = false;
    public boolean pick_inflation = false;
    public boolean old_sneak_height = false;
    public boolean clientside_entities = false;
    public boolean disable_sprint_item_use = false;
    public boolean disable_sprint_sneaking = false;

    public static ConfigCategory create(final ExtrasConfigCategory defaults, final ExtrasConfigCategory config) {
        final ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.extras"));
        config.bundle().install(category, defaults, config);
        return category.build();
    }

    @Override
    public EntryBundle bundle() {
        final EntryBundle bundle = new EntryBundle(this, "extras");

        bundle.booleanEntry("minimalViewBobbing");
        bundle.booleanEntry("showNameTagInThirdPerson");
        bundle.booleanEntry("hideNameTagBackground");
        bundle.booleanEntry("nameTagTextShadow");
        bundle.booleanEntry("debugHudTextColor");
        bundle.booleanEntry("offhandUsageSwinging");
        bundle.booleanEntry("alwaysUsageSwing");
        bundle.booleanEntry("alwaysSharpParticles");
        if (!Mods.HAS_SODIUM_EXTRAS) {
            bundle.booleanEntry("disableRecipeAndTutorialToasts");
        }

        final Minecraft minecraft = Minecraft.getInstance();
        bundle.booleanEntry("showArmWhileInvisible");
        // TODO 3.3: bundle.booleanEntry("damageTintItems");
        // TODO 3.3: bundle.booleanEntry("damageTintCape");
        bundle.booleanEntry("fakeMissPenaltySwing");
        bundle.booleanEntry("dontMoveBlueVoid");
        bundle.booleanEntry("disableEntityDeathTopple");
        bundle.booleanEntry("deepRedHurtTint", (option, event) -> ((GameRendererAccessor) minecraft.gameRenderer).animatium$setOverlayTexture(new OverlayTexture()));
        bundle.booleanEntry("disableParticlePhysics");
        bundle.booleanEntry("disableFirstPersonParticles");
        bundle.booleanEntry("dontClearChat");
        bundle.booleanEntry("dontCloseChat");
        bundle.booleanEntry("oldWaterColorEffects", (option, event) -> minecraft.levelExtractor.allChanged());
        bundle.booleanEntry("colorBoost");
        bundle.booleanEntry("alwaysBlockingHeadCap");

//        bundle.group((EntryBundle.Group) new EntryBundle.Group("item_swing")
        // TODO 3.3: .floatEntry("itemSwingSpeed", -1.0F, 1.0F, 0.1F)
        // TODO 3.3: .floatEntry("hasteSwingSpeed", -1.0F, 1.0F, 0.1F)
        // TODO 3.3: .floatEntry("miningFatigueSwingSpeed", -1.0F, 1.0F, 0.1F)
        // TODO 3.3: .booleanEntry("ignoreHasteSpeed")
        // TODO 3.3: .booleanEntry("ignoreMiningFatigueSpeed"));

        {
            final EntryBundle.Group serverFeatureGroup = new EntryBundle.Group("server_features");
            Arrays.stream(ServerFeature.VALUES)
                    .filter(it -> it != ServerFeature.ALL)
                    .forEach(it -> serverFeatureGroup.booleanEntry(it.getName()));
            bundle.group(serverFeatureGroup);
        }

        return bundle;
    }
}
