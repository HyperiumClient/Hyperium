/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cc.hyperium.utils

import net.minecraft.client.Minecraft
import net.minecraft.network.play.client.C01PacketChatMessage

object ChatUtil {
    /**
     * Send a message using the C01PacketChatMessage
     *
     * @param msg a string that the user inputs
     */
    @JvmStatic
    fun sendMessage(msg: String) {
        val packet = C01PacketChatMessage(msg)
        packet.message = msg
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet)
    }
}
