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
(private val prop: KMutableProperty<String>, name: String = "", x: Int = 0, y: Int = 0)
    : ConfigOption() {

    private var value: String by Delegates.observable(prop.getter.call(Config)) { _, _, new ->
        prop.setter.call(Config, new)
    }
    private val initial = value

    private var textField: GuiTextField? = null
    private var systemTime: Long = 0
    private var isValid: Boolean = false
    var isDirectory: Boolean

    private val isValidColor: String
        get() = if (this.isValid) ChatLib.addColor("&a") else ChatLib.addColor("&c")

    init {
        this.name = name

        this.x = x
        this.y = y
        this.systemTime = Client.getSystemTime()
        this.isValid = true
        this.isDirectory = false
    }

    private fun updateValidDirectory(directory: String) {
        this.isValid = !this.isDirectory || File(directory).isDirectory
    }

    override fun init() {
        super.init()

        updateValidDirectory(this.value)
        this.textField = GuiTextField(0, Renderer.getFontRenderer(),
                Renderer.screen.getWidth() / 2 - 100 + this.x, this.y + 15,
                200, 20)
        this.textField?.maxStringLength = 100
        this.textField?.text = isValidColor + this.value
    }

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (this.hidden) return

        update()

        val middle = Renderer.screen.getWidth() / 2

        Rectangle(-0x80000000, (middle - 105 + this.x).toFloat(), (this.y - 5).toFloat(), 210f, 45f)
                .setShadow(-0x30000000, 3f, 3f)
                .draw()
        Text(this.name!!, (middle - 100 + this.x).toFloat(), this.y.toFloat()).draw()

        //#if MC<=10809
        this.textField!!.xPosition = middle - 100 + this.x
        //#else
        //$$ this.textField!!.x = middle - 100 + this.x
        //#endif

        this.textField!!.drawTextBox()

        super.draw(mouseX, mouseY, partialTicks)
    }

    private fun update() {
        while (this.systemTime < Client.getSystemTime() + 50) {
            this.systemTime += 50
            this.textField!!.updateCursorCounter()
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        if (this.hidden) return

        this.textField!!.mouseClicked(mouseX, mouseY, 0)

        if (this.resetButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value = this.initial
            this.textField!!.text = isValidColor + this.value
            this.resetButton.playPressSound(Client.getMinecraft().soundHandler)
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (this.hidden) return

        if (this.textField!!.isFocused) {
            this.textField!!.textboxKeyTyped(typedChar, keyCode)

            val text = ChatLib.removeFormatting(this.textField!!.text)
            updateValidDirectory(text)
            this.textField!!.text = isValidColor + text

            if (this.isValid)
                this.value = text
        }
    }
}