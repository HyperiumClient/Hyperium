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
        this.chatComponentText = chatComponent
        this.text = this.chatComponentText.formattedText

        val chatStyle = chatComponent.getStyling()

        val clickEvent = chatStyle.getClick()
        this.clickAction = clickEvent?.action?.canonicalName
        this.clickValue = clickEvent?.value

        val hoverEvent = chatStyle.getHover()
        this.hoverAction = hoverEvent?.action?.canonicalName
        this.hoverValue = hoverEvent?.value?.formattedText
    }

    fun getText(): String = this.text
    fun setText(text: String) = apply {
        this.text = text
        reInstance()
    }

    fun isFormatted(): Boolean = this.formatted
    fun setFormatted(formatted: Boolean) = apply {
        this.formatted = formatted
        reInstance()
    }

    fun setClick(action: String, value: String) = apply {
        this.clickAction = action
        this.clickValue = value
        reInstanceClick()
    }

    fun getClickAction(): String? = this.clickAction
    fun setClickAction(action: String) = apply {
        this.clickAction = action
        reInstanceClick()
    }

    fun getClickValue(): String? = this.clickValue
    fun setClickValue(value: String) = apply {
        this.clickValue = value
        reInstanceClick()
    }

    fun setHover(action: String, value: String) = apply {
        this.hoverAction = action
        this.hoverValue = value
        reInstanceHover()
    }

    fun getHoverAction(): String? = this.hoverAction
    fun setHoverAction(action: String) = apply {
        this.hoverAction = action
        reInstanceHover()
    }

    fun getHoverValue(): String? = this.hoverValue
    fun setHoverValue(value: String) = apply {
        this.hoverValue = value
        reInstanceHover()
    }

    fun chat() = Message(this).chat()
    fun actionBar() = Message(this).actionBar()

    private fun reInstance() {
        this.chatComponentText = BaseTextComponent(
                        if (this.formatted) ChatLib.addColor(this.text)
                        else this.text
                )

        reInstanceClick()
        reInstanceHover()
    }

    private fun reInstanceClick() {
        if (this.clickAction == null || this.clickValue == null) return

        this.chatComponentText.getStyling()
                //#if MC<=10809
                .chatClickEvent =
                //#else
                //$$ .clickEvent =
                //#endif
                TextClickEvent(
                        ClickEventAction.getValueByCanonicalName(this.clickAction),
                        if (this.formatted) ChatLib.addColor(this.clickValue)
                        else this.clickValue
                )
    }

    private fun reInstanceHover() {
        if (this.hoverAction == null || this.hoverValue == null) return

        this.chatComponentText.getStyling()
                //#if MC<=10809
                .chatHoverEvent =
                //#else
                //$$ .hoverEvent =
                //#endif
                TextHoverEvent(
                        HoverEventAction.getValueByCanonicalName(this.hoverAction),
                        BaseTextComponent(
                                if (this.formatted) ChatLib.addColor(this.hoverValue)
                                else this.hoverValue
                        )
                )
    }
}
