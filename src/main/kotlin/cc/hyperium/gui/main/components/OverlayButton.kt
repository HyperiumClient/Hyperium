package cc.hyperium.gui.main.components

import org.lwjgl.input.Mouse

class OverlayButton(label: String, val callback: Runnable) : OverlayLabel(label) {
    override fun handleMouseInput(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (Mouse.isButtonDown(0) && mouseX >= overlayX && mouseX <= overlayX + w && mouseY >= overlayY && mouseY <= overlayY + h)
            callback.run()
    }
}