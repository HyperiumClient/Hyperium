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

package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import org.lwjgl.input.Keyboard;

public class TwerkDanceKeybind extends HyperiumBind {
    public TwerkDanceKeybind() {
        super("Twerk", Keyboard.KEY_NONE);
    }

    @Override
    public void onPress() {
        Hyperium.INSTANCE.getHandlers().getTwerkDance().getStates().put(UUIDUtil.getClientUUID(), System.currentTimeMillis());
        Hyperium.INSTANCE.getHandlers().getTwerkDance().startAnimation(UUIDUtil.getClientUUID());
        NettyClient client = NettyClient.getClient();

        if (client != null) {
            client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "twerk_dance")));
        }
    }
}
