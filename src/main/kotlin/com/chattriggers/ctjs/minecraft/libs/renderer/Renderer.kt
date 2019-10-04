package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.MathLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCTessellator
import com.chattriggers.ctjs.utils.kotlin.getRenderer
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11
import java.util.*
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@External
object Renderer {
    var colorized: Int? = null
    private var retainTransforms = false

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
    fun getFontRenderer(): FontRenderer = Client.getMinecraft().fontRendererObj

    @JvmStatic
    @JvmOverloads
    fun getStringWidth(text: String, removeFormatting: Boolean = true): Int {
        return if (removeFormatting)
            getFontRenderer().getStringWidth(ChatLib.removeFormatting(text))
        else getFontRenderer().getStringWidth(text)
    }

    @JvmStatic
    @JvmOverloads
    fun color(red: Int, green: Int, blue: Int, alpha: Int = 255): Int {
        return (MathLib.clamp(alpha, 0, 255) * 0x1000000
                + MathLib.clamp(red, 0, 255) * 0x10000
                + MathLib.clamp(green, 0, 255) * 0x100
                + MathLib.clamp(blue, 0, 255))
    }

    @JvmStatic
    @JvmOverloads
    fun getRainbow(step: Float, speed: Float = 1f): Int {
        val red = ((sin((step / speed).toDouble()) + 0.75) * 170).toInt()
        val green = ((sin(step / speed + 2 * Math.PI / 3) + 0.75) * 170).toInt()
        val blue = ((sin(step / speed + 4 * Math.PI / 3) + 0.75) * 170).toInt()
        return color(red, green, blue, 255)
    }

    @JvmStatic
    @JvmOverloads
    fun getRainbowColors(step: Float, speed: Float = 1f): IntArray {
        val red = ((sin((step / speed).toDouble()) + 0.75) * 170).toInt()
        val green = ((sin(step / speed + 2 * Math.PI / 3) + 0.75) * 170).toInt()
        val blue = ((sin(step / speed + 4 * Math.PI / 3) + 0.75) * 170).toInt()
        return intArrayOf(red, green, blue)
    }

    @JvmStatic
    fun retainTransforms(retain: Boolean) {
        retainTransforms = retain
        finishDraw()
    }

    @JvmStatic
    fun translate(x: Float, y: Float) {
        GL11.glTranslated(x.toDouble(), y.toDouble(), 0.0)
    }

    @JvmStatic
    @JvmOverloads
    fun scale(scaleX: Float, scaleY: Float = scaleX) {
        GL11.glScalef(scaleX, scaleY, 1f)
    }

    @JvmStatic
    fun rotate(angle: Float) {
        GL11.glRotatef(angle, 0f, 0f, 1f)
    }

    @JvmStatic
    @JvmOverloads
    fun colorize(red: Float, green: Float, blue: Float, alpha: Float = 255f) {
        colorized = fixAlpha(color(red.toInt(), green.toInt(), blue.toInt(), alpha.toInt()))

        GlStateManager.color(
            MathLib.clampFloat(red, 0f, 255f),
            MathLib.clampFloat(green, 0f, 255f),
            MathLib.clampFloat(blue, 0f, 255f),
            MathLib.clampFloat(alpha, 0f, 255f)
        )
    }

    @JvmStatic
    fun fixAlpha(color: Int): Int {
        val alpha = color shr 24 and 255
        return if (alpha < 10)
            (color and 0xFF_FF_FF) or 0xA_FF_FF_FF
        else color
    }

    @Deprecated(
        message = "Replaced with Image object",
        replaceWith = ReplaceWith(
            expression = "Image(name[, url])",
            imports = ["com.chattriggers.ctjs.minecraft.libs.renderer.Image"]
        )
    )
    @JvmStatic
    fun image(name: String, url: String): Image = Image(name, url)

    @JvmStatic
    @Deprecated(
        message = "Replaced with Text object",
        replaceWith = ReplaceWith(
            expression = "Text(text, x, y)",
            imports = ["com.chattriggers.ctjs.minecraft.libs.renderer.Text"]
        )
    )
    fun text(text: String, x: Float, y: Float): Text = Text(text, x, y)

