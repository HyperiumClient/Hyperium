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
import java.awt.Color
import java.awt.Font
import java.util.function.Consumer
import java.util.function.Supplier

@Deprecated("Soon to be removed, please refrain from using.")
class OverlaySelector<T> @JvmOverloads constructor(
    label: String,
    var selected: T,
    val callback: Consumer<T>,
    val items: Supplier<Array<T>>,
    var enabled: Boolean = true
) : OverlayLabel(label, enabled, Runnable { }) {
    private val fr = HyperiumFontRenderer("Arial", Font.PLAIN, 20)

    override fun render(
        mouseX: Int,
        mouseY: Int,
        overlayX: Int,
        overlayY: Int,
        w: Int,
        h: Int,
        overlayH: Int
    ): Boolean {
        if (!super.render(mouseX, mouseY, overlayX, overlayY, w, h, overlayH))
            return false

        val textY = overlayY + (h - fr.FONT_HEIGHT) / 2
        if (super.enabled) {
            fr.drawString(
                selected.toString(),
                overlayX + w - fr.getWidth(selected.toString()) - 5,
                textY.toFloat(),
                0xffffff
            )
        } else {
            fr.drawString(
                selected.toString(),
                overlayX + w - fr.getWidth(selected.toString()) - 5,
                textY.toFloat(),
                Color(169, 169, 169).rgb
            )
        }
        return true
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (!super.enabled) {
            return
        }

        if (mouseX >= overlayX + w - fr.getWidth(selected.toString()) - 5 && mouseX <= overlayX + w - 5 && mouseY >= overlayY + 5 && mouseY <= overlayY + h - 5) {
            val tmp = items.get()
            selected = if (tmp.indexOf(selected) == tmp.size - 1) tmp[0] else tmp[tmp.indexOf(selected) + 1]
            callback.accept(selected)
        }
    }
}
