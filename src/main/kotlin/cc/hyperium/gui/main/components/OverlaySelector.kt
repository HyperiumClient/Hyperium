package cc.hyperium.gui.main.components

import cc.hyperium.gui.main.HyperiumMainGui
import org.lwjgl.input.Mouse
import java.util.function.Consumer

class OverlaySelector<T>(label: String, var selected: T, val callback: Consumer<T>, val items: Array<T>) : OverlayLabel(label) {
    override fun render(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int, overlayH: Int): Boolean {
        if (!super.render(mouseX, mouseY, overlayX, overlayY, w, h, overlayH))
            return false
        val textY = overlayY + (h - HyperiumMainGui.getFr().FONT_HEIGHT) / 2
        HyperiumMainGui.getFr().drawString(selected.toString(), overlayX + w - HyperiumMainGui.getFr().getWidth(selected.toString()) - 5, textY.toFloat(), 0xffffff)
        return true
    }

    override fun handleMouseInput(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (Mouse.isButtonDown(0) && mouseX >= overlayX + w - 30 && mouseX <= overlayX + w - 5 && mouseY >= overlayY + 5 && mouseY <= overlayY + h - 5) {
            selected = if (items.indexOf(selected) == items.size - 1) items[0] else items[items.indexOf(selected) + 1]
            callback.accept(selected)
        }
    }
}