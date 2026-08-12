package org.visuals.legacy.animatium.handler.config.bundle

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import org.visuals.legacy.animatium.config.category.Category
import java.awt.Color
import java.util.function.BiConsumer

abstract class Bundle {
    abstract fun install(builder: ConfigCategory.Builder, defaults: Category, config: Category)

    abstract fun booleanEntry(name: String, listener: BiConsumer<Option<Boolean>, Boolean>): Bundle

    fun booleanEntry(name: String) = this.booleanEntry(name) { opt, value -> }

    abstract fun intRange(name: String, min: Int, max: Int, step: Int): Bundle

    fun intRange(name: String, min: Int, max: Int) = this.intRange(name, min, max, 1)

    abstract fun floatRange(name: String, min: Float, max: Float, step: Float): Bundle

    fun floatRange(name: String, min: Float, max: Float) = this.floatRange(name, min, max, 0.1F)

    abstract fun <S : Enum<S>> enumEntry(name: String, enumClazz: Class<S>, listener: BiConsumer<Option<Enum<S>>, Enum<S>>): Bundle

    fun <S : Enum<S>> enumEntry(name: String, enumClazz: Class<S>) = this.enumEntry(name, enumClazz) { opt, value -> }

    abstract fun colorEntry(name: String, listener: BiConsumer<Option<Color>, Color>): Bundle

    fun colorEntry(name: String) = this.colorEntry(name) { opt, value -> }
}