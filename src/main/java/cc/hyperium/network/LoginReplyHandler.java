package cc.hyperium.network;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.PacketHandler;
import cc.hyperium.netty.packet.PacketType;
import cc.hyperium.netty.packet.packets.clientbound.LoginReplyPacket;

import java.util.Arrays;
import java.util.Iterator;

public class LoginReplyHandler implements PacketHandler<LoginReplyPacket> {
    public static boolean SHOW_MESSAGES = false;

    @Override
    public void handle(LoginReplyPacket loginReplyPacket) {
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
