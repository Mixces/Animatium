package me.mixces.animatium.util

abstract class MathUtils {
    companion object {
        fun toRadians(angle: Float): Float {
            return angle * Math.PI.toFloat() / 180F
        }

        fun toAngle(radians: Float): Float {
            return radians / Math.PI.toFloat() * 180F
        }
    }
}