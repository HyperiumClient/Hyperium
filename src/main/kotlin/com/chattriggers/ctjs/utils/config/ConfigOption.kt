package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.gui.GuiButton

abstract class ConfigOption {
    var name: String? = null

    protected var x: Int = 0
    protected var y: Int = 0
    var hidden: Boolean = false

    internal var resetButton = GuiButton(
            0,
            Renderer.screen.getWidth() / 2 - 100 + this.x + 185,
            this.y - 2,
            14, 12, ""
    )

    open fun init() {
        this.resetButton = GuiButton(0,
                Renderer.screen.getWidth() / 2 - 100 + this.x + 185, this.y - 2,
                14, 12, "")
    }

    open fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        //#if MC<=10809
        this.resetButton.xPosition = Renderer.screen.getWidth() / 2 - 100 + this.x + 185
        //#else
        //$$ this.resetButton.x = Renderer.screen.getWidth() / 2 - 100 + this.x + 185;
        //#endif

        //#if MC<=10809
        this.resetButton.drawButton(Client.getMinecraft(), mouseX, mouseY)
        //#else
        //$$ this.resetButton.drawButton(Client.getMinecraft(), mouseX, mouseY, partialTicks);
        //#endif

        Renderer.text("\u21BA", (Renderer.screen.getWidth() / 2 - 100 + this.x + 189).toFloat(), (this.y - 4).toFloat())
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