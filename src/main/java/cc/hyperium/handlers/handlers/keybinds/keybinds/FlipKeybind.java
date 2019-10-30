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
import cc.hyperium.config.Settings;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import org.lwjgl.input.Keyboard;

public class FlipKeybind extends HyperiumBind {

    private boolean inverted;

    public FlipKeybind() {
        super("Flip (Requires Purchase)", Keyboard.KEY_NONE);
    }

    @Override
    public void onPress() {
        if (!Hyperium.INSTANCE.getCosmetics().getFlipCosmetic().isSelfUnlocked()) {
            return;
        }

        if (Settings.isFlipToggle) {
            inverted = !inverted;
            int state = inverted ? Settings.flipType : 0;
            Hyperium.INSTANCE.getHandlers().getFlipHandler().state(UUIDUtil.getClientUUID(), state);
            NettyClient client = NettyClient.getClient();

            if (client != null) {
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "flip_update").put("flip_state", state)));
            }

            Hyperium.INSTANCE.getHandlers().getFlipHandler().resetTick();
        }
    }

    @Override
    public void onRelease() {
        if (!Hyperium.INSTANCE.getCosmetics().getFlipCosmetic().isSelfUnlocked()) {
            return;
        }

        if (!Settings.isFlipToggle) {
            inverted = !inverted;
            int state = inverted ? Settings.flipType : 0;
            Hyperium.INSTANCE.getHandlers().getFlipHandler().state(UUIDUtil.getClientUUID(), state);
            NettyClient client = NettyClient.getClient();

            if (client != null) {
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "flip_update").put("flip_state", state)));
            }
        }
    }
}

