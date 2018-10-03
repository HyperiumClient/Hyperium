package com.chattriggers.ctjs.commands

object CommandHandler {
    private var commandList = mutableListOf<Command>()
    @JvmStatic fun getCommandList() = this.commandList
}