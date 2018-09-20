package com.chattriggers.ctjs.minecraft.objects.display

import cc.hyperium.event.InvokeEvent
import cc.hyperium.event.RenderHUDEvent
import com.chattriggers.ctjs.utils.kotlin.KotlinListener
import net.minecraft.client.renderer.GlStateManager

@KotlinListener
object DisplayHandler {
    private var displays = mutableListOf<Display>()

    fun registerDisplay(display: Display) = this.displays.add(display)
    fun clearDisplays() = this.displays.clear()

    @InvokeEvent
    fun renderDisplays(event: RenderHUDEvent) {
        GlStateManager.pushMatrix()
        this.displays.forEach(Display::render)
        GlStateManager.popMatrix()
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