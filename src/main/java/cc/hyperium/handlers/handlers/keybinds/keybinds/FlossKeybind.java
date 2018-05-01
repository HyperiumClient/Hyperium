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
import cc.hyperium.handlers.handlers.FlossDanceHandler;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.UUID;

public class FlossKeybind extends HyperiumBind {

    boolean down1 = false;

    public FlossKeybind() {
        super("Floss dance", Keyboard.KEY_P);
    }

    @Override
    public void onPress() {
        down1 = !down1;
        if (!down1)
            return;
        FlossDanceHandler flossDanceHandler = Hyperium.INSTANCE.getHandlers().getFlossDanceHandler();
        UUID uuid = (Minecraft.getMinecraft().getSession()).getProfile().getId();
        FlossDanceHandler.DanceState currentState = flossDanceHandler.get(uuid);

        if (currentState.isDancing()) {
            flossDanceHandler.get(uuid).setToggled(false);
            flossDanceHandler.stopDancing(uuid);
            NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "floss_update").put("flossing", false)));
            System.out.println("Stopping");

            return;
        }

        flossDanceHandler.get(uuid).setToggled(CosmeticSettings.flossDanceToggle);
        flossDanceHandler.startDancing(uuid);
        NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "floss_update").put("flossing", true)));
        System.out.println("Starting");

    }

    @Override
    public void onRelease() {
//        justReleased = fal;se
//        if (CosmeticSettings.flossDanceToggle) return;
//        Hyperium.INSTANCE.getHandlers().getFlossDanceHandler().stopDancing(Minecraft.getMinecraft().getSession().getProfile().getId());
//        NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "floss_update").put("flossing", false)));
//        System.out.println("stopping");
    }
}