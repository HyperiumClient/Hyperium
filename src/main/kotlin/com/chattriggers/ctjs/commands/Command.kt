package com.chattriggers.ctjs.commands

import cc.hyperium.commands.BaseCommand
import com.chattriggers.ctjs.triggers.OnTrigger

class Command(trigger: OnTrigger, private val name: String, private val usage: String) : BaseCommand {
    private var triggers = mutableListOf(trigger)

    fun getTriggers() = this.triggers

    override fun getName() = this.name

    override fun getUsage() = this.usage

    override fun onExecute(args: Array<String>) = trigger(args)

    private fun trigger(args: Array<String>) {
        for (trigger in this.triggers)
            trigger.trigger(*args)
    }
}