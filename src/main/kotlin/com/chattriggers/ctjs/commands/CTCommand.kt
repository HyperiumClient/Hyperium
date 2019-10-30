package com.chattriggers.ctjs.commands

import cc.hyperium.commands.BaseCommand
import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.ModuleManager
import com.chattriggers.ctjs.engine.module.ModulesGui
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.listeners.ChatListener
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.utils.config.GuiConfig
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.IOException
import java.util.*

object CTCommand : BaseCommand {
    private const val idFixed = 90123 // ID for dumped chat
    private var idFixedOffset = -1 // ID offset (increments)

    override fun getUsage() = getRealUsage()

    override fun getName() = "chattriggers"

    override fun getCommandAliases() = mutableListOf("ct")

    override fun onExecute(args: Array<String>) = run(args)

    private fun run(args: Array<String>) {
        if (args.isEmpty()) {
            ChatLib.chat(usage)
            return
        }

        when (args[0].toLowerCase()) {
            "load" -> Reference.load()
            "reload" -> Reference.reload()
            "files", "file" -> openFileLocation()
            "import" ->
                if (args.size == 1) ChatLib.chat("&c/ct import [module name]")
                else ModuleManager.importModule(args[1])
            "delete" ->
                if (args.size == 1) ChatLib.chat("&c/ct delete [module name]")
                else ChatLib.chat((if (ModuleManager.deleteModule(args[1])) "&aDeleted " else "&cFailed to delete ") + args[1])
            "modules" -> GuiHandler.openGui(ModulesGui)
            "console" ->
                if (args.size == 1) ModuleManager.generalConsole.showConsole()
                else ModuleManager.getConsole(args[1]).showConsole()
            "config", "settings", "setting" ->
                GuiHandler.openGui(GuiConfig())
            "sim", "simulate" ->
                ChatLib.simulateChat(Arrays.copyOfRange(args, 1, args.size).joinToString(" "))
            "dump" -> dumpChat(args)
            "copy" -> copyArgsToClipboard(args)
            else -> ChatLib.chat(usage)
        }
    }

    private fun getRealUsage() =
        "&b&m${ChatLib.getChatBreak()}\n" +
                "&c/ct <load/reload> &7- &oReloads all of the ct modules.\n" +
                "&c/ct import [module] &7- &oImports a module.\n" +
                "&c/ct files &7- &oOpens the ChatTriggers folder.\n" +
                "&c/ct modules &7- &oOpens the modules Gui\n" +
                "&c/ct console &7- &oOpens the ct console.\n" +
                "&c/ct simulate [message]&7- &oSimulates a received chat message.\n" +
                "&c/ct dump &7- &oDumps previous chat messages into chat.\n" +
                "&c/ct settings &7- &oChange ChatTrigger's settings.\n" +
                "&c/ct &7- &oDisplays this help dialog.\n" +
                "&b&m${ChatLib.getChatBreak()}"

    private fun openFileLocation() {
        try {
            Desktop.getDesktop().open(CTJS.configLocation)
        } catch (exception: IOException) {
            ModuleManager.generalConsole.printStackTrace(exception)
            ChatLib.chat("&cCould not open file location")
        }
    }

    private fun dumpChat(args: Array<String>) {
        var toDump = if (args.size > 1) args[1].toInt() else 100

        clearOldDump()
        val messages = ChatListener.chatHistory
        if (toDump > messages.size) toDump = messages.size

        Message("&6&m${ChatLib.getChatBreak()}").setChatLineId(idFixed).chat()
        var msg: String
        for (i in 0 until toDump) {
            msg = ChatLib.replaceFormatting(messages[messages.size - toDump + i])
            Message(
                TextComponent(msg)
                    .setClick("run_command", "/ct copy $msg")
                    .setHoverValue(ChatLib.addColor("&eClick here to copy this message."))
                    .setFormatted(false)
            ).setFormatted(false).setChatLineId(idFixed + i + 1).chat()
        }
        Message("&6&m${ChatLib.getChatBreak()}").setChatLineId(idFixed + toDump + 1).chat()

        idFixedOffset = idFixed + toDump + 1
    }

    private fun clearOldDump() {
        if (idFixedOffset == -1) return
        while (idFixedOffset >= idFixed)
            ChatLib.clearChat(idFixedOffset--)
        idFixedOffset = -1
    }

    private fun copyArgsToClipboard(args: Array<String>) {
        clearOldDump()
        val toCopy = Arrays.copyOfRange(args, 1, args.size).joinToString(" ")
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(toCopy), null)
    }
}
