package cc.hyperium.gui.main.components


open class OverlayLabel(val label: String, enabled: Boolean, var click: Runnable) : OverlayComponent(enabled) {

    constructor(label: String) : this(label, true, click = Runnable { })

    override fun mouseClicked(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (mouseX >= overlayX && mouseX <= overlayX + w && mouseY >= overlayY && mouseY <= overlayY + h) {
            click.run()
        }
    }
}