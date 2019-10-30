package com.chattriggers.ctjs

import cc.hyperium.Hyperium
import com.chattriggers.ctjs.commands.Command
import com.chattriggers.ctjs.commands.CommandHandler
import com.chattriggers.ctjs.engine.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.config.Config

object Reference {
    const val MODID = "ct.js"
    const val MODNAME = "ChatTriggers"
    const val MODVERSION = "@MOD_VERSION@"

    private var isLoaded = true

    fun reload() {
        load(true)
    }

    @JvmOverloads
    fun load(updateCheck: Boolean = false) {
        if (!isLoaded) return
        isLoaded = false

        TriggerType.GAME_UNLOAD.triggerAll()
        TriggerType.WORLD_UNLOAD.triggerAll()

        ChatLib.chat("&cReloading ChatTriggers...")
        Thread {
            DisplayHandler.clearDisplays()
            GuiHandler.clearGuis()

            CommandHandler.getCommandList().clear()

            Hyperium.INSTANCE.handlers.hyperiumCommandHandler.commands.entries.removeIf {
                it.value is Command
            }

            ModuleManager.unload()

            if (Config.clearConsoleOnLoad) {
                ModuleManager.loaders.forEach {
                    it.console.clearConsole()
                }

                ModuleManager.generalConsole.clearConsole()
            }

            CTJS.loadConfig()

            ModuleManager.load(updateCheck)

            ChatLib.chat("&aDone reloading ChatTriggers!")

            TriggerType.WORLD_LOAD.triggerAll()
            isLoaded = true
        }.start()
    }
}
