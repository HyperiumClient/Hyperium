/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.utils

import cc.hyperium.Hyperium
import cc.hyperium.config.Settings
import cc.hyperium.purchases.EnumPurchaseType
import cc.hyperium.purchases.PurchaseApi
import cc.hyperium.purchases.packages.EarsCosmetic
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
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

    /*
    * @return the entity name offset
    * */
    fun <T : Entity> calculateDeadmauEarsOffset(entity: T): Float {
        try {
            if (entity is EntityPlayer && Hyperium.INSTANCE.cosmetics.deadmau5Cosmetic.isPurchasedBy(entity.getUniqueID())) {
                val packageIfReady = PurchaseApi.getInstance().getPackageIfReady(entity.getUniqueID())
                if (packageIfReady != null) {
                    val purchase = packageIfReady.getPurchase(EnumPurchaseType.DEADMAU5_COSMETIC)
                    if (purchase != null) {
                        if (entity.getUniqueID() !== Minecraft.getMinecraft().thePlayer.uniqueID) {
                            if (purchase is EarsCosmetic && purchase.isEnabled) {
                                return .24f
                            }
                        } else if (Settings.EARS_STATE.equals("on", ignoreCase = true))
                            return .24f
                    }
                }
            }
        } catch (ignored: Exception) {

        }
        return 0f
    }

    fun countSubstrings(s: String, sub: String): Int {
        var count = 0

        for (i in 0 until s.length) {
            if (s[i].toString().equals(sub)) {
                count++
            }
        }

        return count
    }
}