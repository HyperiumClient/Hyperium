package com.chattriggers.ctjs.minecraft.objects.message

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.utils.kotlin.*

@External
class TextComponent {

    lateinit var chatComponentText: ITextComponent

    private var text: String
    private var formatted = true

    private var clickAction: String? = null
    private var clickValue: String? = null
    private var hoverAction: String? = "show_text"
    private var hoverValue: String? = null

    constructor(text: String) {
        this.text = text
        reInstance()
    }

    constructor(chatComponent: ITextComponent) {
        chatComponentText = chatComponent
        text = chatComponentText.formattedText

        val chatStyle = chatComponent.getStyling()

        val clickEvent = chatStyle.getClick()
        clickAction = clickEvent?.action?.canonicalName
        clickValue = clickEvent?.value

        val hoverEvent = chatStyle.getHover()
        hoverAction = hoverEvent?.action?.canonicalName
        hoverValue = hoverEvent?.value?.formattedText
    }

    fun getText(): String = text
    fun setText(text: String) = apply {
        this.text = text
        reInstance()
    }

    fun isFormatted(): Boolean = formatted
    fun setFormatted(formatted: Boolean) = apply {
        this.formatted = formatted
        reInstance()
    }

    fun setClick(action: String, value: String) = apply {
        this.clickAction = action
        this.clickValue = value
        reInstanceClick()
    }

    fun getClickAction(): String? = clickAction
    fun setClickAction(action: String) = apply {
        this.clickAction = action
        reInstanceClick()
    }

    fun getClickValue(): String? = clickValue
    fun setClickValue(value: String) = apply {
        this.clickValue = value
        reInstanceClick()
    }

    fun setHover(action: String, value: String) = apply {
        this.hoverAction = action
        this.hoverValue = value
        reInstanceHover()
    }

    fun getHoverAction(): String? = hoverAction
    fun setHoverAction(action: String) = apply {
        this.hoverAction = action
        reInstanceHover()
    }

    fun getHoverValue(): String? = hoverValue
    fun setHoverValue(value: String) = apply {
        this.hoverValue = value
        reInstanceHover()
    }

    fun chat() = Message(this).chat()
    fun actionBar() = Message(this).actionBar()

    private fun reInstance() {
        chatComponentText = BaseTextComponent(
            if (formatted) ChatLib.addColor(text)
            else text
        )

        reInstanceClick()
        reInstanceHover()
    }

    private fun reInstanceClick() {
        if (clickAction == null || clickValue == null) return

        chatComponentText.getStyling()
            .chatClickEvent =
            TextClickEvent(
                ClickEventAction.getValueByCanonicalName(clickAction),
                if (formatted) ChatLib.addColor(clickValue)
                else clickValue
            )
    }

    private fun reInstanceHover() {
        if (hoverAction == null || hoverValue == null) return

        chatComponentText.getStyling()
            .chatHoverEvent =
            TextHoverEvent(
                HoverEventAction.getValueByCanonicalName(hoverAction),
                BaseTextComponent(
                    if (formatted) ChatLib.addColor(hoverValue)
                    else hoverValue
                )
            )
    }
}
