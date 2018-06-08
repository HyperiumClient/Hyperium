package cc.hyperium.gui.main.components

import org.lwjgl.input.Mouse

class OverlayButton(label: String, val callback: Runnable) : OverlayComponent() {
    init {
        this.label = label
    }

    override fun handleMouseInput(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (Mouse.isButtonDown(0) && mouseX >= overlayX + w - 30 && mouseX <= overlayX + w - 5 && mouseY >= overlayY + 5 && mouseY <= overlayY + h - 5)
            callback.run()
    }
}