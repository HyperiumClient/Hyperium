package cc.hyperium.gui.buttons

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

class IconButton(
    buttonId: Int,
    private val x: Int,
    y: Int,
    widthIn: Int = 20,
    private val heightIn: Int = 20,
    private val tooltipName: String,
    private val location: ResourceLocation
) : GuiButton(buttonId, x, y, widthIn, heightIn, "") {

    override fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int) {
        if (!visible) return

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        hovered =
            (mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height)
        mouseDragged(mc, mouseX, mouseY)

        GlStateManager.enableTexture2D()
        if (hovered) {
            drawString(mc.fontRendererObj, tooltipName, x, heightIn + 8, -1)
        }

        mc.textureManager.bindTexture(location)

        Gui.drawModalRectWithCustomSizedTexture(
            xPosition,
            yPosition,
            0f,
            0f,
            width,
            height,
            width.toFloat(),
            height.toFloat()
        )
        GlStateManager.disableTexture2D()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
    }
}
