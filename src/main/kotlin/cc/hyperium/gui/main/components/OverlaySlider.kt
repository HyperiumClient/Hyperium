package cc.hyperium.gui.main.components

import cc.hyperium.utils.RenderUtils
import org.lwjgl.input.Mouse

class OverlaySlider(label: String, private val minVal: Float, private val maxVal: Float, var value: Float) : OverlayLabel(label) {
    override fun handleMouseInput(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (Mouse.isButtonDown(0) && mouseX >= overlayX + w - 105 && mouseX <= overlayX + w - 5 && mouseY >= overlayY && mouseY <= overlayY + h) {
            val fx = mouseX - (overlayX + w - 105)
            value = fx / 100 * (maxVal - minVal) + minVal
        }
    }

    override fun render(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int, overlayH: Int): Boolean {
        if (!super.render(mouseX, mouseY, overlayX, overlayY, w, h, overlayH))
            return false
        RenderUtils.drawLine((overlayX + w - 105).toFloat(), (overlayY + h / 2).toFloat(), (overlayX + w - 5).toFloat(), (overlayY + h / 2).toFloat(), 2f, 0xFFFFFFFF.toInt())
        RenderUtils.drawFilledCircle((overlayY + w - 105 + (100 / (maxVal - minVal)) * value).toInt(), overlayY + h / 2, 5f, 0xffffffff.toInt())
        return true
    }
}