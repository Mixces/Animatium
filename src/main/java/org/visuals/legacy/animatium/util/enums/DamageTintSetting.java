package org.visuals.legacy.animatium.util.enums;

import net.minecraft.util.ARGB;

import java.util.function.Function;

public enum DamageTintSetting {
    V1_7(brightness -> ARGB.colorFromFloat(0.4F, brightness, 0.0F, 0.0F)),
    V1_8(ARGB.colorFromFloat(0.3F, 1.0F, 0.0F, 0.0F)),
    V1_8_ORANGE_MARSHALL(ARGB.colorFromFloat(0.5F, 1.0F, 0.0F, 0.0F)),
    MODERN(-1); // Doesn't matter, any code will fall out if the setting is set to this (NOTE: 1.15 was when the alpha changed from 0.3F to 0.6980392156862745F)

    private final Function<Float, Integer> colorGetter;

    DamageTintSetting(final Function<Float, Integer> colorGetter) {
        this.colorGetter = colorGetter;
    }

    DamageTintSetting(final int color) {
        this((brightness) -> color);
    }

    public int getColor(final float brightness) {
        return this.colorGetter.apply(brightness);
    }
}