    @JvmStatic
    @Deprecated(
        message = "Replaced with Text object",
        replaceWith = ReplaceWith(
            expression = "Text(text)",
            imports = ["com.chattriggers.ctjs.minecraft.libs.renderer.Text"]
        )
    )
    fun text(text: String): Text = Text(text)

    @JvmStatic
    @Deprecated(
        message = "Replaced with Rectangle object",
        replaceWith = ReplaceWith(
            expression = "Rectangle(color, x, y, width, height)",
            imports = ["com.chattriggers.ctjs.minecraft.libs.renderer.Rectangle"]
        )
    )
    fun rectangle(color: Int, x: Float, y: Float, width: Float, height: Float): Rectangle =
        Rectangle(color, x, y, width, height)

    @JvmStatic
    @Deprecated(
        message = "Replaced with Shape object",
        replaceWith = ReplaceWith(
            expression = "Shape(color)",
            imports = ["com.chattriggers.ctjs.minecraft.libs.renderer.Shape"]
        )
    )
    fun shape(color: Int): Shape = Shape(color)

    @JvmStatic
    fun drawRect(color: Int, x: Float, y: Float, width: Float, height: Float) {
        val pos = mutableListOf(x, y, x + width, y + height)
        if (pos[0] > pos[2])
            Collections.swap(pos, 0, 2)
        if (pos[1] > pos[3])
            Collections.swap(pos, 1, 3)

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()

        val tessellator = MCTessellator.getInstance()
        val worldRenderer = tessellator.getRenderer()

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        if (colorized == null) {
            val a = (color shr 24 and 255).toFloat() / 255.0f
            val r = (color shr 16 and 255).toFloat() / 255.0f
            val g = (color shr 8 and 255).toFloat() / 255.0f
            val b = (color and 255).toFloat() / 255.0f
            GlStateManager.color(r, g, b, a)
        }
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
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
    @JvmOverloads
    fun drawShape(color: Int, vararg vertexes: List<Float>, drawMode: Int = 7) {
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()

        val tessellator = MCTessellator.getInstance()
        val worldRenderer = tessellator.getRenderer()

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        if (colorized == null) {
            val a = (color shr 24 and 255).toFloat() / 255.0f
            val r = (color shr 16 and 255).toFloat() / 255.0f
            val g = (color shr 8 and 255).toFloat() / 255.0f
            val b = (color and 255).toFloat() / 255.0f
            GlStateManager.color(r, g, b, a)
        }

        worldRenderer.begin(drawMode, DefaultVertexFormats.POSITION)

        vertexes.forEach {
            if (it.size == 2) {
                worldRenderer.pos(it[0].toDouble(), it[1].toDouble(), 0.0).endVertex()
            }
        }

        tessellator.draw()

        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        finishDraw()
    }

    @JvmStatic
    @JvmOverloads
    fun drawLine(color: Int, x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float, drawMode: Int = 9) {
        val theta = -atan2((y2 - y1).toDouble(), (x2 - x1).toDouble())
        val i = sin(theta).toFloat() * (thickness / 2)
        val j = cos(theta).toFloat() * (thickness / 2)

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()

        val tessellator = MCTessellator.getInstance()
        val worldRenderer = tessellator.getRenderer()

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        if (colorized == null) {
            val a = (color shr 24 and 255).toFloat() / 255.0f
            val r = (color shr 16 and 255).toFloat() / 255.0f
            val g = (color shr 8 and 255).toFloat() / 255.0f
            val b = (color and 255).toFloat() / 255.0f
            GlStateManager.color(r, g, b, a)
        }

        worldRenderer.begin(drawMode, DefaultVertexFormats.POSITION)

        worldRenderer.pos((x1 + i).toDouble(), (y1 + j).toDouble(), 0.0).endVertex()
        worldRenderer.pos((x2 + i).toDouble(), (y2 + j).toDouble(), 0.0).endVertex()
        worldRenderer.pos((x2 - i).toDouble(), (y2 - j).toDouble(), 0.0).endVertex()
        worldRenderer.pos((x1 - i).toDouble(), (y1 - j).toDouble(), 0.0).endVertex()

        tessellator.draw()

        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        finishDraw()
    }

    @JvmStatic
    @JvmOverloads
    fun drawCircle(color: Int, x: Float, y: Float, radius: Float, steps: Int, drawMode: Int = 5) {
        val theta = 2 * Math.PI / steps
        val cos = cos(theta).toFloat()
        val sin = sin(theta).toFloat()

        var xHolder: Float
        var circleX = 1f
        var circleY = 0f

        val tessellator = MCTessellator.getInstance()
        val worldRenderer = tessellator.getRenderer()

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        if (colorized == null) {
            val a = (color shr 24 and 255).toFloat() / 255.0f
            val r = (color shr 16 and 255).toFloat() / 255.0f
            val g = (color shr 8 and 255).toFloat() / 255.0f
            val b = (color and 255).toFloat() / 255.0f
            GlStateManager.color(r, g, b, a)
        }

        worldRenderer.begin(drawMode, DefaultVertexFormats.POSITION)

        for (i in 0..steps) {
            worldRenderer.pos(x.toDouble(), y.toDouble(), 0.0).endVertex()
            worldRenderer.pos((circleX * radius + x).toDouble(), (circleY * radius + y).toDouble(), 0.0).endVertex()
            xHolder = circleX
            circleX = cos * circleX - sin * circleY
            circleY = sin * xHolder + cos * circleY
            worldRenderer.pos((circleX * radius + x).toDouble(), (circleY * radius + y).toDouble(), 0.0).endVertex()
        }

        tessellator.draw()

        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        finishDraw()
    }

    @JvmStatic
    fun drawString(text: String, x: Float, y: Float) {
        getFontRenderer().drawString(ChatLib.addColor(text), x, y, colorized ?: 0xffffffff.toInt(), false)
        finishDraw()
    }

    @JvmStatic
    fun drawStringWithShadow(text: String, x: Float, y: Float) {
        getFontRenderer().drawString(ChatLib.addColor(text), x, y, colorized ?: 0xffffffff.toInt(), true)
        finishDraw()
    }

    @JvmStatic
    fun drawImage(image: Image, x: Double, y: Double, width: Double, height: Double) {
        if (colorized == null)
            GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableBlend()
        GlStateManager.scale(1f, 1f, 50f)
        GlStateManager.bindTexture(image.getTexture().glTextureId)
        GlStateManager.enableTexture2D()

        val tessellator = MCTessellator.getInstance()
        val worldRenderer = tessellator.getRenderer()

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)

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
        GlStateManager.rotate(-atan((mouseY / 40.0f).toDouble()).toFloat() * 20.0f, 1.0f, 0.0f, 0.0f)
        if (!rotate) {
            ent.renderYawOffset = atan((mouseX / 40.0f).toDouble()).toFloat() * 20.0f
            ent.rotationYaw = atan((mouseX / 40.0f).toDouble()).toFloat() * 40.0f
            ent.rotationPitch = -atan((mouseY / 40.0f).toDouble()).toFloat() * 20.0f
            ent.rotationYawHead = ent.rotationYaw
            ent.prevRotationYawHead = ent.rotationYaw
        }
        GlStateManager.translate(0.0f, 0.0f, 0.0f)

        val renderManager = Client.getMinecraft().renderManager
        renderManager.setPlayerViewY(180.0f)
        renderManager.isRenderShadow = false
        renderManager.renderEntityWithPosYaw(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f)
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
        if (!retainTransforms) {
            colorized = null
            GL11.glPopMatrix()
            GL11.glPushMatrix()
        }
    }

    object Screen {
        @JvmStatic
        fun getWidth(): Int = ScaledResolution(Client.getMinecraft()).scaledWidth

        @JvmStatic
        fun getHeight(): Int = ScaledResolution(Client.getMinecraft()).scaledHeight

        @JvmStatic
        fun getScale(): Int = ScaledResolution(Client.getMinecraft()).scaleFactor
    }
}
