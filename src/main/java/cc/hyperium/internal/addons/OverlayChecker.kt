package cc.hyperium.internal.addons

import cc.hyperium.gui.main.HyperiumOverlay
import cc.hyperium.internal.addons.misc.AddonLoadException

object OverlayChecker {
    @Throws(Throwable::class)
    fun checkOverlayField(value: String) {
        val originClass = Class.forName(value)
        if (!originClass.isAssignableFrom(HyperiumOverlay::class.java)) {
            throw AddonLoadException("overlay has to be an instance of HyperiumOverlay")
        }
    }
}
