package com.chattriggers.ctjs.minecraft.objects.message

import cc.hyperium.event.ServerChatEvent
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.utils.kotlin.BaseTextComponent
import com.chattriggers.ctjs.utils.kotlin.ChatPacket
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.ITextComponent

@External
class Message {
    private lateinit var chatMessage: ITextComponent

    private var messageParts = mutableListOf<TextComponent>()
    private var chatLineId = -1
    private var recursive = false
    private var formatted = true

    /**
     * Creates a new Message object from a chat event.
     *
     * @param event the chat event
     */
    constructor(event: ServerChatEvent): this(event.chat)

    /**
     * Creates a new Message object from an IChatComponent.
     *
     * @param component the IChatComponent
     */
    constructor(component: ITextComponent) {
        if (component.siblings.isEmpty()) {
            this.messageParts.add(TextComponent(component))
        } else {
            this.messageParts.addAll(component.siblings.map { TextComponent(it) })
        }
    }

    /**
     * Creates a new Message object in parts of TextComponents or Strings.
     *
     * @param messageParts the list of TextComponents or Strings
     */
    constructor(messageParts: ArrayList<Any>) {
        this.messageParts.addAll(messageParts.map{
            when (it) {
                is String -> TextComponent(it)
                is TextComponent -> it
                else -> return
            }
        })
    }

    /**
     * Creates a new Message object in parts of TextComponents or Strings.
     *
     * @param components the TextComponents or Strings
     */
    constructor(vararg components: Any): this(ArrayList(components.asList()))



    fun getChatMessage(): ITextComponent {
        parseMessage()
        return this.chatMessage
    }

    /**
     * Gets the message TextComponent parts as a list.
     *
     * @return the message parts
     */
    fun getMessageParts(): List<TextComponent> = this.messageParts

    fun getChatLineId(): Int = this.chatLineId
    fun setChatLineId(id: Int) = apply { this.chatLineId = id }

    fun isRecursive(): Boolean = this.recursive
    fun setRecursive(recursive: Boolean) = apply { this.recursive = recursive }

    fun isFormatted(): Boolean = this.formatted
    fun setFormatted(formatted: Boolean) = apply { this.formatted = formatted }

    /**
     * Sets the TextComponent or String in the Message at index.
     *
     * @param index    the index of the TextComponent or String to change
     * @param component the new TextComponent or String to replace with
     * @return the Message for method chaining
     */
    fun setTextComponent(index: Int, component: Any) = apply {
        when (component) {
            is String -> this.messageParts[index] = TextComponent(component)
            is TextComponent -> this.messageParts[index] = component
        }
    }

    /**
     * Adds a TextComponent or String to the end of the Message.
     *
     * @param component the new TextComponent or String to add
     * @return the Message for method chaining
     */
    fun addTextComponent(component: Any) = apply {
        when (component) {
            is String -> this.messageParts.add(TextComponent(component))
            is TextComponent -> this.messageParts.add(component)
        }
    }

    /**
     * Adds a TextComponent or String at index of the Message.
     *
     * @param index the index to insert the new TextComponent or String
     * @param component the new TextComponent or String to insert
     * @return the Message for method chaining
     */
    fun addTextComponent(index: Int, component: Any) = apply {
        when (component) {
            is String -> this.messageParts.add(index, TextComponent(component))
            is TextComponent -> this.messageParts.add(index, component)
        }
    }

    fun clone(): Message = copy()
    fun copy(): Message {
        val copy = Message(this.messageParts)
                .setChatLineId(this.chatLineId)
        copy.recursive = this.recursive
        copy.formatted = this.formatted
        return copy
    }

    /**
     * Edits this message (once it is already sent)
     *
     * @param replacements the new message(s) to be put in place of the old one
     */
    fun edit(vararg replacements: Message) {
        ChatLib.editChat(this, *replacements)
    }

    /**
     * Outputs the Message into the client's chat.
     */
    fun chat() {
        parseMessage()
        if (!ChatLib.isPlayer("[CHAT]: " + this.chatMessage.formattedText)) return

        if (this.chatLineId != -1) {
            Client.getChatGUI()?.printChatMessageWithOptionalDeletion(this.chatMessage, this.chatLineId)
            return
        }

        //#if MC<=10809
        if (this.recursive) {
           Client.getConnection().handleChat(ChatPacket(this.chatMessage, 0))
        } else {
           Player.getPlayer()?.addChatMessage(this.chatMessage)
        }
        //#else
        //$$ if (this.recursive) {
        //$$    Client.getConnection().handleChat(ChatPacket(this.chatMessage, ChatType.CHAT))
        //$$ } else {
        //$$    Player.getEntity()?.sendMessage(this.chatMessage)
        //$$ }
        //#endif
    }

    /**
     * Outputs the Message into the client's action bar.
     */
    fun actionBar() {
        parseMessage()
        if (!ChatLib.isPlayer("[ACTION BAR]: " + this.chatMessage.formattedText)) return

        Client.getConnection().handleChat(
                ChatPacket(
                        this.chatMessage,
                        //#if MC<=10809
                        2
                        //#else
                        //$$ ChatType.GAME_INFO
                        //#endif
                )
        )
    }

    private fun parseMessage() {
        this.chatMessage = BaseTextComponent("")

        this.messageParts.map {
            this.chatMessage.appendSibling(it.chatComponentText)
        }
    }
}