package cc.hyperium.gui.main.components

import cc.hyperium.utils.HyperiumFontRenderer
import java.awt.Color
import java.awt.Font
import java.util.function.Consumer
import java.util.function.Supplier

class OverlaySelector<T> @JvmOverloads constructor(label: String, var selected: T, val callback: Consumer<T>, val items: Supplier<Array<T>>, var enabled: Boolean = true) : OverlayLabel(label,enabled, Runnable {  }) {
    private val fr = HyperiumFontRenderer("Arial", Font.PLAIN, 20)

    override fun render(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int, overlayH: Int): Boolean {
        if (!super.render(mouseX, mouseY, overlayX, overlayY, w, h, overlayH))
            return false

        val textY = overlayY + (h -fr.FONT_HEIGHT) / 2
        if(super.enabled) {
           fr.drawString(selected.toString(), overlayX + w -fr.getWidth(selected.toString()) - 5, textY.toFloat(), 0xffffff)
        } else{
           fr.drawString(selected.toString(), overlayX + w -fr.getWidth(selected.toString()) - 5, textY.toFloat(), Color(169, 169, 169).rgb)
        }
        return true
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (!super.enabled) {
            return
        }

        if (mouseX >= overlayX + w -fr.getWidth(selected.toString()) - 5 && mouseX <= overlayX + w - 5 && mouseY >= overlayY + 5 && mouseY <= overlayY + h - 5) {
            val tmp = items.get()
            selected = if (tmp.indexOf(selected) == tmp.size - 1) tmp[0] else tmp[tmp.indexOf(selected) + 1]
            callback.accept(selected)
        }
    }
}