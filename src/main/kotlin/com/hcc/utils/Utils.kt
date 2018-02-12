/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.utils

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.lwjgl.BufferUtils
import org.lwjgl.input.Cursor
import org.lwjgl.input.Mouse
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.IntBuffer
import javax.imageio.ImageIO

object Utils {

    fun fromList(list: List<String>) =
            list.joinToString { "\n" }


    fun readImageToBuffer(inputStream: InputStream): ByteBuffer {
        val bufferedimage = ImageIO.read(inputStream)
        val aint = bufferedimage.getRGB(0, 0, bufferedimage.width, bufferedimage.height, null as IntArray?, 0, bufferedimage.width)
        val bytebuffer = ByteBuffer.allocate(4 * aint.size)

        for (i in aint) {
            bytebuffer.putInt(i shl 8 or (i shr 24 and 255))
        }

        bytebuffer.flip()
        return bytebuffer
    }

    fun setCursor(cursor: ResourceLocation) {
        try {
            val image = ImageIO.read(Minecraft.getMinecraft().resourceManager.getResource(cursor).inputStream)
            val w = image.width
            val h = image.height
            val pixels = IntArray(w * h)
            image.getRGB(0, 0, w, h, pixels, 0, w)
            val buffer = BufferUtils.createByteBuffer(w * h * 4)
            for (y in 0 until h) {
                for (x in 0 until w) {
                    val pixel = pixels[(h - 1 - y) * w + x]
                    buffer.put((pixel and 0xFF).toByte())
                    buffer.put((pixel shr 8 and 0xFF).toByte())
                    buffer.put((pixel shr 16 and 0xFF).toByte())
                    buffer.put((pixel shr 24 and 0xFF).toByte())
                }
            }
            buffer.flip()
            Mouse.setNativeCursor(Cursor(w, h, 0, h - 1, 1, buffer.asIntBuffer(), null as IntBuffer?))
        } catch (e: Exception) {
        }

    }
}