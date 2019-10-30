package com.chattriggers.ctjs.commands

import cc.hyperium.commands.BaseCommand
import com.chattriggers.ctjs.triggers.OnTrigger

class Command(trigger: OnTrigger, private val name: String, private val usage: String) : BaseCommand {
    private var triggers = mutableListOf(trigger)

    fun getTriggers() = triggers

    override fun getName() = name

    override fun getUsage() = usage

    override fun onExecute(args: Array<String>) = trigger(args)

    private fun trigger(args: Array<String>) {
        for (trigger in triggers)
            trigger.trigger(*args)
    }
}
