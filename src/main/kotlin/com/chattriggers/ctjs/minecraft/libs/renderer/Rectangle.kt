package com.chattriggers.ctjs.minecraft.libs.renderer

import org.lwjgl.util.vector.Vector2f

class Rectangle(
        private var color: Int,
        private var x: Float,
        private var y: Float,
        private var width: Float,
        private var height: Float) {

    private var shadow = Shadow(this)
    private var outline = Outline(this)

    fun getColor() = this.color
    fun setColor(color: Int) = apply { this.color = color }

    fun getX() = this.x
    fun setX(x: Float) = apply { this.x = x }

    fun getY() = this.y
    fun setY(y: Float) = apply { this.y = y }

    fun getWidth() = this.width
    fun setWidth(width: Float) = apply { this.width = width }

    fun getHeight() = this.height
    fun setHeight(height: Float) = apply { this.height = height }

    fun isShadow() = this.shadow.on
    fun setShadow(shadow: Boolean) = apply { this.shadow.on = shadow }

    fun getShadowOffset() = this.shadow.offset
    fun getShadowOffsetX() = this.shadow.offset.x
    fun getShadowOffsetY() = this.shadow.offset.y
    fun setShadowOffset(x: Float, y: Float) = apply {
        this.shadow.offset.x = x
        this.shadow.offset.y = y
    }
    fun setShadowOffsetX(x: Float) = apply { this.shadow.offset.x = x }
    fun setShadowOffsetY(y: Float) = apply { this.shadow.offset.y = y }

    fun getShadowColor() = this.shadow.color
    fun setShadowColor(color: Int) = apply { this.shadow.color = color }

    fun setShadow(color: Int, x: Float, y: Float) = apply {
        setShadow(true)
        setShadowColor(color)
        setShadowOffset(x, y)
    }

    fun getOutline() = this.outline.on
    fun setOutline(outline: Boolean) = apply { this.outline.on = outline }

    fun getOutlineColor() = this.outline.color
    fun setOutlineColor(color: Int) = apply { this.outline.color = color }

    fun getThickness() = this.outline.thickness
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
        Renderer.finishDraw()
    }

    private class Shadow(
            val rect: Rectangle,
            var on: Boolean = false,
            var color: Int = 0x50000000,
            var offset: Vector2f = Vector2f(5f, 5f)) {
        fun draw() {
            if (!this.on) return
            Renderer.drawRect(this.color,
                    this.rect.x + this.offset.x,
                    this.rect.y + this.rect.height,
                    this.rect.width,
                    this.offset.y
            )
            Renderer.drawRect(this.color,
                    this.rect.x + this.rect.width,
                    this.rect.y + this.offset.y,
                    this.offset.x,
                    this.rect.height - this.offset.y
            )
        }
    }

    private class Outline(
            val rect: Rectangle,
            var on: Boolean = false,
            var color: Int = 0xff000000.toInt(),
            var thickness: Float = 5f) {
        fun draw() {
            if (!this.on) return
            Renderer.drawRect(this.color,
                    this.rect.x - this.thickness,
                    this.rect.y - this.thickness,
                    this.rect.width + this.thickness * 2,
                    this.rect.height + this.thickness * 2
            )
        }
    }
}