package com.chattriggers.ctjs.minecraft.objects.display

import cc.hyperium.event.InvokeEvent
import cc.hyperium.event.RenderHUDEvent
import com.chattriggers.ctjs.utils.kotlin.KotlinListener
import org.lwjgl.opengl.GL11

@KotlinListener
object DisplayHandler {
    private var displays = mutableListOf<Display>()

    fun registerDisplay(display: Display) = this.displays.add(display)
    fun clearDisplays() = this.displays.clear()

    @InvokeEvent
    fun renderDisplays(event: RenderHUDEvent) {
        GL11.glPushMatrix()
        this.displays.forEach(Display::render)
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