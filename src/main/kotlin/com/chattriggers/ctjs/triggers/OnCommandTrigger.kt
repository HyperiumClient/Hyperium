package com.chattriggers.ctjs.triggers

import cc.hyperium.Hyperium
import com.chattriggers.ctjs.commands.Command
import com.chattriggers.ctjs.commands.CommandHandler
import com.chattriggers.ctjs.engine.ILoader

class OnCommandTrigger(method: Any, loader: ILoader) : OnTrigger(method, TriggerType.COMMAND, loader) {
    private var commandName: String? = null
    private var command: Command? = null

    override fun trigger(vararg args: Any?) {
        require(args::javaClass != Array<String>::javaClass) { "Arguments must be string array" }
        callMethod(*args)
    }

    /**
     * Sets the command name.<br></br>
     * Example:<br></br>
     * OnCommandTrigger.setCommandName("test")<br></br>
     * would result in the command being /test
     *
     * @param commandName The command name
     * @return the trigger for additional modification
     */
    fun setCommandName(commandName: String) = apply {
        this.commandName = commandName
        reInstance()
    }

    /**
     * Alias for [.setCommandName]
     *
     * @param commandName The command name
     * @return the trigger for additional modification
     */
    fun setName(commandName: String) = setCommandName(commandName)

    // helper method to re instance the command
    private fun reInstance() {
        for (command in CommandHandler.getCommandList()) {
            if (command.name == commandName) {
                command.getTriggers().add(this)
                return
            }
        }

        command = Command(this, commandName!!, "/${commandName}")
        Hyperium.INSTANCE.handlers.hyperiumCommandHandler.registerCommand(command!!)
        CommandHandler.getCommandList().add(command ?: return)
    }
}
