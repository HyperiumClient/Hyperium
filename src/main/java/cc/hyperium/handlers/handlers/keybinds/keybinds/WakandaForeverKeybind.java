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
import cc.hyperium.handlers.handlers.animation.AbstractAnimationHandler;
import cc.hyperium.handlers.handlers.animation.WakandaForeverPostHandler;
import cc.hyperium.handlers.handlers.animation.WakandaForeverPreHandler;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.UUID;

public class WakandaForeverKeybind extends HyperiumBind {

    public WakandaForeverKeybind() {
        super("Wakanda Forever", Keyboard.KEY_PERIOD);
    }

    @Override
    public void onPress() {
        WakandaForeverPreHandler dabHandler = Hyperium.INSTANCE.getHandlers().getWakandaForeverPreHandler();
        UUID uuid = Minecraft.getMinecraft().thePlayer.getUniqueID();
        AbstractAnimationHandler.AnimationState currentState = dabHandler.get(uuid);
        currentState.setToggled(true);
        dabHandler.startAnimation(uuid);

        WakandaForeverPostHandler postHandler = Hyperium.INSTANCE.getHandlers().getWakandaForeverPostHandler();
        currentState = postHandler.get(uuid);
        currentState.setToggled(true);
        postHandler.startAnimation(uuid);


        NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "wakanda_update").put("active", true)));
    }


    @Override
    public void onRelease() {
        Hyperium.INSTANCE.getHandlers().getWakandaForeverPreHandler().stopAnimation(Minecraft.getMinecraft().thePlayer.getUniqueID());
        Hyperium.INSTANCE.getHandlers().getWakandaForeverPostHandler().stopAnimation(Minecraft.getMinecraft().thePlayer.getUniqueID());

        NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "dab_update").put("dabbing", false)));
    }
}
