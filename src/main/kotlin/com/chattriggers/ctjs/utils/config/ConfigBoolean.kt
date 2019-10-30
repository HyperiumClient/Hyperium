package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Rectangle
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.gui.GuiButton
import kotlin.properties.Delegates
import kotlin.reflect.KMutableProperty

class ConfigBoolean
    (private val prop: KMutableProperty<Boolean>, name: String = "", x: Int = 0, y: Int = 0) : ConfigOption() {

    private var value: Boolean by Delegates.observable(prop.getter.call(Config)) { _, _, new ->
        prop.setter.call(Config, new)
    }
    private val initial: Boolean = value

    init {
        this.name = name

        this.x = x
        this.y = y
    }

    private var button: GuiButton = GuiButton(
        0,
        Renderer.Screen.getWidth() / 2 - 100 + this.x,
        this.y + 15,
        stringValue
    )

    private val stringValue: String
        get() = if (value) ChatLib.addColor("&aTrue") else ChatLib.addColor("&cFalse")

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (hidden) return

        val middle = Renderer.Screen.getWidth() / 2

        Rectangle(-0x80000000, (middle - 105 + x).toFloat(), (y - 5).toFloat(), 210f, 45f)
            .setShadow(-0x30000000, 3f, 3f)
            .draw()

        Text(name!!, (middle - 100 + x).toFloat(), y.toFloat()).draw()

        button.xPosition = middle - 100 + x
        button.drawButton(Client.getMinecraft(), mouseX, mouseY)

        super.draw(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        if (hidden) return

        if (button.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            value = (!value)
            button.playPressSound(Client.getMinecraft().soundHandler)
        }

        if (resetButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            value = initial
            resetButton.playPressSound(Client.getMinecraft().soundHandler)
        }

        button.displayString = stringValue
    }
}
