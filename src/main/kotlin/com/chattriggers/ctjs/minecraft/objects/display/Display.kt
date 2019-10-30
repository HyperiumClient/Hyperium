package com.chattriggers.ctjs.minecraft.objects.display

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.NotAbstract
import jdk.nashorn.api.scripting.ScriptObjectMirror

@External
@NotAbstract
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

    private var minWidth = 0f
    private var width = 0f
    private var height = 0f

    constructor() {
        DisplayHandler.registerDisplay(this)
    }

    constructor(config: ScriptObjectMirror?) {
        shouldRender = config.getOption("shouldRender", true).toBoolean()
        renderX = config.getOption("renderX", 0).toFloat()
        renderY = config.getOption("renderY", 0).toFloat()

        backgroundColor = config.getOption("backgroundColor", 0x50000000).toInt()

        textColor = config.getOption("textColor", 0xffffffff.toInt()).toInt()

        setBackground(config.getOption("background", DisplayHandler.Background.NONE))
        setAlign(config.getOption("align", DisplayHandler.Align.LEFT))
        setOrder(config.getOption("order", DisplayHandler.Order.DOWN))

        minWidth = config.getOption("minWidth", 0f).toFloat()

        DisplayHandler.registerDisplay(this)
    }

    private fun ScriptObjectMirror?.getOption(key: String, default: Any): String {
        if (this == null) return default.toString()
        return this.getOrDefault(key, default).toString()
    }

    fun getBackgroundColor() = backgroundColor
    fun setBackgroundColor(backgroundColor: Int): Display {
        this.backgroundColor = backgroundColor
        return this
    }

    fun getTextColor() = textColor
    fun setTextColor(textColor: Int): Display {
        this.textColor = textColor
        return this
    }

    fun getBackground(): DisplayHandler.Background = background
    fun setBackground(background: Any): Display {
        this.background = when (background) {
            is String -> DisplayHandler.Background.valueOf(background.toUpperCase().replace(" ", "_"))
            is DisplayHandler.Background -> background
            else -> DisplayHandler.Background.NONE
        }
        return this
    }

    fun getAlign(): DisplayHandler.Align = align
    fun setAlign(align: Any): Display {
        this.align = when (align) {
            is String -> DisplayHandler.Align.valueOf(align.toUpperCase())
            is DisplayHandler.Align -> align
            else -> DisplayHandler.Align.LEFT
        }
        return this
    }

    fun getOrder(): DisplayHandler.Order = order
    fun setOrder(order: Any): Display {
        this.order = when (order) {
            is String -> DisplayHandler.Order.valueOf(order.toUpperCase())
            is DisplayHandler.Order -> order
            else -> DisplayHandler.Order.DOWN
        }
        return this
    }


    fun setLine(index: Int, line: Any): Display {
        while (this.lines.size - 1 < index) this.lines.add(createDisplayLine(""))
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
            this.lines.add(
                when (it) {
                    is String -> createDisplayLine(it)
                    is DisplayLine -> it
                    else -> createDisplayLine("")
                }
            )
        }
        return this
    }

    fun clearLines(): Display {
        this.lines.clear()
        return this
    }

    fun getRenderX() = renderX
    fun setRenderX(renderX: Float): Display {
        this.renderX
        return this
    }

    fun getRenderY() = renderY
    fun setRenderY(renderY: Float): Display {
        this.renderY = renderY
        return this
    }

    fun setRenderLoc(renderX: Float, renderY: Float): Display {
        this.renderX = renderX
        this.renderY = renderY
        return this
    }

    fun getShouldRender() = shouldRender
    fun setShouldRender(shouldRender: Boolean): Display {
        this.shouldRender = shouldRender
        return this
    }

    fun getWidth() = width
    fun getHeight() = height
    fun getMinWidth() = minWidth
    fun setMinWidth(minWidth: Float): Display {
        this.minWidth = minWidth
        return this
    }

    fun render() {
        if (!shouldRender) return

        var maxWidth = minWidth
        lines.forEach {
            if (it.getTextWidth() > maxWidth)
                maxWidth = it.getTextWidth()
        }

        width = maxWidth

        var i = 0f
        lines.forEach {
            drawLine(it, renderX, renderY + (i * 10), maxWidth)
            when (order) {
                DisplayHandler.Order.DOWN -> i += it.getText().getScale()
                DisplayHandler.Order.UP -> i -= it.getText().getScale()
            }
        }

        height = i
    }

    private fun drawLine(line: DisplayLine, x: Float, y: Float, maxWidth: Float) {
        when (align) {
            DisplayHandler.Align.LEFT -> line.drawLeft(
                x,
                y,
                maxWidth,
                background,
                backgroundColor,
                textColor
            )
            DisplayHandler.Align.RIGHT -> line.drawRight(
                x,
                y,
                maxWidth,
                background,
                backgroundColor,
                textColor
            )
            DisplayHandler.Align.CENTER -> line.drawCenter(
                x,
                y,
                maxWidth,
                background,
                backgroundColor,
                textColor
            )
            else -> return
        }
    }

    internal abstract fun createDisplayLine(text: String): DisplayLine

    override fun toString() =
        "Display{" +
                "shouldRender=$shouldRender, " +
                "renderX=$renderX, renderY=$renderY, " +
                "background=$background, backgroundColor=$backgroundColor, " +
                "textColor=$textColor, align=$align, order=$order, " +
                "minWidth=$minWidth, width=$width, height=$height, " +
                "lines=$lines" +
                "}"

}
