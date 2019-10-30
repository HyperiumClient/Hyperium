package com.chattriggers.ctjs.minecraft.objects.display

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.triggers.OnRegularTrigger
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.NotAbstract
import jdk.nashorn.api.scripting.ScriptObjectMirror
import org.lwjgl.input.Mouse
import org.lwjgl.util.vector.Vector2f

@External
@NotAbstract
abstract class DisplayLine {
    private lateinit var text: Text
    private var textWidth = 0f
    private var textColor: Int? = null
    private var backgroundColor: Int? = null

    private var background: DisplayHandler.Background? = null
    private var align: DisplayHandler.Align? = null

    private var onClicked: OnTrigger? = null
    private var onHovered: OnTrigger? = null
    private var onDragged: OnTrigger? = null

    private var mouseState = HashMap<Int, Boolean>()
    private var draggedState = HashMap<Int, Vector2f>()

    constructor(text: String) {
        setText(text)
        for (i in 0..5) mouseState[i] = false
    }

    constructor(text: String, config: ScriptObjectMirror) {
        setText(text)
        for (i in 0..5) mouseState[i] = false

        textColor = config.getOption("textColor", null)?.toInt()
        backgroundColor = config.getOption("backgroundColor", null)?.toInt()

        setAlign(config.getOption("align", null))
        setBackground(config.getOption("background", null))
    }

    private fun ScriptObjectMirror?.getOption(key: String, default: Any?): String? {
        if (this == null) return default?.toString()
        return this.getOrDefault(key, default).toString()
    }

    fun getText(): Text = text
    fun setText(text: String) = apply {
        this.text = Text(text)
        this.textWidth = Renderer.getStringWidth(text) * this.text.getScale()
    }

    fun getTextWidth(): Float = textWidth

    fun setShadow(shadow: Boolean) = apply { this.text.setShadow(shadow) }

    fun setScale(scale: Float) = apply {
        this.text.setScale(scale)
        this.textWidth = Renderer.getStringWidth(text.getString()) * scale
    }

    fun getAlign(): DisplayHandler.Align? = align
    fun setAlign(align: Any?) = apply {
        this.align = when (align) {
            is String -> DisplayHandler.Align.valueOf(align.toUpperCase())
            is DisplayHandler.Align -> align
            else -> null
        }
    }

    fun getBackground(): DisplayHandler.Background? = background
    fun setBackground(background: Any?) = apply {
        this.background = when (background) {
            is String -> DisplayHandler.Background.valueOf(background.toUpperCase().replace(" ", "_"))
            is DisplayHandler.Background -> background
            else -> null
        }
    }

    fun getBackgroundColor(): Int? = backgroundColor
    fun setBackgroundColor(color: Int) = apply {
        this.backgroundColor = color
    }

    fun setTextColor(): Int? = textColor
    fun setTextColor(color: Int) = apply {
        this.textColor = color
    }

    fun registerClicked(method: Any) = run {
        this.onClicked = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        this.onClicked
    }

    fun registerHovered(method: Any) = run {
        this.onHovered = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        this.onHovered
    }

    fun registerDragged(method: Any) = run {
        this.onDragged = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        this.onDragged
    }

    private fun handleInput(x: Float, y: Float, width: Float, height: Float) {
        if (!Mouse.isCreated()) return

        for (button in 0..5) handleDragged(button)

        if (Client.getMouseX() > x && Client.getMouseX() < x + width
            && Client.getMouseY() > y && Client.getMouseY() < y + height
        ) {
            handleHovered()

            for (button in 0..5) {
                if (Mouse.isButtonDown(button) == mouseState[button]) continue
                handleClicked(button)
                mouseState[button] = Mouse.isButtonDown(button)
                if (Mouse.isButtonDown(button))
                    draggedState[button] = Vector2f(Client.getMouseX(), Client.getMouseY())
            }
        }

        for (button in 0..5) {
            if (Mouse.isButtonDown(button)) continue
            if (!draggedState.containsKey(button)) continue
            draggedState.remove(button)
        }
    }

    private fun handleClicked(button: Int) {
        onClicked?.trigger(
            Client.getMouseX(),
            Client.getMouseY(),
            button,
            Mouse.isButtonDown(button)
        )
    }

    private fun handleHovered() {
        onHovered?.trigger(
            Client.getMouseX(),
            Client.getMouseY()
        )
    }

