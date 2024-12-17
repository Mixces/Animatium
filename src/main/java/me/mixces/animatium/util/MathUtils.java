package me.mixces.animatium.util;

public abstract class MathUtils {
    public static float toRadians(float angle) {
        return angle * (float) Math.PI / 180F;
    }

    public static float toAngle(float radians) {
        return radians / (float) Math.PI * 180F;
    }
}
