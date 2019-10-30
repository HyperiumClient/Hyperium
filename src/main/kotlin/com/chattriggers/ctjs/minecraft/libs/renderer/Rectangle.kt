package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.utils.kotlin.External
import org.lwjgl.util.vector.Vector2f

@External
class Rectangle(
    private var color: Int,
    private var x: Float,
    private var y: Float,
    private var width: Float,
    private var height: Float
) {

    private var shadow = Shadow(this)
    private var outline = Outline(this)

    fun getColor(): Int = color
    fun setColor(color: Int) = apply { this.color = color }

    fun getX(): Float = x
    fun setX(x: Float) = apply { this.x = x }

    fun getY(): Float = y
    fun setY(y: Float) = apply { this.y = y }

    fun getWidth(): Float = width
    fun setWidth(width: Float) = apply { this.width = width }

    fun getHeight(): Float = height
    fun setHeight(height: Float) = apply { this.height = height }

    fun isShadow(): Boolean = shadow.on
    fun setShadow(shadow: Boolean) = apply { this.shadow.on = shadow }

    fun getShadowOffset(): Vector2f = shadow.offset
    fun getShadowOffsetX(): Float = shadow.offset.x
    fun getShadowOffsetY(): Float = shadow.offset.y
    fun setShadowOffset(x: Float, y: Float) = apply {
        this.shadow.offset.x = x
        this.shadow.offset.y = y
    }

    fun setShadowOffsetX(x: Float) = apply { this.shadow.offset.x = x }
    fun setShadowOffsetY(y: Float) = apply { this.shadow.offset.y = y }

    fun getShadowColor(): Int = shadow.color
    fun setShadowColor(color: Int) = apply { this.shadow.color = color }

    fun setShadow(color: Int, x: Float, y: Float) = apply {
        setShadow(true)
        setShadowColor(color)
        setShadowOffset(x, y)
    }

    fun getOutline(): Boolean = outline.on
    fun setOutline(outline: Boolean) = apply { this.outline.on = outline }

    fun getOutlineColor(): Int = outline.color
    fun setOutlineColor(color: Int) = apply { this.outline.color = color }

    fun getThickness(): Float = outline.thickness
    fun setThickness(thickness: Float) = apply { this.outline.thickness = thickness }

    fun setOutline(color: Int, thickness: Float) = apply {
        setOutline(true)
        setOutlineColor(color)
        setThickness(thickness)
    }

    fun draw() = apply {
        this.shadow.draw()
        this.outline.draw()
        Renderer.drawRect(this.color, this.x, this.y, this.width, this.height)
    }

    private class Shadow(
        val rect: Rectangle,
        var on: Boolean = false,
        var color: Int = 0x50000000,
        var offset: Vector2f = Vector2f(5f, 5f)
    ) {
        fun draw() {
            if (!on) return
            Renderer.drawRect(
                color,
                rect.x + offset.x,
                rect.y + rect.height,
                rect.width,
                offset.y
            )
            Renderer.drawRect(
                color,
                rect.x + rect.width,
                rect.y + offset.y,
                offset.x,
                rect.height - offset.y
            )
        }
    }

    private class Outline(
        val rect: Rectangle,
        var on: Boolean = false,
        var color: Int = 0xff000000.toInt(),
        var thickness: Float = 5f
    ) {
        fun draw() {
            if (!on) return
            Renderer.drawRect(
                color,
                rect.x - thickness,
                rect.y - thickness,
                rect.width + thickness * 2,
                rect.height + thickness * 2
            )
        }
    }
}
