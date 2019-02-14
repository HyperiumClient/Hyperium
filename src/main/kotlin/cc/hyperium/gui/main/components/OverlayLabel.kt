package cc.hyperium.gui.main.components

import cc.hyperium.utils.HyperiumFontRenderer
import net.minecraft.client.gui.Gui
import java.awt.Color
import java.awt.Font


open class OverlayLabel(label: String, enabled: Boolean, var click: Runnable) : OverlayComponent(enabled) {
    private val fr = HyperiumFontRenderer("Arial", Font.PLAIN, 20)

    init {
        this.label = label
    }

    constructor(label: String) : this(label, true, click = Runnable { })

    override fun mouseClicked(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (mouseX >= overlayX && mouseX <= overlayX + w && mouseY >= overlayY && mouseY <= overlayY + h) {
            click.run()
        }
    }

    override fun render(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int, overlayH: Int): Boolean {
        // Draw distinguishing box.
        val cls = OverlayLabel::class.java

        if (this.javaClass.equals(cls)) {
            // Check if the box exceeds boundaries.
            val textY = overlayY + (h - fr.FONT_HEIGHT) / 2
            if (textY < overlayH / 4) {
                return false
            } else if (textY + h > overlayH / 4 * 3) {
                return false
            }
            Gui.drawRect(overlayX, overlayY, overlayX + w, overlayY + h, Color(40, 40, 40).rgb)
        }

        return super.render(mouseX, mouseY, overlayX, overlayY, w, h, overlayH)
    }
}
