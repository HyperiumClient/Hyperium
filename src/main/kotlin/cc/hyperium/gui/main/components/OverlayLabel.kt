/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *  
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui.main.components

import cc.hyperium.utils.HyperiumFontRenderer
import net.minecraft.client.gui.Gui
import java.awt.Color
import java.awt.Font

@Deprecated("Soon to be removed, please refrain from using.")
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

    override fun render(
        mouseX: Int,
        mouseY: Int,
        overlayX: Int,
        overlayY: Int,
        w: Int,
        h: Int,
        overlayH: Int
    ): Boolean {
        // Draw distinguishing box.
        val cls = OverlayLabel::class.java

        if (this.javaClass == cls) {
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
