package com.chattriggers.ctjs.minecraft.objects.display

import cc.hyperium.event.InvokeEvent
import cc.hyperium.event.render.RenderHUDEvent
import com.chattriggers.ctjs.utils.kotlin.KotlinListener
import org.lwjgl.opengl.GL11

@KotlinListener
object DisplayHandler {
    private var displays = mutableListOf<Display>()

    fun registerDisplay(display: Display) = displays.add(display)
    fun clearDisplays() = displays.clear()

    @InvokeEvent
    fun renderDisplays(event: RenderHUDEvent) {
        GL11.glPushMatrix()
        displays.forEach { it.render() }
        GL11.glPopMatrix()
    }

    enum class Background {
        NONE, FULL, PER_LINE;
    }

    enum class Align {
        NONE, LEFT, CENTER, RIGHT;
    }

    enum class Order {
        UP, DOWN;
    }
}
