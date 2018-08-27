package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.CapesGui;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.ParticleGui;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;

public class CosmeticsTab extends AbstractTab implements GuiYesNoCallback {

    private int y, w;

    private GuiBlock block;
    private DecimalFormat formatter = new DecimalFormat("#,###");
    private SettingItem credits;
    private boolean purchasing = false;
    private JsonHolder personData = null;
    private JsonHolder cosmeticCallback = null;
    private HashMap<String, SettingItem> cosmetics = new HashMap<>();
    private int tx = 0;
    private int ty = 1;
    private HashMap<Integer, Runnable> ids = new HashMap<>();
    private int purchaseIds = 0;

    public CosmeticsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;
        items.add(new SettingItem(() -> new CapesGui().show(), Icons.COSMETIC.getResource(), "Capes", "Browse and select Hyperium Capes", "Click to open", 0, 0));

        credits = new SettingItem(() -> {
            Desktop desktop = Desktop.getDesktop();
            if (desktop != null) {
                try {
                    desktop.browse(new URL("https://purchase.sk1er.club/category/1125808").toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, Icons.COSMETIC.getResource(), "Credits", "", "Credits are used to purchase cosmetics", 1, 0);
        items.add(credits);

        SettingItem auras = new SettingItem(() -> {
            new ParticleGui().show();
        }, Icons.COSMETIC.getResource(), "Auras", "Browse Particle Auras", "Browse Particle Auras", 2, 0);
        items.add(auras);

        rebuild();
        refreshData();
    }

    public void rebuild() {


        if (cosmeticCallback != null && !purchasing) {
            for (String s : cosmeticCallback.getKeys()) {

                JsonHolder jsonHolder = cosmeticCallback.optJSONObject(s);
                if (jsonHolder.optBoolean("cape"))
                    continue;

                String name = jsonHolder.optString("name");
                String description = jsonHolder.optString("description");
                int cost = jsonHolder.optInt("cost");
                boolean purchased = jsonHolder.optBoolean("purchased");
                boolean enough = jsonHolder.optBoolean("enough");
                String state = purchased ? "Purchased" : (enough ? "Click to purchase" : "Insufficient credits");
                Runnable runnable = () -> {
                    if (!purchased && enough) {
                        int i4 = ++purchaseIds;
                        GuiYesNo gui = new GuiYesNo(this, "Purchase " + s, "", i4);
                        Minecraft.getMinecraft().displayGuiScreen(gui);
                        ids.put(i4, () -> {
                            refreshData();
                            GeneralChatHandler.instance().sendMessage("Attempting to purchase " + s);
                            purchasing = true;
                            NettyClient client = NettyClient.getClient();
                            if (client != null) {
                                client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("cosmetic_purchase", true).put("value", s)));
                            }
                        });

                    } else if (purchased) {
                        GeneralChatHandler.instance().sendMessage("Already purchased " + name);
                    }
                };
                SettingItem settingItem = cosmetics.computeIfAbsent(s, s1 -> {

                    if (tx >= 3) {
                        ty++;
                        tx = 0;
                    }

                    SettingItem e = new SettingItem(runnable, Icons.COSMETIC.getResource(), name, description + " \n State: " + state, "Cost: " + cost, tx, ty);
                    items.add(e);
                    tx++;
                    return e;


                });
                settingItem.setDesc(description + (purchased ? "" : " \nCost: " + cost));

                settingItem.setTitle(name);
                settingItem.setOnClick(runnable);

            }
        }
    }

    @Override
    public void confirmClicked(boolean result, int id) {

        if (result) {
            Runnable runnable = ids.get(id);
            if (runnable != null) {
                runnable.run();
            }
        }
        Minecraft.getMinecraft().displayGuiScreen(HyperiumMainGui.INSTANCE);
    }

    @Override
    public void drawTabIcon() {
        rebuild();
        //Used as render tick
        if (PurchaseApi.getInstance() != null && PurchaseApi.getInstance().getSelf() != null && PurchaseApi.getInstance().getSelf().getResponse() != null) {
            JsonHolder response = PurchaseApi.getInstance().getSelf().getResponse();
            credits.setDesc("Total credits: " + formatter.format(response.optInt("total_credits")) + "" + " \n Remaining credits: " + formatter.format(response.optInt("remaining_credits")) + " \n Click to buy more!");
        }

        Icons.COSMETIC.bind();
        Gui.drawScaledCustomSizeModalRect(5, y + 5, 0, 0, 144, 144, w - 10, w - 10, 144, 144);
    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight(float s) {
        Gui.drawRect(0, (int) (y + s * (s * w / 2)), 3, (int) (y + w - s * (w / 2)), Color.WHITE.getRGB());
    }

    @Override
    public String getTitle() {
        return "Cosmetics";
    }

    public void refreshData() {
        Multithreading.runAsync(() -> {
            PurchaseApi.getInstance().refreshSelf();
            personData = PurchaseApi.getInstance().getSelf().getResponse();
            cosmeticCallback = PurchaseApi.getInstance().get("https://api.hyperium.cc/cosmetics/" + UUIDUtil.getClientUUID().toString().replace("-", ""));
            purchasing = false;
        });
    }

}
