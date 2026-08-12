package org.visuals.legacy.animatium.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.toasts.SystemToast
import net.minecraft.network.chat.Component

object ToastUtil {
    fun send(message: Component) {
        Minecraft.getInstance().gui.toastManager().addToast(
            SystemToast(
                SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                Component.literal("Animatium"),
                message
            )
        )
    }

    fun send(message: String) = this.send(Component.literal(message))
}