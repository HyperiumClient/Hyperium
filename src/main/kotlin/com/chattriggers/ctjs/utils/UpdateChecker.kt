package com.chattriggers.ctjs.utils

import cc.hyperium.event.InvokeEvent
import cc.hyperium.event.RenderHUDEvent
import cc.hyperium.event.WorldLoadEvent
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.utils.config.Config
import com.chattriggers.ctjs.utils.kotlin.KotlinListener
import net.minecraft.client.renderer.GlStateManager

@KotlinListener
object UpdateChecker {
    private var worldLoaded = false
    private var updateAvailable = false
    private var warned = false

    init {
        getUpdate()
        warned = !Config.showUpdatesInChat
    }

    private fun getUpdate() {
        val fileName = "ctjs-" + Reference.MODVERSION + ".jar"
        val get = FileLib.getUrlContent("http://167.99.3.229/versions/latest/").trim { it <= ' ' }
        if ("" == get) return

        this.updateAvailable = fileName != get
    }

    @InvokeEvent
    fun worldLoad(event: WorldLoadEvent) {
        this.worldLoaded = true
    }

    @InvokeEvent
    fun renderOverlay(event: RenderHUDEvent) {
        if (!this.worldLoaded) return
        this.worldLoaded = false

        if (!this.updateAvailable || this.warned) return

        World.playSound("note.bass", 1000f, 1f)
        Message(
                "&c&m" + ChatLib.getChatBreak("-"),
                "\n",
                "&cChatTriggers requires an update to work properly!",
                "\n",
                TextComponent("&a[Download]").setClick("open_url", "https://www.chattriggers.com/#download"),
                " ",
                TextComponent("&e[Changelog]").setClick("open_url", "https://github.com/ChatTriggers/ct.js/releases"),
                "\n",
                "&c&m" + ChatLib.getChatBreak("-")
        ).chat()

        this.warned = true
    }

    fun drawUpdateMessage() {
        if (!this.updateAvailable) return

        GlStateManager.pushMatrix()

        Renderer.getFontRenderer()
                .drawString(
                        ChatLib.addColor("&cChatTriggers requires an update to work properly!"),
                        2f,
                        2f,
                        -0x1,
                        false
                )

        GlStateManager.popMatrix()
    }
}