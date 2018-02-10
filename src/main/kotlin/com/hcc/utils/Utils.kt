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

import java.io.InputStream
import java.nio.ByteBuffer
import javax.imageio.ImageIO

class Utils {
    fun fromList(list: List<String>): String{
        var s = ""
        list.forEach{s+=(it+"\n")}
        return s
    }

    fun readImageToBuffer(inputStream: InputStream): ByteBuffer{
        val bufferedimage = ImageIO.read(inputStream)
        val aint = bufferedimage.getRGB(0, 0, bufferedimage.width, bufferedimage.height, null as IntArray?, 0, bufferedimage.width)
        val bytebuffer = ByteBuffer.allocate(4 * aint.size)

        for (i in aint) {
            bytebuffer.putInt(i shl 8 or (i shr 24 and 255))
        }

        bytebuffer.flip()
        return bytebuffer
    }
}