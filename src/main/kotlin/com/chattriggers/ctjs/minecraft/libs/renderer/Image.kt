package com.chattriggers.ctjs.minecraft.libs.renderer

import cc.hyperium.event.EventBus
import cc.hyperium.event.InvokeEvent
import cc.hyperium.event.render.RenderHUDEvent
import com.chattriggers.ctjs.CTJS
import net.minecraft.client.renderer.texture.DynamicTexture
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

class Image(var image: BufferedImage?) {
    private lateinit var texture: DynamicTexture
    private var textureWidth = image?.width ?: 0
    private var textureHeight = image?.height ?: 0

    init {
        EventBus.INSTANCE.register(this)
    }

    @JvmOverloads
    constructor(name: String, url: String? = null) : this(getBufferedImage(name, url))

    fun getTextureWidth(): Int = textureWidth
    fun getTextureHeight(): Int = textureHeight
    fun getTexture(): DynamicTexture = texture

    @InvokeEvent
    fun onRender(event: RenderHUDEvent) {
        if (image != null) {
            texture = DynamicTexture(image)
            image = null

            EventBus.INSTANCE.unregister(this)
        }
    }

    @JvmOverloads
    fun draw(
        x: Double, y: Double,
        width: Double = textureWidth.toDouble(),
        height: Double = textureHeight.toDouble()
    ) = apply {
        if (image != null) return@apply

        Renderer.drawImage(this, x, y, width, height)
    }
}

private fun getBufferedImage(name: String, url: String? = null): BufferedImage? {
    val resourceFile = File(CTJS.assetsDir, name)

    if (resourceFile.exists()) {
        return ImageIO.read(resourceFile)
    }

    val image = ImageIO.read(URL(url))
    ImageIO.write(image, "png", resourceFile)
    return image
}
