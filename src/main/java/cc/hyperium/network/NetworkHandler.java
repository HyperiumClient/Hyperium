package cc.hyperium.network;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.netty.INetty;
import net.minecraft.client.Minecraft;

import java.util.UUID;

public class NetworkHandler implements INetty {
    @Override
    public String getSession() {
        return Minecraft.getMinecraft().getSession().getToken();
    }

    @Override
    public UUID getPlayerUUID() {
        return Minecraft.getMinecraft().getSession().getProfile().getId();
    }

    @Override
    public String getPlayerName() {
        return Minecraft.getMinecraft().getSession().getProfile().getName();
    }

    @Override
    public void handleChat(String s) {
        GeneralChatHandler.instance().sendMessage(s);
    }

    @Override
    public void handleDab(UUID uuid, boolean b) {
        if (b)
            Hyperium.INSTANCE.getHandlers().getDabHandler().get(uuid).ensureDabbingFor(60);
        else Hyperium.INSTANCE.getHandlers().getDabHandler().get(uuid).stopDabbing();
    }
}
