package cc.hyperium.utils;

import cc.hyperium.mixins.packet.MixinC01PacketChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class ChatUtil {

    public static void sendMessage(String msg) {
        final C01PacketChatMessage packet = new C01PacketChatMessage(msg);
        ((MixinC01PacketChatMessage) packet).setMessage(msg);
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
    }
}
