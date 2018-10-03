package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.engine.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.minecraft.wrappers.Player
import net.minecraft.client.gui.GuiScreen

object ModulesGui : GuiScreen() {
    private val title = object {
        var text = Text("Modules").setScale(2f).setShadow(true)
        var exit = Text(ChatLib.addColor("&cx")).setScale(2f)
    }

    override fun doesGuiPauseGame() = false

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)

        val middle = Renderer.screen.getWidth() / 2f
        var width = Renderer.screen.getWidth() - 100f
        if (width > 500) width = 500f

        Renderer.drawRect(0x50000000, 0f, 0f, Renderer.screen.getWidth().toFloat(), Renderer.screen.getHeight().toFloat())
        Renderer.drawRect(0x50000000, middle - width / 2f, 95f, width, Renderer.screen.getHeight() - 195f)

        Renderer.drawRect(0xaa000000.toInt(), middle - width / 2f, 95f, width, 25f)
        title.text.draw(middle - width / 2f + 5, 100f)
        title.exit.setString(ChatLib.addColor("&cx")).draw(middle + width / 2f - 17, 99f)

        var i = 125f
        ModuleManager.cachedModules.forEach {
            i += it.draw(middle - width / 2f, i, width)
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseClicked(mouseX, mouseY, button)

        var width = Renderer.screen.getWidth() - 100f
        if (width > 500) width = 500f

        if (mouseX > Renderer.screen.getWidth() / 2f + width / 2f - 25 && mouseX < Renderer.screen.getWidth() / 2f + width / 2f
        && mouseY > 95 && mouseY < 120) {
            Player.getPlayer()?.closeScreen()
        }

        ModuleManager.cachedModules.forEach {
            it.click(mouseX, mouseY, width)
        }
    }
}