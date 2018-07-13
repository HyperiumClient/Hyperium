package cc.hyperium.gui.main.components

import cc.hyperium.gui.main.HyperiumMainGui
import net.minecraft.client.gui.Gui
import java.awt.Color
import java.util.function.Consumer
import java.util.function.Supplier

class OverlaySelector<T>(label: String, var selected: T, val callback: Consumer<T>, val items: Supplier<Array<T>>, val enabled: Boolean) : OverlayLabel(label) {

    override fun render(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int, overlayH: Int): Boolean {
        // Render name of setting as text.
        val textY = overlayY + (h - HyperiumMainGui.INSTANCE.fr.FONT_HEIGHT) / 2
        if (textY < overlayH / 4) {
            return false
        } else if (textY + h > overlayH / 4 * 3) {
            return false
        }
        if (mouseX >= overlayX && mouseX <= overlayX + w && mouseY >= overlayY && mouseY <= overlayY + h) {
            Gui.drawRect(overlayX, overlayY, overlayX + w, overlayY + h, 0x1e000000)
        }
        if (enabled) {
            HyperiumMainGui.INSTANCE.fr.drawString(label, (overlayX + 4).toFloat(),
                    (overlayY + (h - HyperiumMainGui.INSTANCE.fr.FONT_HEIGHT) / 2).toFloat(), 0xffffff)
        } else {
            HyperiumMainGui.INSTANCE.fr.drawString(label, (overlayX + 4).toFloat(),
                    (overlayY + (h - HyperiumMainGui.INSTANCE.fr.FONT_HEIGHT) / 2).toFloat(), 0xA9A9A9)
        }

        if(enabled) {
            HyperiumMainGui.INSTANCE.fr.drawString(selected.toString(), overlayX + w - HyperiumMainGui.INSTANCE.fr.getWidth(selected.toString()) - 5, textY.toFloat(), 0xffffff)
        } else{
            HyperiumMainGui.INSTANCE.fr.drawString(selected.toString(), overlayX + w - HyperiumMainGui.INSTANCE.fr.getWidth(selected.toString()) - 5, textY.toFloat(), Color(169, 169, 169).rgb)
        }
        return true
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (!enabled) {
            return
        }

        if (mouseX >= overlayX + w - HyperiumMainGui.INSTANCE.fr.getWidth(selected.toString()) - 5 && mouseX <= overlayX + w - 5 && mouseY >= overlayY + 5 && mouseY <= overlayY + h - 5) {
            val tmp = items.get()
            selected = if (tmp.indexOf(selected) == tmp.size - 1) tmp[0] else tmp[tmp.indexOf(selected) + 1]
            callback.accept(selected)
        }
    }
}