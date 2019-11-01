package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.engine.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.renderer.Image
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.World
import java.awt.Desktop
import java.net.URL

object IconHandler {
    private var icons = mutableListOf<Icon>()

    init {
        val imageUrl = "http://167.99.3.229/assets/images/"

        icons.add(Icon(Image("CT_logo.png", imageUrl + "logo-icon.png"), "https://chattriggers.com/"))
        icons.add(
            Icon(
                Image("CT_Patreon.png", imageUrl + "Patreon-dark.png"),
                "https://www.patreon.com/ChatTriggers",
                1
            )
        )
        icons.add(
            Icon(
                Image("CT_Github.png", imageUrl + "github-dark.png"),
                "https://github.com/ChatTriggers/ct.js",
                2
            )
        )
        icons.add(
            Icon(
                Image("CT_Discord.png", imageUrl + "discord-dark.png"),
                "https://discordapp.com/invite/0fNjZyopOvBHZyG8",
                3
            )
        )
    }

    fun drawIcons() = icons.forEach { it.draw() }
    fun clickIcons(x: Int, y: Int) = icons.forEach { it.click(x, y) }

    private class Icon(private var image: Image, private var url: String, private var y: Int = -1) {
        fun draw() {
            if (y == -1)
                image.draw(0.0, (Renderer.Screen.getHeight() - 65).toDouble(), 64.0, 64.0)
            else
                image.draw(
                    65.0,
                    (Renderer.Screen.getHeight() - y * 21.3f).toInt().toDouble(),
                    (64 / 3).toDouble(),
                    (64 / 3).toDouble()
                )
        }

        fun click(x: Int, y: Int) {
            var ix = 65f
            var iy = Renderer.Screen.getHeight() - this.y * 21.3f
            var size = 64f / 3f

            if (this.y == -1) {
                ix = 0f
                iy = Renderer.Screen.getHeight() - 65f
                size = 64f
            }

            if (x > ix && x < ix + size && y > iy && y < iy + size) {
                try {
                    Desktop.getDesktop().browse(URL(url).toURI())
                    World.playSound("Gui.button.press", 100f, 1f)
                } catch (exception: Exception) {
                    ModuleManager.generalConsole.printStackTrace(exception)
                }
            }
        }
    }
}
