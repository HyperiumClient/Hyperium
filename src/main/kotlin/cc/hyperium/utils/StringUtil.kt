package cc.hyperium.utils

import net.minecraft.client.Minecraft
import java.awt.Color

object StringUtil {

    fun drawChromaWaveString(text: String, xIn: Int, y: Int) {
        val renderer = Minecraft.getMinecraft().fontRendererObj
        var x = xIn
        for (c in text.toCharArray()) {
            val dif = x * 10 - (y * 10).toLong()
            val l = System.currentTimeMillis() - dif
            val ff = 2000.0f
            val i = Color.HSBtoRGB((l % ff.toInt()).toFloat() / ff, 0.8f, 0.8f)
            val tmp = c.toString()
            renderer.drawString(
                tmp,
                x.toDouble().toFloat(),
                y.toDouble().toFloat(),
                i,
                false
            )

            x += renderer.getCharWidth(c)
        }
    }
}