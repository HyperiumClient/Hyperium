package cc.hyperium.utils

import net.minecraft.client.Minecraft
import java.awt.Color

object StringUtil {

    fun drawChromaWaveString(text: String, xIn: Int, y: Int) {
        val renderer = Minecraft.getMinecraft().fontRendererObj
        var x = xIn
        for (chars in text.toCharArray()) {
            val dif = x * 10 - (y * 10)
            val color = Color.HSBtoRGB((System.currentTimeMillis() - dif % 2000f / 2000f), 0.8f, 0.8f)
            renderer.drawString(
                chars.toString(),
                x.toFloat(),
                y.toFloat(),
                color,
                false
            )

            x += renderer.getCharWidth(chars)
        }
    }
}