package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Rectangle
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.gui.GuiTextField
import java.io.File
import kotlin.properties.Delegates
import kotlin.reflect.KMutableProperty

class ConfigString
    (private val prop: KMutableProperty<String>, name: String = "", x: Int = 0, y: Int = 0) : ConfigOption() {

    private var value: String by Delegates.observable(prop.getter.call(Config)) { _, _, new ->
        prop.setter.call(Config, new)
    }
    private val initial = value

    private var textField: GuiTextField? = null
    private var systemTime: Long = 0
    private var isValid: Boolean = false
    private var isDirectory: Boolean

    private val isValidColor: String
        get() = if (isValid) ChatLib.addColor("&a") else ChatLib.addColor("&c")

    init {
        this.name = name

        this.x = x
        this.y = y
        systemTime = Client.getSystemTime()
        isValid = true
        isDirectory = false
    }

    private fun updateValidDirectory(directory: String) {
        isValid = !isDirectory || File(directory).isDirectory
    }

    override fun init() {
        super.init()

        updateValidDirectory(value)
        textField = GuiTextField(
            0, Renderer.getFontRenderer(),
            Renderer.Screen.getWidth() / 2 - 100 + x, y + 15,
            200, 20
        )
        textField?.maxStringLength = 100
        textField?.text = isValidColor + value
    }

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (hidden) return

        update()

        val middle = Renderer.Screen.getWidth() / 2

        Rectangle(-0x80000000, (middle - 105 + x).toFloat(), (y - 5).toFloat(), 210f, 45f)
            .setShadow(-0x30000000, 3f, 3f)
            .draw()
        Text(name!!, (middle - 100 + x).toFloat(), y.toFloat()).draw()
        textField!!.xPosition = middle - 100 + x
        textField!!.drawTextBox()

        super.draw(mouseX, mouseY, partialTicks)
    }

    private fun update() {
        while (systemTime < Client.getSystemTime() + 50) {
            systemTime += 50
            textField!!.updateCursorCounter()
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        if (hidden) return

        textField!!.mouseClicked(mouseX, mouseY, 0)

        if (resetButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            value = initial
            textField!!.text = isValidColor + value
            resetButton.playPressSound(Client.getMinecraft().soundHandler)
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (hidden) return

        if (textField!!.isFocused) {
            textField!!.textboxKeyTyped(typedChar, keyCode)

            val text = ChatLib.removeFormatting(textField!!.text)
            updateValidDirectory(text)
            textField!!.text = isValidColor + text

            if (isValid)
                value = text
        }
    }
}
