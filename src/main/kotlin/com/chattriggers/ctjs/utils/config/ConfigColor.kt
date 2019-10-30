package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.minecraft.libs.MathLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Rectangle
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.gui.GuiButton
import java.awt.Color
import kotlin.properties.Delegates
import kotlin.reflect.KMutableProperty

open class ConfigColor
    (private val prop: KMutableProperty<Color>, name: String = "", x: Int = 0, y: Int = 0) : ConfigOption() {

    private var value: Color by Delegates.observable(prop.getter.call(Config)) { _, _, new ->
        prop.setter.call(Config, new)
    }
    private val initial = value

    init {
        this.name = name

        this.x = x
        this.y = y
    }


    private var redButton = GuiButton(
        0,
        MathLib.map(
            value.red.toFloat(), 0f, 255f,
            (Renderer.Screen.getWidth() / 2 - 100 + this.x).toFloat(),
            (Renderer.Screen.getWidth() / 2 + 52 + this.x).toFloat()
        ).toInt(),
        this.y + 15,
        5, 10, ""
    )


    private var greenButton = GuiButton(
        0,
        MathLib.map(
            value.green.toFloat(),
            0f,
            255f,
            (Renderer.Screen.getWidth() / 2 - 100 + this.x).toFloat(),
            (Renderer.Screen.getWidth() / 2 + 52 + this.x).toFloat()
        ).toInt(), this.y + 30,
        5, 10, ""
    )


    private var blueButton = GuiButton(
        0,
        MathLib.map(
            value.blue.toFloat(),
            0f,
            255f,
            (Renderer.Screen.getWidth() / 2 - 100 + this.x).toFloat(),
            (Renderer.Screen.getWidth() / 2 + 52 + this.x).toFloat()
        ).toInt(), this.y + 45,
        5, 10, ""
    )

    private var redHeld: Boolean = false
    private var blueHeld: Boolean = false
    private var greenHeld: Boolean = false

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (hidden) return

        val middle = Renderer.Screen.getWidth() / 2

        Rectangle(-0x80000000, (middle - 105 + x).toFloat(), (y - 5).toFloat(), 210f, 65f)
            .setShadow(-0x30000000, 3f, 3f)
            .draw()
        Text(name!!, (middle - 100 + x).toFloat(), y.toFloat()).draw()

        // red slider
        Rectangle(-0x560000, (middle - 100 + x).toFloat(), (y + 19).toFloat(), 155f, 3f)
            .setOutline(-0x1000000, 1f)
            .draw()

        redButton.xPosition = MathLib.map(
            value.red.toFloat(),
            0f,
            255f,
            (middle - 100 + x).toFloat(),
            (middle + 52 + x).toFloat()
        ).toInt()
        redButton.drawButton(Client.getMinecraft(), mouseX, mouseY)

        // green slider
        Rectangle(-0xff7800, (middle - 100 + x).toFloat(), (y + 34).toFloat(), 155f, 3f)
            .setOutline(-0x1000000, 1f)
            .draw()

        greenButton.xPosition = MathLib.map(
            value.green.toFloat(),
            0f,
            255f,
            (middle - 100 + x).toFloat(),
            (middle + 52 + x).toFloat()
        ).toInt()
        greenButton.drawButton(Client.getMinecraft(), mouseX, mouseY)

        // blue slider
        Rectangle(-0xffff34, (middle - 100 + x).toFloat(), (y + 49).toFloat(), 155f, 3f)
            .setOutline(-0x1000000, 1f)
            .draw()
        blueButton.xPosition = MathLib.map(
            value.blue.toFloat(),
            0f,
            255f,
            (middle - 100 + x).toFloat(),
            (middle + 52 + x).toFloat()
        ).toInt()
        blueButton.drawButton(Client.getMinecraft(), mouseX, mouseY)

        // color preview
        Rectangle(value.rgb, (middle + x + 60).toFloat(), (y + 15).toFloat(), 40f, 40f)
            .setOutline(-0x1000000, 1f)
            .draw()

        handleHeldButtons(mouseX, middle)

        super.draw(mouseX, mouseY, partialTicks)
    }

    private fun handleHeldButtons(mouseX: Int, middle: Int) {
        if (redHeld) {
            redButton.xPosition = mouseX - 1

            limitHeldButton(redButton)
            value = Color(
                MathLib.map(
                    redButton.xPosition.toFloat(),
                    (middle - 100 + x).toFloat(), (middle + 52 + x).toFloat(), 0f, 255f
                ).toInt(),
                value.green,
                value.blue
            )
        }
        if (greenHeld) {
            greenButton.xPosition = mouseX - 1

            limitHeldButton(greenButton)
            value = Color(
                value.red,
                MathLib.map(
                    greenButton.xPosition.toFloat(),
                    (middle - 100 + x).toFloat(), (middle + 52 + x).toFloat(), 0f, 255f
                ).toInt(),
                value.blue
            )
        }
        if (blueHeld) {
            blueButton.xPosition = mouseX - 1

            limitHeldButton(blueButton)
            value = Color(
                value.red,
                value.green,
                MathLib.map(
                    blueButton.xPosition.toFloat(),
                    (middle - 100 + x).toFloat(), (middle + 52 + x).toFloat(), 0f, 255f
                ).toInt()
            )
        }
    }

    private fun limitHeldButton(button: GuiButton) {
        if (button.xPosition < Renderer.Screen.getWidth() / 2 - 100 + x)
            button.xPosition = Renderer.Screen.getWidth() / 2 - 100 + x
        if (button.xPosition > Renderer.Screen.getWidth() / 2 + 52 + x)
            button.xPosition = Renderer.Screen.getWidth() / 2 + 52 + x
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        if (hidden) return

        if (redButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            redHeld = true
            redButton.playPressSound(Client.getMinecraft().soundHandler)
        }
        if (greenButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            greenHeld = true
            greenButton.playPressSound(Client.getMinecraft().soundHandler)
        }
        if (blueButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            blueHeld = true
            blueButton.playPressSound(Client.getMinecraft().soundHandler)
        }

        if (resetButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            value = initial
            val middle = Renderer.Screen.getWidth() / 2
            redButton.xPosition = MathLib.map(
                value.red.toFloat(),
                0f,
                255f,
                (middle - 100 + x).toFloat(),
                (middle + 52 + x).toFloat()
            ).toInt()
            greenButton.xPosition = MathLib.map(
                value.green.toFloat(),
                0f,
                255f,
                (middle - 100 + x).toFloat(),
                (middle + 52 + x).toFloat()
            ).toInt()
            blueButton.xPosition = MathLib.map(
                value.blue.toFloat(),
                0f,
                255f,
                (middle - 100 + x).toFloat(),
                (middle + 52 + x).toFloat()
            ).toInt()
            resetButton.playPressSound(Client.getMinecraft().soundHandler)
        }
    }

    override fun mouseReleased() {
        redHeld = false
        blueHeld = false
        greenHeld = false
    }
}

class SpecialConfigColor
    (prop: KMutableProperty<Color>, name: String = "", x: Int = 0, y: Int = 0) : ConfigColor(prop, name, x, y) {
    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        hidden = !Config.customTheme

        super.draw(mouseX, mouseY, partialTicks)
    }
}
