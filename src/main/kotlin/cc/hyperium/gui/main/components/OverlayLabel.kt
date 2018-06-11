package cc.hyperium.gui.main.components

open class OverlayLabel(label: String) : OverlayComponent() {
    init {
        this.label = label
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {}
}