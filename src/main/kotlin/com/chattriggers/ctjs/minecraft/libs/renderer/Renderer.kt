package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.MathLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP
import com.chattriggers.ctjs.utils.kotlin.MCTessellator
import com.chattriggers.ctjs.utils.kotlin.getRenderer
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.EntityLivingBase
import java.io.File
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

object Renderer {
    var colorized = false

    @JvmStatic val BLACK = color(0, 0, 0, 255)
    @JvmStatic val DARK_BLUE = color(0, 0, 190, 255)
    @JvmStatic val DARK_GREEN = color(0, 190, 0, 255)
    @JvmStatic val DARK_AQUA = color(0, 190, 190, 255)
    @JvmStatic val DARK_RED = color(190, 0, 0, 255)
    @JvmStatic val DARK_PURPLE = color(190, 0, 190, 255)
    @JvmStatic val GOLD = color(217, 163, 52, 255)
    @JvmStatic val GRAY = color(190, 190, 190, 255)
    @JvmStatic val DARK_GRAY = color(63, 63, 63, 255)
    @JvmStatic val BLUE = color(63, 63, 254, 255)
    @JvmStatic val GREEN = color(63, 254, 63, 255)
    @JvmStatic val AQUA = color(63, 254, 254, 255)
    @JvmStatic val RED = color(254, 63, 63, 255)
    @JvmStatic val LIGHT_PURPLE = color(254, 63, 254, 255)
    @JvmStatic val YELLOW = color(254, 254, 63, 255)
    @JvmStatic val WHITE = color(255, 255, 255, 255)

    @JvmStatic
    fun getColor(color: Int): Int {
        return when (color) {
            0 -> BLACK
            1 -> DARK_BLUE
            2 -> DARK_GREEN
            3 -> DARK_AQUA
            4 -> DARK_RED
            5 -> DARK_PURPLE
            6 -> GOLD
            7 -> GRAY
            8 -> DARK_GRAY
            9 -> BLUE
            10 -> GREEN
            11 -> AQUA
            12 -> RED
            13 -> LIGHT_PURPLE
            14 -> YELLOW
            else -> WHITE
        }
    }

    @JvmStatic
    fun getFontRenderer(): FontRenderer {
        //#if MC<=10809
        return Client.getMinecraft().fontRendererObj
        //#else
        //$$ return Client.getMinecraft().fontRenderer
        //#endif
    }

    @JvmStatic @JvmOverloads
    fun getStringWidth(text: String, removeFormatting: Boolean = true): Int {
        return if (removeFormatting)
            getFontRenderer().getStringWidth(ChatLib.removeFormatting(text))
            else getFontRenderer().getStringWidth(text)
    }

    @JvmStatic @JvmOverloads
    fun color(red: Int, green: Int, blue: Int, alpha: Int = 255): Int {
        return (MathLib.clamp(alpha, 0, 255) * 0x1000000
                + MathLib.clamp(red, 0, 255) * 0x10000
                + MathLib.clamp(green, 0, 255) * 0x100
                + MathLib.clamp(blue, 0, 255))
    }

    @JvmStatic @JvmOverloads
    fun getRainbow(step: Float, speed: Float = 1f): Int {
        val red = ((Math.sin((step / speed).toDouble()) + 0.75) * 170).toInt()
        val green = ((Math.sin(step / speed + 2 * Math.PI / 3) + 0.75) * 170).toInt()
        val blue = ((Math.sin(step / speed + 4 * Math.PI / 3) + 0.75) * 170).toInt()
        return color(red, green, blue, 255)
    }

    @JvmStatic @JvmOverloads
    fun getRainbowColors(step: Float, speed: Float = 1f): IntArray {
        val red = ((Math.sin((step / speed).toDouble()) + 0.75) * 170).toInt()
        val green = ((Math.sin(step / speed + 2 * Math.PI / 3) + 0.75) * 170).toInt()
        val blue = ((Math.sin(step / speed + 4 * Math.PI / 3) + 0.75) * 170).toInt()
        return intArrayOf(red, green, blue)
    }

    @JvmStatic
    fun translate(x: Float, y: Float) {
        GlStateManager.translate(x.toDouble(), y.toDouble(), 0.toDouble())
    }

    @JvmStatic @JvmOverloads
    fun scale(scaleX: Float, scaleY: Float = scaleX) {
        GlStateManager.scale(scaleX, scaleY, 1f)
    }

    @JvmStatic
    fun rotate(angle: Float) {
        GlStateManager.rotate(angle, 0f, 0f, 1f)
    }

    @JvmStatic @JvmOverloads
    fun colorize(red: Int, green: Int, blue: Int, alpha: Int = 255) {
        this.colorized = true

        GlStateManager.color(
                MathLib.clamp(red, 0, 255).toFloat(),
                MathLib.clamp(green, 0, 255).toFloat(),
                MathLib.clamp(blue, 0, 255).toFloat(),
                MathLib.clamp(alpha, 0, 255).toFloat()
        )
    }

    @JvmStatic
    fun image(name: String, url: String): Image? = loadImage(name, url)
    @JvmStatic
    fun text(text: String, x: Float, y: Float) = Text(text, x, y)
    @JvmStatic
    fun text(text: String) = Text(text)
    @JvmStatic
    fun rectangle(color: Int, x: Float, y: Float, width: Float, height: Float) = Rectangle(color, x, y, width, height)
    @JvmStatic
    fun shape(color: Int) = Shape(color)

    private fun loadImage(name: String, url: String): Image? {
        val resourceFile = File(CTJS.assetsDir, name)

        if (resourceFile.exists()) {
            return Image(ImageIO.read(resourceFile))
        }

        val image = ImageIO.read(URL(url))
        ImageIO.write(image, "png", resourceFile)
        return Image(image)
    }

