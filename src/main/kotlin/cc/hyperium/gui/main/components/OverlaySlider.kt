package cc.hyperium.gui.main.components

import cc.hyperium.gui.main.HyperiumMainGui
import cc.hyperium.utils.RenderUtils
import org.lwjgl.input.Mouse
import java.util.function.Consumer

class OverlaySlider(label: String, private val minVal: Float, private val maxVal: Float, var value: Float, var update: Consumer<Float>, var round: Boolean) : OverlayLabel(label) {
    override fun handleMouseInput(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (mouseX >= overlayX + w - 105 && mouseX <= overlayX + w - 5 && mouseY >= overlayY && mouseY <= overlayY + h) {
            if (!Mouse.isButtonDown(0))
                return
            val fx = mouseX - (overlayX + w - 105)
            value = fx / 100F * (maxVal - minVal) + minVal
            update.accept(value)
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        super.mouseClicked(mouseX, mouseY, overlayX, overlayY, w, h)
    }

    override fun render(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int, overlayH: Int): Boolean {
        if (!super.render(mouseX, mouseY, overlayX, overlayY, w, h, overlayH))
            return false
        val left = (overlayX + w - 105).toFloat()

        val fr = HyperiumMainGui.INSTANCE.fr
        var s = value.toString()
        if (round)
            s = Math.round(value).toString()
        val toFloat = (overlayY + h / 2).toFloat()
        fr.drawString(s, left - 5 - fr.getWidth(s), toFloat - 5, 0xFFFFFFFF.toInt())
        val rightSide = (overlayX + w - 5).toFloat()
        RenderUtils.drawLine(left, toFloat, rightSide, (overlayY + h / 2).toFloat(), 2f, 0xFFFFFFFF.toInt())
        var d = (value - minVal) / (maxVal - minVal)*100
        var toInt = (left + d).toInt()
        RenderUtils.drawFilledCircle(toInt, overlayY + h / 2, 5f, 0xffffffff.toInt())
        return true
    }
}