    private fun handleDragged(button: Int) {
        if (onDragged == null) return

        if (!draggedState.containsKey(button))
            return

        onDragged?.trigger(
            Client.getMouseX() - draggedState[button]!!.x,
            Client.getMouseY() - draggedState[button]!!.y,
            Client.getMouseX(),
            Client.getMouseY(),
            button
        )

        draggedState[button] = Vector2f(Client.getMouseX(), Client.getMouseY())
    }

    private fun drawFullBG(bg: DisplayHandler.Background, color: Int, x: Float, y: Float, width: Float, height: Float) {
        if (bg === DisplayHandler.Background.FULL)
            Renderer.drawRect(color, x, y, width, height)
    }

    private fun drawPerLineBG(
        bg: DisplayHandler.Background,
        color: Int,
        x: Float,
        y: Float,
        width: Float,
        height: Float
    ) {
        if (bg === DisplayHandler.Background.PER_LINE)
            Renderer.drawRect(color, x, y, width, height)
    }

    fun drawLeft(
        x: Float,
        y: Float,
        maxWidth: Float,
        background: DisplayHandler.Background,
        backgroundColor: Int,
        textColor: Int
    ) {
        val bg = this.background ?: background
        val bgColor = this.backgroundColor ?: backgroundColor
        val textCol = this.textColor ?: textColor

        // full background
        drawFullBG(bg, bgColor, x - 1, y - 1, maxWidth + 2, 10 * text.getScale())

        // blank line
        if ("" == text.getString()) return

        // text and per line background
        var xOff = x

        if (align === DisplayHandler.Align.RIGHT) {
            xOff = x - textWidth + maxWidth
        } else if (align === DisplayHandler.Align.CENTER) {
            xOff = x - textWidth / 2 + maxWidth / 2
        }

        drawPerLineBG(bg, bgColor, xOff - 1, y - 1, (textWidth + 2), 10 * text.getScale())
        text.setX(xOff).setY(y).setColor(textCol).draw()

        handleInput(xOff - 1, y - 1, (textWidth + 2), 10 * text.getScale())
    }

    fun drawRight(
        x: Float,
        y: Float,
        maxWidth: Float,
        background: DisplayHandler.Background,
        backgroundColor: Int,
        textColor: Int
    ) {
        val bg = this.background ?: background
        val bgColor = this.backgroundColor ?: backgroundColor
        val textCol = this.textColor ?: textColor

        // full background
        drawFullBG(bg, bgColor, x - maxWidth - 1f, y - 1, maxWidth + 2, 10 * text.getScale())

        // blank line
        if ("" == text.getString()) return

        // text and per line background\
        var xOff = x - textWidth

        if (align === DisplayHandler.Align.LEFT) {
            xOff = x - maxWidth
        } else if (align === DisplayHandler.Align.CENTER) {
            xOff = x - (textWidth / 2) - maxWidth / 2
        }

        drawPerLineBG(bg, bgColor, xOff - 1, y - 1, (textWidth + 2), 10 * text.getScale())
        text.setX(xOff).setY(y).setColor(textCol).draw()

        handleInput(xOff - 1, y - 1, (textWidth + 2), 10 * text.getScale())
    }

    fun drawCenter(
        x: Float,
        y: Float,
        maxWidth: Float,
        background: DisplayHandler.Background,
        backgroundColor: Int,
        textColor: Int
    ) {
        val bg = this.background ?: background
        val bgColor = this.backgroundColor ?: backgroundColor
        val textCol = this.textColor ?: textColor

        // full background
        drawFullBG(bg, bgColor, x - maxWidth / 2 - 1f, y - 1, maxWidth + 2, 10 * text.getScale())

        // blank line
        if ("" == text.getString()) return

        // text and per line background
        var xOff = x - textWidth / 2

        if (align === DisplayHandler.Align.LEFT) {
            xOff = x - maxWidth / 2
        } else if (align === DisplayHandler.Align.RIGHT) {
            xOff = x + maxWidth / 2 - textWidth
        }

        drawPerLineBG(bg, bgColor, xOff - 1, y - 1, (textWidth + 2), 10 * text.getScale())
        text.setX(xOff).setY(y).setColor(textCol).draw()

        handleInput(xOff - 1, y - 1, (textWidth + 2), 10 * text.getScale())
    }

    internal abstract fun getLoader(): ILoader

    override fun toString() =
        "DisplayLine{" +
                "text=$text, textColor=$textColor, align=$align, " +
                "background=$background, backgroundColor=$backgroundColor, " +
                "}"
}
