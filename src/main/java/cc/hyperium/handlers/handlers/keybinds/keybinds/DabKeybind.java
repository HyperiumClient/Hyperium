/*
 *      Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published
 *      by the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.settings.items.CosmeticSettings;
import cc.hyperium.handlers.handlers.animation.DabHandler;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.UUID;

public class DabKeybind extends HyperiumBind {

    public DabKeybind() {
        super("Dab", Keyboard.KEY_B);
    }

    @Override
    public void onPress() {
        DabHandler dabHandler = Hyperium.INSTANCE.getHandlers().getDabHandler();
        UUID uuid = (Minecraft.getMinecraft().getSession()).getProfile().getId();
        DabHandler.DabState currentState = dabHandler.get(uuid);

        if (CosmeticSettings.dabToggle && currentState.isDabbing() && !this.wasPressed()) {
            dabHandler.get(uuid).setToggled(false);
            dabHandler.stopDabbing(uuid);
            NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "dab_update").put("dabbing", false)));
            return;
        }

        if (!this.wasPressed()) {
            dabHandler.get(uuid).setToggled(CosmeticSettings.dabToggle);
            dabHandler.startDabbing(uuid);
        }
        NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "dab_update").put("dabbing", true)));
    }


    @Override
    public void onRelease() {
        if (CosmeticSettings.dabToggle) return;

        Hyperium.INSTANCE.getHandlers().getDabHandler().stopDabbing(Minecraft.getMinecraft().getSession().getProfile().getId());
        NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "dab_update").put("dabbing", false)));
    }
}
