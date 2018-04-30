package cc.hyperium.network;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.ShopGui;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.netty.INetty;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;

import java.util.List;
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
        System.out.println("Chat: " + s);
        GeneralChatHandler.instance().sendMessage(s, false);
    }

    @Override
    public void handleCrossClientData(UUID uuid, JsonHolder jsonHolder) {
        String type = jsonHolder.optString("type");
        if (type.equalsIgnoreCase("dab_update"))
            if (jsonHolder.optBoolean("dabbing"))
                Hyperium.INSTANCE.getHandlers().getDabHandler().get(uuid).ensureDabbingFor(60);
            else Hyperium.INSTANCE.getHandlers().getDabHandler().get(uuid).stopDabbing();
        else if (type.equalsIgnoreCase("floss_update")) {
            if (jsonHolder.optBoolean("flossing"))
                Hyperium.INSTANCE.getHandlers().getFlossDanceHandler().get(uuid).ensureDancingFor(60);
            else Hyperium.INSTANCE.getHandlers().getFlossDanceHandler().get(uuid).stopDancing();
        } else if (type.equalsIgnoreCase("flip_update")) {
            boolean flipped = jsonHolder.optBoolean("flipped");
            if (flipped)
                Hyperium.INSTANCE.getHandlers().getFlipHandler().state(uuid, 1);
            else if (jsonHolder.has("flip_state")) {
                Hyperium.INSTANCE.getHandlers().getFlipHandler().state(uuid, jsonHolder.optInt("flip_state"));
            } else {
                Hyperium.INSTANCE.getHandlers().getFlipHandler().state(uuid, 0);
            }
        } else if(type.equalsIgnoreCase("refresh_cosmetics")) {
            if(Minecraft.getMinecraft().currentScreen instanceof ShopGui) {
                ((ShopGui) Minecraft.getMinecraft().currentScreen).refreshData();
            } else {
                PurchaseApi.getInstance().refreshSelf();
            }
        }
    }

    @Override
    public void party(List<String> list) {
        for (String s : list) {
            Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/party invite " + s);
        }
    }

    @Override
    public void setLeader(String s) {
        Hyperium.INSTANCE.getConfirmation().setAcceptFrom(s);
    }
}
