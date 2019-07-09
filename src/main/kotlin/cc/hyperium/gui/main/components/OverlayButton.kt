package cc.hyperium.gui.main.components

class OverlayButton(label: String, val callback: Runnable) : OverlayLabel(label,true, Runnable {  }) {
    override fun mouseClicked(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (mouseX >= overlayX && mouseX <= overlayX + w && mouseY >= overlayY && mouseY <= overlayY + h)
            callback.run()
    }
}