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
import cc.hyperium.config.Settings;
import cc.hyperium.handlers.handlers.animation.AbstractAnimationHandler;
import cc.hyperium.handlers.handlers.animation.TPoseHandler;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class TPoseKeybind extends HyperiumBind {

    public TPoseKeybind() {
        super("T-Pose", Keyboard.KEY_N);
    }

    private boolean tPoseToggled;

    @Override
    public void onPress() {
        TPoseHandler tPoseHandler = Hyperium.INSTANCE.getHandlers().getTPoseHandler();
        UUID uuid = (Minecraft.getMinecraft().getSession()).getProfile().getId();
        AbstractAnimationHandler.AnimationState currentState = tPoseHandler.get(uuid);

        if (Settings.TPOSE_TOGGLE_MODE) {
            tPoseToggled = !tPoseToggled;
        } else {
            tPoseToggled = true;
        }

        NettyClient client = NettyClient.getClient();

        if (Settings.TPOSE_TOGGLE_MODE) {
            currentState.setToggled(tPoseToggled);
            if (tPoseToggled) {
                tPoseHandler.startAnimation(uuid);
            } else {
                tPoseHandler.stopAnimation(uuid);
            }
            if (client != null) {
                client.write(ServerCrossDataPacket
                    .build(new JsonHolder().put("type", "tpose_update").put("posing", tPoseToggled)));
            }
            return;
        }

        if (Settings.TPOSE_TOGGLE && currentState.isAnimating() && !this.wasPressed()) {
            currentState.setToggled(false);
            tPoseHandler.stopAnimation(uuid);
            if (client != null) {
                client.write(ServerCrossDataPacket
                    .build(new JsonHolder().put("type", "tpose_update").put("posing", false)));
            }
            return;
        }

        if (!this.wasPressed()) {
            currentState.setToggled(Settings.TPOSE_TOGGLE);
            tPoseHandler.startAnimation(uuid);
        }
        if (client != null) {
            client.write(ServerCrossDataPacket
                .build(new JsonHolder().put("type", "tpose_update").put("posing", true)));
        }
    }


    @Override
    public void onRelease() {
        if (!Settings.TPOSE_TOGGLE_MODE) {
            tPoseToggled = false;
        }

        if (Settings.TPOSE_TOGGLE || Settings.TPOSE_TOGGLE_MODE) {
            return;
        }

        Hyperium.INSTANCE.getHandlers().getTPoseHandler().stopAnimation(UUIDUtil.getClientUUID());
        NettyClient client = NettyClient.getClient();
        if (client != null) {
            client.write(ServerCrossDataPacket
                .build(new JsonHolder().put("type", "tpose_update").put("posing", false)));
        }
    }
}
