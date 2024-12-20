package me.mixces.animatium.util

abstract class MathUtils {
    companion object {
        @JvmStatic
        fun toRadians(angle: Float): Float {
            return angle * Math.PI.toFloat() / 180F
        }

        @JvmStatic
        fun toAngle(radians: Float): Float {
            return radians / Math.PI.toFloat() * 180F
        }
    }
}