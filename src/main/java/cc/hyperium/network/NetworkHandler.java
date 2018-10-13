package cc.hyperium.network;

import cc.hyperium.C;
import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.PostConfigHandler;
import cc.hyperium.config.PreSaveHandler;
import cc.hyperium.gui.CapesGui;
import cc.hyperium.gui.CustomLevelheadConfigurer;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.netty.INetty;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NetworkHandler implements INetty, PostConfigHandler, PreSaveHandler {

    @ConfigOpt
    public boolean log = false;
    private boolean post = false;
    private List<String> verboseLogs = new ArrayList<>();

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

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
        s = s.replace("&", C.COLOR_CODE_SYMBOL);
        IChatComponent chatComponent = new ChatComponentText("");
        for (String s1 : s.split(" ")) {
            ChatComponentText iChatComponents = new ChatComponentText(s1 + " ");
            if (s1.contains(".") && !s1.startsWith(".") && !s1.endsWith(".")) {
                ChatStyle chatStyle = new ChatStyle();
                chatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, s1.startsWith("http") ? s1 : "http://" + s1));
                iChatComponents.setChatStyle(chatStyle);
            }
            chatComponent.appendSibling(iChatComponents);
        }
        GeneralChatHandler.instance().sendMessage(chatComponent);
    }

    @Override
    public void handleCrossClientData(UUID uuid, JsonHolder jsonHolder) {
        String type = jsonHolder.optString("type");
        if (type.equalsIgnoreCase("fortnite_default_dance")) {
            Hyperium.INSTANCE.getHandlers().getFortniteDefaultDance().startAnimation(uuid);
            Hyperium.INSTANCE.getHandlers().getFortniteDefaultDance().getStates().put(uuid, System.currentTimeMillis());
        } else if (type.equalsIgnoreCase("yeet")) {
            Hyperium.INSTANCE.getHandlers().getYeetHandler().yeet(uuid);
        } else if (type.equalsIgnoreCase("twerk_dance")) {
            Hyperium.INSTANCE.getHandlers().getTwerkDance().startAnimation(uuid);
            Hyperium.INSTANCE.getHandlers().getTwerkDance().getStates().put(uuid, System.currentTimeMillis());
        } else if (type.equalsIgnoreCase("tpose_update"))
            if (jsonHolder.optBoolean("posing"))
                Hyperium.INSTANCE.getHandlers().getTPoseHandler().get(uuid).ensureAnimationFor(60);
            else Hyperium.INSTANCE.getHandlers().getTPoseHandler().stopAnimation(uuid);
        else if (type.equalsIgnoreCase("dab_update"))
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
            if (Minecraft.getMinecraft().currentScreen instanceof CapesGui) {
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

    public boolean isPost() {
        return post;
    }

    public List<String> getVerboseLogs() {
        return verboseLogs;
    }

    @Override
    public void addVerboseLog(String s) {
        if (log)
            verboseLogs.add(s);
    }

    @Override
    public void postUpdate() {
        if (log)
            post = true;
    }

    @Override
    public void preSave() {
        if (post)
            log = false;
    }
}
