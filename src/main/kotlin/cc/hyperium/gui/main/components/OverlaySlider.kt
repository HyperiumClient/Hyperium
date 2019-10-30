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
import cc.hyperium.utils.RenderUtils
import org.lwjgl.input.Mouse
import java.awt.Color
import java.awt.Font
import java.util.function.Consumer
import kotlin.math.roundToInt

@Deprecated("Soon to be removed, please refrain from using.")
class OverlaySlider @JvmOverloads constructor(
    label: String,
    private val minVal: Float,
    private val maxVal: Float,
    var value: Float,
    var update: Consumer<Float>,
    var round: Boolean,
    val enabled: Boolean = true
) : OverlayLabel(label, enabled, Runnable { }) {
    private val fr = HyperiumFontRenderer("Arial", Font.PLAIN, 20)

    var updated = false
    override fun handleMouseInput(mouseX: Int, mouseY: Int, overlayX: Int, overlayY: Int, w: Int, h: Int) {
        if (mouseX >= overlayX + w - 105 && mouseX <= overlayX + w - 5 && mouseY >= overlayY && mouseY <= overlayY + h && Mouse.isButtonDown(
                0
            )
        ) {
            val fx = mouseX - (overlayX + w - 105)
            value = fx / 100F * (maxVal - minVal) + minVal
            updated = false
        } else {
            if (!updated) {
                updated = true
                update.accept(value)
            }
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
        if (!super.render(mouseX, mouseY, overlayX, overlayY, w, h, overlayH))
            return false
        val left = (overlayX + w - 105).toFloat()

        val fr = fr
        var s = value.toString()
        if (round)
            s = value.roundToInt().toString()
        val toFloat = (overlayY + h / 2).toFloat()

        var color = 0xFFFFFFFF.toInt()

        if (!super.enabled) {
            color = Color(169, 169, 169).rgb
        }
        fr.drawString(s, left - 5 - fr.getWidth(s), toFloat - 5, color)
        val rightSide = (overlayX + w - 5).toFloat()
        RenderUtils.drawLine(left, toFloat, rightSide, (overlayY + h / 2).toFloat(), 2f, color)
        val d = (value - minVal) / (maxVal - minVal) * 100
        val toInt = (left + d).toInt()
        RenderUtils.drawFilledCircle(toInt, overlayY + h / 2, 5f, color)
        return true
    }
}
