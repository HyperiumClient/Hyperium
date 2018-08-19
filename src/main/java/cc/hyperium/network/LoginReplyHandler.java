package cc.hyperium.network;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.config.Settings;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.PacketHandler;
import cc.hyperium.netty.packet.PacketType;
import cc.hyperium.netty.packet.packets.clientbound.LoginReplyPacket;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.netty.packet.packets.serverbound.UpdateLocationPacket;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

import java.util.Arrays;
import java.util.Iterator;

public class LoginReplyHandler implements PacketHandler<LoginReplyPacket> {
    public static boolean SHOW_MESSAGES = false;

    @Override
    public void handle(LoginReplyPacket loginReplyPacket) {
        ServerData currentServerData = Minecraft.getMinecraft().getCurrentServerData();
        if (currentServerData != null) {
            NettyClient client = NettyClient.getClient();
            if (client != null) {
                client.write(UpdateLocationPacket.build("Other"));
                if (Settings.SEND_SERVER)
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("server_update", currentServerData.serverIP)));
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
                    if (args.length == 1 && args[0].equalsIgnoreCase("show_messages")) {
                        SHOW_MESSAGES = !SHOW_MESSAGES;
                    }
                    StringBuilder builder = new StringBuilder();
                    Iterator<String> iterator = Arrays.stream(args).iterator();
                    while (iterator.hasNext()) {
                        builder.append(iterator.next());
                        if (iterator.hasNext())
                            builder.append(" ");
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
