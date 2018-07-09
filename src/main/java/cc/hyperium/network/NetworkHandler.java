package cc.hyperium.network;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.CapesGui;
import cc.hyperium.gui.CustomLevelheadConfigurer;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.netty.INetty;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
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
        return UUIDUtil.getClientUUID();
    }

    @Override
    public String getPlayerName() {
        return Minecraft.getMinecraft().getSession().getProfile().getName();
    }

    @Override
    public void handleChat(String s) {
        if (s.toLowerCase().contains("reconnecting hyperium connection"))
            return;
        System.out.println("Chat: " + s);
        GeneralChatHandler.instance().sendMessage(s, false);
    }

    @Override
    public void handleCrossClientData(UUID uuid, JsonHolder jsonHolder) {
        String type = jsonHolder.optString("type");
        if (type.equalsIgnoreCase("dab_update"))
            if (jsonHolder.optBoolean("dabbing"))
                Hyperium.INSTANCE.getHandlers().getDabHandler().get(uuid).ensureAnimationFor(60);
            else Hyperium.INSTANCE.getHandlers().getDabHandler().get(uuid).stopAnimation();
        else if (type.equalsIgnoreCase("floss_update")) {
            if (jsonHolder.optBoolean("flossing"))
                Hyperium.INSTANCE.getHandlers().getFlossDanceHandler().get(uuid).ensureAnimationFor(60);
            else Hyperium.INSTANCE.getHandlers().getFlossDanceHandler().get(uuid).stopAnimation();
        } else if (type.equalsIgnoreCase("flip_update")) {
            boolean flipped = jsonHolder.optBoolean("flipped");
            if (flipped)
                Hyperium.INSTANCE.getHandlers().getFlipHandler().state(uuid, 1);
            else if (jsonHolder.has("flip_state")) {
                Hyperium.INSTANCE.getHandlers().getFlipHandler().state(uuid, jsonHolder.optInt("flip_state"));
            } else {
                Hyperium.INSTANCE.getHandlers().getFlipHandler().state(uuid, 0);
            }
        } else if (type.equalsIgnoreCase("refresh_cosmetics")) {
            if (Minecraft.getMinecraft().currentScreen instanceof HyperiumMainGui) {
                ((HyperiumMainGui) Minecraft.getMinecraft().currentScreen).getCosmeticsTab().refreshData();
            } else if (Minecraft.getMinecraft().currentScreen instanceof CapesGui) {
                ((CapesGui) Minecraft.getMinecraft().currentScreen).updatePurchases();
            } else {
                PurchaseApi.getInstance().refreshSelf();
            }
        } else if (type.equalsIgnoreCase("custom_levelhead_success")) {
            if (Minecraft.getMinecraft().currentScreen instanceof CustomLevelheadConfigurer)
                Minecraft.getMinecraft().displayGuiScreen(null);
        } else if (type.equalsIgnoreCase("cache_update")) {
            PurchaseApi.getInstance().reload(UUID.fromString(jsonHolder.optString("uuid")));

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
