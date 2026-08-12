package org.visuals.legacy.animatium.handler.config.bundle

import org.visuals.legacy.animatium.config.AnimatiumConfig

object ConfigBundles {
    val MOVEMENT = AnimatiumConfig.instance().movement.bundle()
    val ITEMS = AnimatiumConfig.instance().items.bundle()
    val SCREEN = AnimatiumConfig.instance().screen.bundle()
    val FIXES = AnimatiumConfig.instance().fixes.bundle()
    val OTHER = AnimatiumConfig.instance().other.bundle()
    val EXTRAS = AnimatiumConfig.instance().extras.bundle()

    val ALL = arrayOf(MOVEMENT, ITEMS, SCREEN, FIXES, OTHER, EXTRAS)
}