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

package cc.hyperium.handlers.handlers;

import cc.hyperium.Metadata;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.ServerJoinEvent;
import cc.hyperium.launch.HyperiumTweaker;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.JsonHolder;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class HyperiumNetwork {

    @InvokeEvent
    public void joinHypixel(ServerJoinEvent event) {
        Multithreading.runAsync(() -> {
            NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
            if (netHandler != null) {
                netHandler.addToSendQueue(
                    new C17PacketCustomPayload("hyperium",
                        new PacketBuffer(Unpooled.buffer()).writeString(new JsonHolder()
                            .put("id", Metadata.getModid())
                            .put("optifine", HyperiumTweaker.INSTANCE.isUsingOptifine())
                            .put("version", Metadata.getVersion()).toString())));
            }
        });
    }
}
