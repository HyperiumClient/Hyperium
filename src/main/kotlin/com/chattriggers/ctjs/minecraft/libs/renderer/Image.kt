package com.chattriggers.ctjs.minecraft.libs.renderer

import cc.hyperium.event.EventBus
import cc.hyperium.event.InvokeEvent
import cc.hyperium.event.RenderHUDEvent
import net.minecraft.client.renderer.texture.DynamicTexture
import java.awt.image.BufferedImage

class Image(var image: BufferedImage?) {
    private lateinit var texture: DynamicTexture
    private var textureWidth = image?.width ?: 0
    private var textureHeight = image?.height ?: 0

    init {
        EventBus.INSTANCE.register(this)
    }

    fun getTextureWidth() = this.textureWidth
    fun getTextureHeight() = this.textureHeight
    fun getTexture(): DynamicTexture = this.texture

    @InvokeEvent
    fun onRender(event: RenderHUDEvent) {
        if (image != null) {
            texture = DynamicTexture(image)
            image = null

            EventBus.INSTANCE.unregister(this)
        }
    }

    @JvmOverloads
    fun draw(x: Double, y: Double,
             width: Double = this.textureWidth.toDouble(),
             height: Double = this.textureHeight.toDouble()) = apply {
        if (image != null) return@apply

        Renderer.drawImage(this, x, y, width, height)
    }
}