    private fun loadImage(path: String): Image? {
        val file = File(path)

        return Image(ImageIO.read(file))
    }

    @JvmStatic
    fun drawRect(color: Int, x: Float, y: Float, width: Float, height: Float) {
        val pos = mutableListOf(x, y, x + width, y + height)
        if (pos[0] > pos[2])
            Collections.swap(pos, 0, 2)
        if (pos[1] > pos[3])
            Collections.swap(pos, 1, 3)

        val a = (color shr 24 and 255).toFloat() / 255.0f
        val r = (color shr 16 and 255).toFloat() / 255.0f
        val g = (color shr 8 and 255).toFloat() / 255.0f
        val b = (color and 255).toFloat() / 255.0f

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()

        val tessellator = MCTessellator.getInstance()
        val worldRenderer = tessellator.getRenderer()

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        if (!Renderer.colorized)
            GlStateManager.color(r, g, b, a)
        worldRenderer.begin(7, DefaultVertexFormats.POSITION)
        worldRenderer.pos(pos[0].toDouble(), pos[3].toDouble(), 0.0).endVertex()
        worldRenderer.pos(pos[2].toDouble(), pos[3].toDouble(), 0.0).endVertex()
        worldRenderer.pos(pos[2].toDouble(), pos[1].toDouble(), 0.0).endVertex()
        worldRenderer.pos(pos[0].toDouble(), pos[1].toDouble(), 0.0).endVertex()
        tessellator.draw()
        GlStateManager.color(1f, 1f, 1f, 1f)

        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        finishDraw()
    }

    @JvmStatic
    fun drawString(text: String, x: Float, y: Float) {
        getFontRenderer().drawString(ChatLib.addColor(text), x, y, 0xffffffff.toInt(), false)
        finishDraw()
    }

    @JvmStatic
    fun drawStringWithShadow(text: String, x: Float, y: Float) {
        getFontRenderer().drawString(ChatLib.addColor(text), x, y, 0xffffffff.toInt(), true)
        finishDraw()
    }

    @JvmStatic
    fun drawImage(image: Image, x: Double, y: Double, width: Double, height: Double) {
        if (image.getTexture() == null) return

        if (!Renderer.colorized)
            GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableBlend()
        GlStateManager.scale(1f, 1f, 50f)
        GlStateManager.bindTexture(image.getTexture().glTextureId)
        GlStateManager.enableTexture2D()

        val tessellator = MCTessellator.getInstance()
        val worldRenderer = tessellator.getRenderer()

        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX)

        worldRenderer.pos(x, y + height, 0.0).tex(0.0, 1.0).endVertex()
        worldRenderer.pos(x + width, y + height, 0.0).tex(1.0, 1.0).endVertex()
        worldRenderer.pos(x + width, y, 0.0).tex(1.0, 0.0).endVertex()
        worldRenderer.pos(x, y, 0.0).tex(0.0, 0.0).endVertex()
        tessellator.draw()

        finishDraw()
    }

    @JvmStatic
    fun drawPlayer(player: Any, x: Int, y: Int, rotate: Boolean) {
        val mouseX = -30f
        val mouseY = 0f

        var ent: EntityLivingBase? = Player.getPlayer()
        if (player is PlayerMP)
            ent = player.player

        GlStateManager.enableColorMaterial()
        RenderHelper.enableStandardItemLighting()

        val f = ent!!.renderYawOffset
        val f1 = ent.rotationYaw
        val f2 = ent.rotationPitch
        val f3 = ent.prevRotationYawHead
        val f4 = ent.rotationYawHead

        GlStateManager.translate(x.toFloat(), y.toFloat(), 50.0f)
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f)
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-45.0f, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-Math.atan((mouseY / 40.0f).toDouble()).toFloat() * 20.0f, 1.0f, 0.0f, 0.0f)
        if (!rotate) {
            ent.renderYawOffset = Math.atan((mouseX / 40.0f).toDouble()).toFloat() * 20.0f
            ent.rotationYaw = Math.atan((mouseX / 40.0f).toDouble()).toFloat() * 40.0f
            ent.rotationPitch = -Math.atan((mouseY / 40.0f).toDouble()).toFloat() * 20.0f
            ent.rotationYawHead = ent.rotationYaw
            ent.prevRotationYawHead = ent.rotationYaw
        }
        GlStateManager.translate(0.0f, 0.0f, 0.0f)

        val renderManager = Client.getMinecraft().renderManager
        renderManager.setPlayerViewY(180.0f)
        renderManager.isRenderShadow = false
        //#if MC<=10809
        renderManager.renderEntityWithPosYaw(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f)
        //#else
        //$$ renderManager.doRenderEntity(ent, 0.0, 0.0, 0.0, 0.0F, 1.0F, false)
        //#endif
        renderManager.isRenderShadow = true

        ent.renderYawOffset = f
        ent.rotationYaw = f1
        ent.rotationPitch = f2
        ent.prevRotationYawHead = f3
        ent.rotationYawHead = f4

        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit)
        GlStateManager.disableTexture2D()
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit)

        finishDraw()
    }

    @JvmStatic
    fun finishDraw() {
        this.colorized = false
        GlStateManager.popMatrix()
        GlStateManager.pushMatrix()
    }

    object screen {
        @JvmStatic
        fun getWidth() = ScaledResolution(Client.getMinecraft()).scaledWidth
        @JvmStatic
        fun getHeight() = ScaledResolution(Client.getMinecraft()).scaledHeight
        @JvmStatic
        fun getScale() = ScaledResolution(Client.getMinecraft()).scaleFactor
    }
}