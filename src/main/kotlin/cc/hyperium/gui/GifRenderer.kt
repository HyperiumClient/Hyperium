package cc.hyperium.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.texture.DynamicTexture
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

/**
 * renders gif c:
 *
 * @author Cubxity
 *
 * @param tpf ticks per frame
 */
class GifRenderer(gif: InputStream, private val tpf: Int) : Gui() {
    private lateinit var tex: DynamicTexture
    private var w = 0
    private var h = 0
    private var frames = 0
    private var step = 0
    private var wt = 0 // waiting ticks for next frame

    init {
        try {
            val reader = ImageIO.getImageReadersByFormatName("gif").next()
            val stream = ImageIO.createImageInputStream(gif)
            reader.input = stream

            frames = reader.getNumImages(true)

            val first = reader.read(0)
            val img = BufferedImage(frames * first.width, first.height, BufferedImage.TYPE_INT_ARGB)
            val g2d = img.graphics
            w = first.width
            h = first.height
            for (index in 0 until frames)
                g2d.drawImage(reader.read(index), first.width * index, 0, null)
            tex = DynamicTexture(img)
            tex.loadTexture(Minecraft.getMinecraft().resourceManager)

            stream.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun render(x: Int, y: Int, w: Int, h: Int) {
        try {
            GlStateManager.bindTexture(tex.glTextureId)
            drawScaledCustomSizeModalRect(x, y, (step * w).toFloat(), 0f, this.w, this.h, w, h, (this.w * frames).toFloat(), this.h.toFloat())
            if (wt == 0) {
                step = if (step == frames) 0 else step + 1
                wt = tpf
            } else
                wt--
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}