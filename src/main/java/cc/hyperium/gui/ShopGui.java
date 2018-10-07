package cc.hyperium.gui;

import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 4/29/18. Designed for production use on Sk1er.club
 */
public class ShopGui extends HyperiumGui {


    private JsonHolder personData = null;
    private JsonHolder cosmeticCallback = null;
    private List<GuiBoxItem<Runnable>> list = new ArrayList<>();
    private DecimalFormat formatter = new DecimalFormat("#,###");
    private int cooldown = 0;
    private boolean purchasing = false;

    public ShopGui() {
        refreshData();
    }

    public void refreshData() {
        Multithreading.runAsync(() -> {
            PurchaseApi.getInstance().refreshSelf();
            personData = PurchaseApi.getInstance().getSelf().getResponse();
            cosmeticCallback = PurchaseApi.getInstance().get("https://api.hyperium.cc/cosmetics/" + UUIDUtil.getClientUUID().toString().replace("-", ""));
            purchasing = false;
        });
    }

    @Override
    protected void pack() {
        reg("CAPES", new GuiButton(nextId(), 1, 1, 100, 20, I18n.format("gui.shop.capes")),guiButton -> new CapesGui().show(), guiButton -> {

        });
        reg("WEB", new GuiButton(nextId(), 1, 21, 100, 20, I18n.format("gui.shop.browser")), guiButton -> {
            Desktop desktop = Desktop.getDesktop();
            if (desktop != null) {
                try {
                    desktop.browse(new URL("https://hyperium.sk1er.club").toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, guiButton -> {

        });
        reg("MORE", new GuiButton(nextId(), 1, 41, 100, 20, I18n.format("gui.shop.purchasecredits")), guiButton -> {
            Desktop desktop = Desktop.getDesktop();
            if (desktop != null) {
                try {
                    desktop.browse(new URL("https://purchase.sk1er.club/category/1125808").toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, guiButton -> {

        });
        reg("REFRESH", new GuiButton(nextId(), 1, 61, 100, 20, I18n.format("gui.shop.refresh")), guiButton -> {
            refreshData();
            cooldown = 0;
            purchasing = false;
        }, guiButton -> {
            cooldown++;
            guiButton.enabled = cooldown > 20;
        });
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean f = false;
        for (GuiBoxItem<Runnable> runnableGuiBoxItem : list) {
            if (runnableGuiBoxItem.getBox().isMouseOver(mouseX, mouseY) && mouseButton == 0) {
                runnableGuiBoxItem.getObject().run();
                f = true;
            }
        }
        if (f)
            list.clear();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        list.clear();
        int yLevel = 50 - offset;
        if (personData != null) {
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            String credits = I18n.format("gui.shop.credits");
            fontRendererObj.drawString(credits, (width / 2 - fontRendererObj.getStringWidth(credits)) / 2, (15 + yLevel) / 2, Color.RED.getRGB(), true);

            GlStateManager.scale(.5F, .5F, .5F);

            String total_credits = I18n.format("gui.cosmetics.totalcredits", formatter.format(personData.optInt("total_credits")));
            fontRendererObj.drawString(total_credits, width / 2 - fontRendererObj.getStringWidth(total_credits) / 2, 32 + yLevel, Color.RED.getRGB(), true);
            String rem_credits = I18n.format("gui.cosmetics.remainingcredits", formatter.format(personData.optInt("remaining_credits")));
            fontRendererObj.drawString(rem_credits, width / 2 - fontRendererObj.getStringWidth(rem_credits) / 2, 45 + yLevel, Color.RED.getRGB(), true);

            yLevel += 75;
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            String cosmetics = I18n.format("gui.shop.cosmetics");
            fontRendererObj.drawString(cosmetics, (width / 2 - fontRendererObj.getStringWidth(cosmetics)) / 2, (yLevel) / 2, Color.RED.getRGB(), true);
            GlStateManager.scale(.5F, .5F, .5F);

            yLevel += 20;

            if (cosmeticCallback != null && !purchasing) {
                for (String s : cosmeticCallback.getKeys()) {
                    yLevel += 10;
                    JsonHolder jsonHolder = cosmeticCallback.optJSONObject(s);
                    if (jsonHolder.optBoolean("cape"))
                        return;
                    GlStateManager.scale(1.5F, 1.5F, 1.5F);
                    String name = jsonHolder.optString("name");
                    fontRendererObj.drawString(name, 150 / 2, (yLevel) / 1.5F, Color.RED.getRGB(), true);
                    yLevel += 15;
                    GlStateManager.scale(1 / 1.5F, 1 / 1.5F, 1 / 1.5F);

                    String description = jsonHolder.optString("description");
                    boolean first = true;
                    for (String s1 : fontRendererObj.listFormattedStringToWidth(description, ResolutionUtil.current().getScaledWidth() - 200)) {
                        String line = first ? I18n.format("gui.shop.description", s1) : s1;
                        fontRendererObj.drawString(line, 150, yLevel, Color.RED.getRGB(), true);
                        first = false;
                        yLevel += 10;
                    }
                    int cost = jsonHolder.optInt("cost");
                    fontRendererObj.drawString(I18n.format("gui.shop.cost", cost), 150, yLevel, Color.RED.getRGB());
                    yLevel += 10;
                    String status = I18n.format("gui.shop.status");
                    fontRendererObj.drawString(status, 150, yLevel, Color.RED.getRGB(), true);
                    int tx = fontRendererObj.getStringWidth(status);
                    if (jsonHolder.optBoolean("purchased")) {
                        fontRendererObj.drawString(I18n.format("gui.shop.purchased"), 150 + tx, yLevel, new Color(68, 77, 202).getRGB(), true);
                    } else {
                        if (jsonHolder.optBoolean("enough")) {
                            GuiBoxItem<Runnable> tmp = new GuiBoxItem<>(new GuiBlock(150 + tx, 60 + tx, yLevel, yLevel + 10), () -> {
                                System.out.println("Attempting to purchase " + s);
                                purchasing = true;
                                NettyClient client = NettyClient.getClient();
                                if (client != null) {
                                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("cosmetic_purchase", true).put("value", s)));
                                }
                            });
                            tmp.getBox().drawString(I18n.format("gui.shop.purchase"), fontRendererObj, true, false, 0, 0, true, true, new Color(117, 193, 129).getRGB(), true);
                            list.add(tmp);
                        } else {
                            fontRendererObj.drawString(I18n.format("gui.shop.insufficientcredits"), 150 + tx, yLevel, Color.RED.getRGB(), true);
                        }
                    }
                }


            } else {
                String loading = I18n.format("gui.shop.loading.cosmetics");
                fontRendererObj.drawString(loading, width / 2 - fontRendererObj.getStringWidth(loading) / 2, 55 + yLevel, Color.RED.getRGB(), true);
            }
        } else {
            String loading = I18n.format("gui.shop.loading.person");
            fontRendererObj.drawString(loading, width / 2 - fontRendererObj.getStringWidth(loading) / 2, yLevel, Color.RED.getRGB(), true);
        }
    }
}
