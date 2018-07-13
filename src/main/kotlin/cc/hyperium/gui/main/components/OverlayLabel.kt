package cc.hyperium.gui.main.components

open class OverlayLabel(label: String, enabled: Boolean) : OverlayComponent(enabled) {
    init {
        this.label = label
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {}
}