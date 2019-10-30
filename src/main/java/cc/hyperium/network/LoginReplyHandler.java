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

package cc.hyperium.network;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.PacketHandler;
import cc.hyperium.netty.packet.PacketType;
import cc.hyperium.netty.packet.packets.clientbound.LoginReplyPacket;
import cc.hyperium.netty.packet.packets.serverbound.UpdateLocationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

import java.util.Arrays;
import java.util.Iterator;

public class LoginReplyHandler implements PacketHandler<LoginReplyPacket> {
    public static boolean SHOW_MESSAGES;

    @Override
    public void handle(LoginReplyPacket loginReplyPacket) {
        ServerData currentServerData = Minecraft.getMinecraft().getCurrentServerData();
        if (currentServerData != null) {
            NettyClient client = NettyClient.getClient();
            if (client != null) {
                client.write(UpdateLocationPacket.build("Other"));
            }
        }

        if (loginReplyPacket.isAdmin()) {
            Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new BaseCommand() {
                @Override
                public String getName() {
                    return "hyperiumadmin";
                }

                @Override
                public String getUsage() {
                    return "/hyperiumadmin";
                }

                @Override
                public void onExecute(String[] args) {
                    if (args.length == 1 && args[0].equalsIgnoreCase("show_messages")) SHOW_MESSAGES = !SHOW_MESSAGES;
                    StringBuilder builder = new StringBuilder();
                    Iterator<String> iterator = Arrays.stream(args).iterator();
                    while (iterator.hasNext()) {
                        builder.append(iterator.next());
                        if (iterator.hasNext()) builder.append(" ");
                    }

                    NettyClient.getClient().dispatchCommand(builder.toString());

                }
            });

        }

    }

    @Override
    public PacketType accepting() {
        return PacketType.LOGIN_REPLY;
    }
}
