package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.CTJS
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import java.io.IOException
import java.util.*
import kotlin.reflect.full.declaredMemberProperties

class GuiConfig : GuiScreen() {
    private val configOptions: ArrayList<ConfigOption> = ArrayList()
    private var isOpen = false
    private val iconHandler = IconHandler

    init {
        Config::class.declaredMemberProperties.forEach { prop ->
            prop.annotations.firstOrNull { ann ->
                ann.annotationClass == ConfigOpt::class
            }?.let { ann ->
                val opt = ann as ConfigOpt
                configOptions.add(
                        opt.type.constructors.first().call(
                                prop,
                                opt.name,
                                opt.x,
                                opt.y
                        ) as ConfigOption
                )
            }
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        GlStateManager.pushMatrix()

        if (!this.isOpen) {
            this.isOpen = true
            for (configOption in this.configOptions)
                configOption.init()
        }

        drawBackground(0)

        for (configOption in this.configOptions)
            configOption.draw(mouseX, mouseY, partialTicks)

        iconHandler.drawIcons()

        GlStateManager.popMatrix()
    }

    override fun onGuiClosed() {
        this.isOpen = false
        CTJS.saveConfig()
    }

    @Throws(IOException::class)
    public override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (mouseButton != 0) return

        for (configOption in this.configOptions)
            configOption.mouseClicked(mouseX, mouseY)

        iconHandler.clickIcons(mouseX, mouseY)
    }

    public override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        for (configOption in this.configOptions)
            configOption.mouseReleased()
    }

    @Throws(IOException::class)
    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)

        for (configOption in this.configOptions)
            configOption.keyTyped(typedChar, keyCode)
    }
}