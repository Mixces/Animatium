package me.mixces.animatium.util

interface ViewBobbingStorage {
    fun `animatium$setBobbingTilt`(bobbingTilt: Float)

    fun `animatium$setPreviousBobbingTilt`(previousBobbingTilt: Float)

    fun `animatium$getBobbingTilt`(): Float

    fun `animatium$getPreviousBobbingTilt`(): Float
}