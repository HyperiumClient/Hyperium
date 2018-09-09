package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler
import net.minecraft.client.renderer.GlStateManager

class Text(private var string: String, private var x: Float = 0f, private var y: Float = 0f) {
    private var lines = mutableListOf<String>()

    private var color = 0xffffffff.toInt()
    private var formatted = true
    private var shadow = false
    private var align = DisplayHandler.Align.LEFT

    private var width = 0
    private var maxLines = 0
    private var scale = 1f

    init {
        this.lines.add(this.string)
        updateFormatting()
    }

    fun getString() = this.string
    fun setString(string: String) = apply { this.string = string }

    fun getColor() = this.color
    fun setColor(color: Int) = apply { this.color = fixAlpha(color) }

    fun getFormatted() = this.formatted
    fun setFormatted(formatted: Boolean) = apply {
        this.formatted = formatted
        updateFormatting()
    }

    fun getShadow() = this.shadow
    fun setShadow(shadow: Boolean) = apply  { this.shadow = shadow }

    fun getAlign() = this.align
    fun setAlign(align: Any) = apply {
        this.align = when (align) {
            is String -> DisplayHandler.Align.valueOf(align.toUpperCase())
            is DisplayHandler.Align -> align
            else -> DisplayHandler.Align.LEFT
        }
    }

    fun getX() = this.x
    fun setX(x: Float) = apply { this.x = x }

    fun getY() = this.y
    fun setY(y: Float) = apply { this.y = y }

    fun getWidth() = this.width
    fun setWidth(width: Int) = apply {
        this.width = width
        this.lines = Renderer.getFontRenderer().listFormattedStringToWidth(this.string, this.width)
    }

    fun getMaxLines() = this.maxLines
    fun setMaxLines(maxLines: Int) = apply { this.maxLines = maxLines }

    fun getScale() = this.scale
    fun setScale(scale: Float) = apply { this.scale = scale }

    fun getMaxWidth(): Int {
        return if (this.width == 0) {
            Renderer.getStringWidth(this.string)
        } else {
            var maxWidth = 0
            this.lines.forEach {
                if (Renderer.getStringWidth(it) > maxWidth)
                    maxWidth = Renderer.getStringWidth(it)
            }
            maxWidth
        }
    }

    fun getHeight(): Float {
        return if (this.width == 0)
            this.scale * 9
            else this.lines.size * this.scale * 9
    }

    fun exceedsMaxLines(): Boolean {
        return this.width != 0 && this.lines.size > this.maxLines
    }

    @JvmOverloads
    fun draw(x: Float? = null, y: Float? = null) = apply {
        GlStateManager.enableBlend()
        GlStateManager.scale(this.scale, this.scale, this.scale)
        if (this.width > 0) {
            var maxLinesHolder = this.maxLines
            var yHolder = y ?: this.y
            this.lines.forEach {
                Renderer.getFontRenderer().drawString(it, getXAlign(it, x ?: this.x), yHolder / this.scale, this.color, this.shadow)
                yHolder += this.scale * 9
                maxLinesHolder--
                if (maxLinesHolder == 0)
                    return@forEach
            }
        } else {
            Renderer.getFontRenderer().drawString(this.string, getXAlign(this.string, x ?: this.x), (y ?: this.y) / this.scale, this.color, this.shadow)
        }
        GlStateManager.disableBlend()
        Renderer.finishDraw()
    }

    private fun updateFormatting() {
        this.string =
                if (this.formatted) ChatLib.addColor(this.string)
                else ChatLib.replaceFormatting(this.string)
    }

    private fun fixAlpha(color: Int): Int {
        val alpha = color shr 24 and 255
        return if (alpha < 10)
            (color and 0xFF_FF_FF) or 0xA_FF_FF_FF
            else color
    }

    private fun getXAlign(string: String, x: Float): Float {
        val newX = x / this.scale
        return when (this.align) {
            DisplayHandler.Align.CENTER -> newX - Renderer.getStringWidth(string) / 2
            DisplayHandler.Align.RIGHT -> newX - Renderer.getStringWidth(string)
            else -> newX
        }
    }
}