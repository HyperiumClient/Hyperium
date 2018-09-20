package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.engine.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.gui.GuiButton
import kotlin.properties.Delegates
import kotlin.reflect.KMutableProperty

open class ConfigStringSelector
(private val prop: KMutableProperty<String>, name: String = "", private val values: Array<String> = emptyArray(), x: Int = 0, y: Int = 0)
    : ConfigOption() {


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
            return this.values[this.value]
        } catch (exception: IndexOutOfBoundsException) {
            if (this.values.isNotEmpty())
                return this.values[0]
            else
                ModuleManager.generalConsole.printStackTrace(exception)
        }

        return ""
    }

    override fun init() {
        super.init()

        this.leftArrowButton = GuiButton(
                0,
                Renderer.screen.getWidth() / 2 - 100 + this.x,
                this.y + 15,
                30,
                20,
                "<"
        )

        this.rightArrowButton = GuiButton(
                0,
                Renderer.screen.getWidth() / 2 + 70 + this.x,
                this.y + 15,
                30,
                20,
                ">"
        )
    }

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (this.hidden) return

        val middle = Renderer.screen.getWidth() / 2

        Renderer.rectangle(-0x80000000, (middle - 105 + this.x).toFloat(), (this.y - 5).toFloat(), 210f, 45f)
                .setShadow(-0x30000000, 3f, 3f)
                .draw()
        Renderer.text(this.name!!, (middle - 100 + this.x).toFloat(), this.y.toFloat()).draw()

        Renderer.text(
                getValue(),
                (middle + this.x - Renderer.getStringWidth(getValue()) / 2).toFloat(),
                (this.y + 20).toFloat()
        ).draw()

        //#if MC<=10809
        this.leftArrowButton!!.xPosition = middle - 100 + this.x
        this.rightArrowButton!!.xPosition = middle + 70 + this.x

        this.leftArrowButton!!.drawButton(Client.getMinecraft(), mouseX, mouseY)
        this.rightArrowButton!!.drawButton(Client.getMinecraft(), mouseX, mouseY)
        //#else
        //$$ this.leftArrowButton!!.x = middle - 100 + this.x
        //$$ this.rightArrowButton!!.x = middle + 70 + this.x
        //$$
        //$$ this.leftArrowButton!!.drawButton(Client.getMinecraft(), mouseX, mouseY, partialTicks)
        //$$ this.rightArrowButton!!.drawButton(Client.getMinecraft(), mouseX, mouseY, partialTicks)
        //#endif

        super.draw(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        if (this.hidden) return

        if (this.leftArrowButton!!.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            if (this.value - 1 < 0) this.value = this.values.size - 1
            else this.value--

            this.leftArrowButton!!.playPressSound(Client.getMinecraft().soundHandler)
        } else if (this.rightArrowButton!!.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            if (this.value + 1 >= this.values.size) this.value = 0
            else this.value++

            this.rightArrowButton!!.playPressSound(Client.getMinecraft().soundHandler)
        }

        if (this.resetButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value = this.initial
            this.resetButton.playPressSound(Client.getMinecraft().soundHandler)
        }
    }
}

class ConsoleThemeSelector
(prop: KMutableProperty<String>, name: String = "", x: Int = 0, y: Int = 0)
    : ConfigStringSelector(prop, name,
        arrayOf("default.dark", "ashes.dark", "atelierforest.dark", "isotope.dark", "codeschool.dark", "gotham", "hybrid", "3024.light", "chalk.light", "blue", "slate", "red", "green", "aids"),
        x, y) {
    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        hidden = Config.customTheme

        super.draw(mouseX, mouseY, partialTicks)
    }
}