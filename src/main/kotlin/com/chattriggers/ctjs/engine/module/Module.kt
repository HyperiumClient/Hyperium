package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.engine.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.utils.config.Config
import java.io.File

class Module(val name: String, val metadata: ModuleMetadata, val folder: File) {
    private val gui = object {
        var collapsed = true
        var x = 0f
        var y = 0f
        var description = Text(metadata.description ?: "No description provided in the metadata")
    }

    fun getFilesWithExtension(type: String): List<File> {
        return this.folder.walkTopDown().filter {
            it.name.endsWith(type)
        }.filter {
            if (this.metadata.ignored == null) return@filter true

            for (ignore: String in this.metadata.ignored) {
                if (ignore in it.name) return@filter false
            }

            return@filter true
        }.filter {
            it.isFile
        }.toList()
    }

    fun draw(x: Float, y: Float, width: Float): Float {
        gui.x = x
        gui.y = y

        Renderer.drawRect(
                0xaa000000.toInt(),
                x, y, width, 13f)
        Renderer.drawStringWithShadow(
                metadata.name ?: name,
                x + 3, y + 3)

        return if (gui.collapsed) {
            Renderer.translate(x + width - 5, y + 8)
            Renderer.rotate(180f)
            Renderer.drawString("^", 0f, 0f)

            15f
        } else {
            gui.description.setWidth(width.toInt() - 5)

            Renderer.drawRect(0x50000000, x, y + 13, width, gui.description.getHeight() + 12)
            Renderer.drawString("^", x + width - 10, y + 5)

            gui.description.draw(x + 3, y + 15)

            if (metadata.version != null) {
                Renderer.drawStringWithShadow(
                        ChatLib.addColor("&8v" + (metadata.version)),
                        x + width - Renderer.getStringWidth(ChatLib.addColor("&8v" + metadata.version)), y + gui.description.getHeight() + 15)
            }

            Renderer.drawStringWithShadow(
                    ChatLib.addColor(if (metadata.isRequired) "&8required" else "&4[delete]"),
                    x + 3, y + gui.description.getHeight() + 15
            )

            gui.description.getHeight() + 27
        }
    }

    fun click(x: Int, y: Int, width: Float) {
        if (x > gui.x && x < gui.x + width
        && y > gui.y && y < gui.y + 13) {
            gui.collapsed = !gui.collapsed
            return
        }

        if (gui.collapsed) return

        if (x > gui.x && x < gui.x + 45
        && y > gui.y + gui.description.getHeight() + 15 && y < gui.y + gui.description.getHeight() + 25) {
            ModuleManager.deleteModule(name)
        }
    }

    override fun toString() = "Module{name=$name,folder=$folder,metadata=$metadata}"
}