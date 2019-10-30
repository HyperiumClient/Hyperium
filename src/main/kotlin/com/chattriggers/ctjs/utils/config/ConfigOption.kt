package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.gui.GuiButton

abstract class ConfigOption {
    var name: String? = null

    protected var x: Int = 0
    protected var y: Int = 0
    var hidden: Boolean = false

    internal var resetButton = GuiButton(
        0,
        Renderer.Screen.getWidth() / 2 - 100 + x + 185,
        y - 2,
        14, 12, ""
    )

    open fun init() {
        resetButton = GuiButton(
            0,
            Renderer.Screen.getWidth() / 2 - 100 + x + 185, y - 2,
            14, 12, ""
        )
    }

    open fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        resetButton.xPosition = Renderer.Screen.getWidth() / 2 - 100 + x + 185
        resetButton.drawButton(Client.getMinecraft(), mouseX, mouseY)

        Text("\u21BA", (Renderer.Screen.getWidth() / 2 - 100 + x + 189).toFloat(), (y - 4).toFloat())
            .setScale(2f)
            .setColor(-0x1)
            .setShadow(true)
            .draw()
    }

    abstract fun mouseClicked(mouseX: Int, mouseY: Int)
    open fun mouseReleased() {}
    open fun keyTyped(typedChar: Char, keyCode: Int) {}

    enum class Type {
        STRING, STRING_SELECTOR, COLOR, BOOLEAN
    }
}
