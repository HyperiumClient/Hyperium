package com.chattriggers.ctjs.triggers

import cc.hyperium.event.network.chat.ServerChatEvent
import com.chattriggers.ctjs.engine.ILoader
import java.util.*
import java.util.regex.Pattern

class OnChatTrigger(method: Any, type: TriggerType, loader: ILoader) : OnTrigger(method, type, loader) {
    private var chatCriteria: String = ""
    private var criteriaPattern: Pattern? = null
    private var parameters = mutableListOf<Parameter?>()
    private var triggerIfCanceled: Boolean = true

    /**
     * Sets if the chat trigger should run if the chat event has already been canceled.
     * True by default.
     * @param bool Boolean to set
     * @return the trigger object for method chaining
     */
    fun triggerIfCanceled(bool: Boolean) = apply { this.triggerIfCanceled = bool }

    /**
     * Sets the chat criteria for [.matchesChatCriteria].<br></br>
     * Arguments for the trigger's method can be passed in using ${variable}.<br></br>
     * Example: `OnChatTrigger.setChatCriteria("<${name}> ${message}");`<br></br>
     * Use ${*} to match a chat message but ignore the pass through.
     * @param chatCriteria the chat criteria to set
     * @return the trigger object for method chaining
     */
    fun setChatCriteria(chatCriteria: String) = apply {
        this.chatCriteria = chatCriteria

        val replacedCriteria = chatCriteria.replace("\n", "->newLine<-").let { Pattern.quote(it) }
            .replace("\\$\\{[^*]+?}".toRegex(), "\\\\E(.+)\\\\Q")
            .replace("\\$\\{\\*?}".toRegex(), "\\\\E(?:.+)\\\\Q")

        this.criteriaPattern = Pattern.compile(if ("" == chatCriteria) ".+" else replacedCriteria)
    }

    /**
     * Alias for [.setChatCriteria].
     * @param chatCriteria the chat criteria to set
     * @return the trigger object for method chaining
     */
    fun setCriteria(chatCriteria: String) = setChatCriteria(chatCriteria)

    /**
     * Sets the chat parameter for [Parameter].
     * Clears current parameter list.
     * @param parameter the chat parameter to set
     * @return the trigger object for method chaining
     */
    fun setParameter(parameter: String) = apply {
        this.parameters = mutableListOf(Parameter.getParameterByName(parameter))
    }

    /**
     * Sets multiple chat parameters for [Parameter].
     * Clears current parameter list.
     * @param parameters the chat parameters to set
     * @return the trigger object for method chaining
     */
    fun setParameters(vararg parameters: String) = apply {
        this.parameters.clear()
        addParameters(*parameters)
    }

    /**
     * Adds chat parameter for [Parameter].
     * @param parameter the chat parameter to add
     * @return the trigger object for method chaining
     */
    fun addParameter(parameter: String) = apply {
        this.parameters.add(Parameter.getParameterByName(parameter))
    }

    /**
     * Adds multiple chat parameters for [Parameter].
     * @param parameters the chat parameters to add
     * @return the trigger object for method chaining
     */
    fun addParameters(vararg parameters: String) = apply {
        parameters.forEach { this.parameters.add(Parameter.getParameterByName(it)) }
    }

    /**
     * Argument 1 (String) The chat message received
     * Argument 2 (ClientChatReceivedEvent) the chat event fired
     * @param args list of arguments as described
     */
    override fun trigger(vararg args: Any?) {
        if (args[0] !is String || args[1] !is ServerChatEvent)
            throw IllegalArgumentException("Argument 1 must be a String, Argument 2 must be a ClientChatReceivedEvent")

        val chatEvent = args[1] as ServerChatEvent

        if (!this.triggerIfCanceled && chatEvent.isCancelled) return

        val chatMessage = getChatMessage(chatEvent, args[0] as String)

        val variables = getVariables(chatMessage) ?: return
        variables.add(chatEvent)

        callMethod(*variables.toTypedArray())
    }

    // helper method to get the proper chat message based on the presence of color codes
    private fun getChatMessage(chatEvent: ServerChatEvent, chatMessage: String) =
        if (chatCriteria.contains("&"))
            chatEvent.chat.formattedText.replace("\u00a7", "&")
        else chatMessage

    // helper method to get the variables to pass through
    private fun getVariables(chatMessage: String) =
        if ("" != chatCriteria) matchesChatCriteria(chatMessage.replace("\n", "->newLine<-")) else ArrayList()

    /**
     * A method to check whether or not a received chat message
     * matches this trigger's definition criteria.
     * Ex. "FalseHonesty joined Cops vs Crims" would match `${playername} joined ${gamejoined}`
     * @param chat the chat message to compare against
     * @return a list of the variables, in order or null if it doesn't match
     */
    private fun matchesChatCriteria(chat: String): MutableList<Any>? {
        val matcher = criteriaPattern!!.matcher(chat)

        if (parameters.isEmpty()) {
            if (!matcher.matches()) return null
        } else {
            for (parameter in parameters) {
                when {
                    parameter == Parameter.CONTAINS -> if (!matcher.find()) return null
                    parameter == Parameter.START -> if (!matcher.find() || matcher.start() != 0) return null
                    parameter == Parameter.END -> {
                        var endMatch = -1
                        while (matcher.find())
                            endMatch = matcher.end()
                        if (endMatch != chat.length) return null
                    }
                    parameter == null && !matcher.matches() -> return null
                }
            }
        }

        val variables = ArrayList<Any>()

        for (i in 1..matcher.groupCount()) {
            variables.add(matcher.group(i))
        }

        return variables
    }

    /**
     * The parameter to match chat criteria to.<br></br>
     * Location parameters<br></br>
     * **contains**<br></br>
     * **start**<br></br>
     * **end**<br></br>
     */
    enum class Parameter constructor(vararg names: String) {
        CONTAINS("<c>", "<contains>", "c", "contains"),
        START("<s>", "<start>", "s", "start"),
        END("<e>", "<end>", "e", "end");

        var names: List<String> = listOf(*names)

        companion object {
            fun getParameterByName(name: String): Parameter? {
                for (parameter in values()) {
                    for (paramName in parameter.names) {
                        if (paramName.equals(name, ignoreCase = true)) return parameter
                    }
                }

                return null
            }
        }
    }
}
