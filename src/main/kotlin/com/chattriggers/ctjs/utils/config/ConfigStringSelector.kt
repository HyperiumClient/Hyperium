package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.engine.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.renderer.Rectangle
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.gui.GuiButton
import kotlin.properties.Delegates
import kotlin.reflect.KMutableProperty

open class ConfigStringSelector
    (
    private val prop: KMutableProperty<String>,
    name: String = "",
    private val values: Array<String> = emptyArray(),
    x: Int = 0,
    y: Int = 0
) : ConfigOption() {

    private var value: Int by Delegates.observable(
        values.indexOf(prop.getter.call(Config))
    ) { _, _, new ->
        prop.setter.call(Config, values[new])
    }
    private val initial = value

    private var leftArrowButton: GuiButton? = null
    @Transient
    private var rightArrowButton: GuiButton? = null

    init {
        this.name = name

        this.x = x
        this.y = y
    }

    fun getValue(): String {
        try {
            return values[value]
        } catch (exception: IndexOutOfBoundsException) {
            if (values.isNotEmpty())
                return values[0]
            else
                ModuleManager.generalConsole.printStackTrace(exception)
        }

        return ""
    }

    override fun init() {
        super.init()

        leftArrowButton = GuiButton(
            0,
            Renderer.Screen.getWidth() / 2 - 100 + x,
            y + 15,
            30,
            20,
            "<"
        )

        rightArrowButton = GuiButton(
            0,
            Renderer.Screen.getWidth() / 2 + 70 + x,
            y + 15,
            30,
            20,
            ">"
        )
    }

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (hidden) return

        val middle = Renderer.Screen.getWidth() / 2

        Rectangle(-0x80000000, (middle - 105 + x).toFloat(), (y - 5).toFloat(), 210f, 45f)
            .setShadow(-0x30000000, 3f, 3f)
            .draw()
        Text(name!!, (middle - 100 + x).toFloat(), y.toFloat()).draw()

        Text(
            getValue(),
            (middle + x - Renderer.getStringWidth(getValue()) / 2).toFloat(),
            (y + 20).toFloat()
        ).draw()

        leftArrowButton!!.xPosition = middle - 100 + x
        rightArrowButton!!.xPosition = middle + 70 + x

        leftArrowButton!!.drawButton(Client.getMinecraft(), mouseX, mouseY)
        rightArrowButton!!.drawButton(Client.getMinecraft(), mouseX, mouseY)

        super.draw(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        if (hidden) return

        if (leftArrowButton!!.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            if (value - 1 < 0) value = values.size - 1
            else value--

            leftArrowButton!!.playPressSound(Client.getMinecraft().soundHandler)
        } else if (rightArrowButton!!.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            if (value + 1 >= values.size) value = 0
            else value++

            rightArrowButton!!.playPressSound(Client.getMinecraft().soundHandler)
        }

        if (resetButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            value = initial
            resetButton.playPressSound(Client.getMinecraft().soundHandler)
        }
    }
}

class ConsoleThemeSelector
    (prop: KMutableProperty<String>, name: String = "", x: Int = 0, y: Int = 0) : ConfigStringSelector(
    prop, name,
    arrayOf(
        "default.dark",
        "ashes.dark",
        "atelierforest.dark",
        "isotope.dark",
        "codeschool.dark",
        "gotham",
        "hybrid",
        "3024.light",
        "chalk.light",
        "blue",
        "slate",
        "red",
        "green",
        "aids"
    ),
    x, y
) {
    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        hidden = Config.customTheme

        super.draw(mouseX, mouseY, partialTicks)
    }
}
