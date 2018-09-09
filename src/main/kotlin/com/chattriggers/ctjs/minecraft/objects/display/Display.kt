package com.chattriggers.ctjs.minecraft.objects.display

import jdk.nashorn.api.scripting.ScriptObjectMirror

abstract class Display {
    private var lines = mutableListOf<DisplayLine>()

    private var renderX = 0f
    private var renderY = 0f
    private var shouldRender = true

    private var backgroundColor = 0x50000000
    private var textColor = 0xffffffff.toInt()

    private var background = DisplayHandler.Background.NONE
    private var align = DisplayHandler.Align.LEFT
    private var order = DisplayHandler.Order.DOWN

    private var minWidth = 0
    private var width = 0
    private var height = 0f

    constructor() {
        DisplayHandler.registerDisplay(this)
    }

    constructor(config: ScriptObjectMirror?) {
        this.shouldRender = config.getOption("shouldRender", true).toBoolean()
        this.renderX = config.getOption("renderX", 0).toFloat()
        this.renderY = config.getOption("renderY", 0).toFloat()

        this.backgroundColor = config.getOption("backgroundColor", 0x50000000).toInt()
        this.textColor = config.getOption("textColor", 0xffffffff).toInt()

        this.setBackground(config.getOption("background", DisplayHandler.Background.NONE))
        this.setAlign(config.getOption("align", DisplayHandler.Align.RIGHT))
        this.setOrder(config.getOption("order", DisplayHandler.Order.DOWN))

        this.minWidth = config.getOption("minWidth", 0).toInt()

        DisplayHandler.registerDisplay(this)
    }

    private fun ScriptObjectMirror?.getOption(key: String, default: Any): String {
        if (this == null) return default.toString()
        return this.getOrDefault(key, default).toString()
    }

    fun getBackgroundColor() = this.backgroundColor
    fun setBackgroundColor(backgroundColor: Int): Display {
        this.backgroundColor = backgroundColor
        return this
    }

    fun getTextColor() = this.textColor
    fun setTextColor(textColor: Int): Display {
        this.textColor = textColor
        return this
    }

    fun setBackground(background: Any): Display {
        this.background = when (background) {
            is String -> DisplayHandler.Background.valueOf(background.toUpperCase().replace(" ", "_"))
            is DisplayHandler.Background -> background
            else -> DisplayHandler.Background.NONE
        }
        return this
    }

    fun setAlign(align: Any): Display {
        this.align = when (align) {
            is String -> DisplayHandler.Align.valueOf(align.toUpperCase())
            is DisplayHandler.Align -> align
            else -> DisplayHandler.Align.LEFT
        }
        return this
    }

    fun setOrder(order: Any): Display {
        this.order = when (order) {
            is String -> DisplayHandler.Order.valueOf(order.toUpperCase())
            is DisplayHandler.Order -> order
            else -> DisplayHandler.Order.DOWN
        }
        return this
    }


    fun setLine(index: Int, line: Any): Display {
        while (this.lines.size -1 < index) this.lines.add(createDisplayLine(""))
        this.lines[index] = when (line) {
            is String -> createDisplayLine(line)
            is DisplayLine -> line
            else -> createDisplayLine("")
        }
        return this
    }

    fun getLine(index: Int) = this.lines[index]
    fun getLines() = this.lines
    fun setLines(lines: MutableList<DisplayLine>): Display {
        this.lines = lines
        return this
    }

    @JvmOverloads
    fun addLine(index: Int = -1, line: Any) {
        val toAdd = when (line) {
            is String -> createDisplayLine(line)
            is DisplayLine -> line
            else -> createDisplayLine("")
        }

        if (index == -1) this.lines.add(toAdd)
        else this.lines.add(index, toAdd)
    }

    fun addLines(vararg lines: Any): Display {
        lines.forEach {
            this.lines.add(when (it) {
                is String -> createDisplayLine(it)
                is DisplayLine -> it
                else -> createDisplayLine("")
            })
        }
        return this
    }

    fun clearLines(): Display {
        this.lines.clear()
        return this
    }

    fun getRenderX() = this.renderX
    fun setRenderX(renderX: Float): Display {
        this.renderX
        return this
    }

    fun getRenderY() = this.renderY
    fun setRenderY(renderY: Float): Display {
        this.renderY = renderY
        return this
    }

    fun setRenderLoc(renderX: Float, renderY: Float): Display {
        this.renderX = renderX
        this.renderY = renderY
        return this
    }

    fun getShouldRender() = this.shouldRender
    fun setShouldRender(shouldRender: Boolean): Display {
        this.shouldRender = shouldRender
        return this
    }

    fun getWidth() = this.width
    fun getHeight() = this.height
    fun getMinWidth() = this.minWidth
    fun setMinWidth(minWidth: Int): Display {
        this.minWidth = minWidth
        return this
    }

    fun render() {
        if (!this.shouldRender) return

        var maxWidth = this.minWidth
        lines.forEach {
            if (it.getTextWidth() > maxWidth)
                maxWidth = it.getTextWidth()
        }

        this.width = maxWidth

        var i = 0f
        lines.forEach {
            drawLine(it, this.renderX, this.renderY + (i * 10), maxWidth)
            when (this.order) {
                DisplayHandler.Order.DOWN -> i += it.getText().getScale()
                DisplayHandler.Order.UP -> i -= it.getText().getScale()
            }
        }

        this.height = i
    }

    private fun drawLine(line: DisplayLine, x: Float, y: Float, maxWidth: Int) {
        when (this.align) {
            DisplayHandler.Align.LEFT -> line.drawLeft(x, y, maxWidth.toFloat(), this.background, this.backgroundColor, this.textColor)
            DisplayHandler.Align.RIGHT -> line.drawRight(x, y, maxWidth.toFloat(), this.background, this.backgroundColor, this.textColor)
            DisplayHandler.Align.CENTER -> line.drawCenter(x, y, maxWidth.toFloat(), this.background, this.backgroundColor, this.textColor)
            else -> return
        }
    }

    internal abstract fun createDisplayLine(text: String): DisplayLine
}