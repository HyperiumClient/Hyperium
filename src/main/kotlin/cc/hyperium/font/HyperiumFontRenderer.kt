package cc.hyperium.font

import cc.hyperium.utils.ChatColor
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import org.newdawn.slick.UnicodeFont
import org.newdawn.slick.font.effects.ColorEffect
import java.awt.Color
import java.awt.Font
import java.util.*


class HyperiumFontRenderer(private val fontImpl: Font, val size: Float, private var antiAliasingFactor: Int) {

    private var prevScaleFactor: Int = ScaledResolution(Minecraft.getMinecraft()).scaleFactor
    private var font: UnicodeFont = UnicodeFont(fontImpl.deriveFont(size * prevScaleFactor / 2))
    private var colorCodes: IntArray

    init {
        font.addAsciiGlyphs()
        font.effects.add(ColorEffect(Color.WHITE))
        font.loadGlyphs()
        this.colorCodes = IntArray(32)
        (0..31).forEach { i ->
            val shadow = (i shr 3 and 1) * 85
            var red = (i shr 2 and 1) * 170 + shadow
            var green = (i shr 1 and 1) * 170 + shadow
            var blue = (i and 1) * 170 + shadow

            if (i >= 16) {
                red /= 4
                green /= 4
                blue /= 4
            }

            this.colorCodes[i] = red and 255 shl 16 or (green and 255 shl 8) or (blue and 255)
        }
    }

    fun drawString(text: String, x: Int, y: Int, color: Int, shadow: Boolean) {
        val textureEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D)
        val resolution = ScaledResolution(Minecraft.getMinecraft())

        if (resolution.scaleFactor != prevScaleFactor) {
            prevScaleFactor = resolution.scaleFactor
            font = UnicodeFont(fontImpl.deriveFont(size * prevScaleFactor / 2))
            font.addAsciiGlyphs()
            font.effects.add(ColorEffect(Color.WHITE))
            font.loadGlyphs()
        }

        antiAliasingFactor = resolution.scaleFactor

        val posX: Float = (x * antiAliasingFactor).toFloat()
        val posY: Float = (y * antiAliasingFactor).toFloat()

        val chars = text.toCharArray()

        val red = (color shr 16 and 255).toFloat() / 255.0f
        val green = (color shr 8 and 255).toFloat() / 255.0f
        val blue = (color and 255).toFloat() / 255.0f
        val alpha = (color shr 24 and 255).toFloat() / 255.0f

        GL11.glColor4f(red, green, blue, alpha)

        GlStateManager.scale(1f / antiAliasingFactor, 1f / antiAliasingFactor, 1f / antiAliasingFactor)

        GlStateManager.disableTexture2D()
        GlStateManager.disableLighting()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        var currentX: Float = posX
        var currentY: Float = posY
        var currentColor = color

        chars.indices.forEach { i ->
            val c = chars[i]

            if (c == '\n') {
                currentY += getHeight(text) / 4
                currentX = posX
            } else if (c != '\u00a7' && (i == 0 || i == chars.size - 1 || chars[i - 1] != '\u00a7')) {
                if ((i > 0 && chars[i - 1] == '\u00a7' && i == chars.size - 1) || alpha == 0.0f) {
                    return@forEach
                }

                val charStr = c.toString()

                font.drawString(
                    currentX,
                    currentY,
                    charStr,
                    org.newdawn.slick.Color(currentColor)
                )

                currentX += font.getWidth(charStr)
            } else if (c == '\u00a7' && i != chars.size - 1) {
                var index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH)[i + 1])

                if (index < 0) {
                    return@forEach
                }

                if (shadow) {
                    index += 16
                }

                val colorResult = colorCodes[index]

                val rRed = colorResult shr 16 and 255
                val rGreen = colorResult shr 8 and 255
                val rBlue = colorResult and 255
                val rAlpha = colorResult shr 24 and 255

                currentColor = java.awt.Color(rRed, rGreen, rBlue, if (shadow) 150 else rAlpha).rgb
            }
        }

        GlStateManager.scale(
            antiAliasingFactor.toDouble(),
            antiAliasingFactor.toDouble(),
            antiAliasingFactor.toDouble()
        )

        GL11.glColor4f(1f, 1f, 1f, 1f)

        GlStateManager.bindTexture(0)

        if (textureEnabled) GlStateManager.enableTexture2D()
    }

    fun drawString(text: String, x: Int, y: Int, color: Int) = drawString(text, x, y, color, false)

    private fun drawStringWithShadow(text: String, x: Int, y: Int, color: Int) {
        drawString(text, x + 1, y + 1, Color(0, 0, 0, 150).rgb, true)
        drawString(text, x, y, color)
    }

    fun drawCenteredString(text: String, x: Int, y: Int, color: Int) =
        drawString(text, x - getWidth(text) / 2, y, color)

    private fun drawCenteredStringWithShadow(text: String, x: Int, y: Int, color: Int) =
        drawStringWithShadow(text, x - getWidth(text) / 2, y, color)

    fun getHeight(text: String) = font.getHeight(text) / antiAliasingFactor

    fun getWidth(text: String) = font.getWidth(ChatColor.stripColor(text)) / antiAliasingFactor

    fun splitString(text: String, wrapWidth: Int): List<String> {
        val lines = arrayListOf<String>()

        val splitText = text.split(" ".toRegex()).dropLastWhile {
            it.isEmpty()
        }.toTypedArray()
        var currentString = StringBuilder()

        splitText.forEach { word ->
            val potential = "$currentString $word"

            if (getWidth(potential) >= wrapWidth) {
                lines.add(currentString.toString())
                currentString = StringBuilder()
            }
            currentString.append(word).append(" ")
        }

        lines.add(currentString.toString())
        return lines
    }
}
