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

package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.RenderUtils;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class CapesGui extends HyperiumGui implements GuiYesNoCallback {

    private int purchaseIds;
    private Map<String, DynamicTexture> textures = new ConcurrentHashMap<>();
    private Map<String, BufferedImage> texturesImage = new ConcurrentHashMap<>();
    private JsonHolder cosmeticCallback = new JsonHolder();
    private boolean purchasing;
    private HashMap<Integer, Runnable> ids = new HashMap<>();
    private HashMap<String, Integer> intMap = new HashMap<>();

    public CapesGui() {
        guiScale = 2;
        scrollMultiplier = 2;
        updatePurchases();

        Multithreading.runAsync(() -> {
            JsonHolder capeAtlas = PurchaseApi.getInstance().getCapeAtlas();
            for (String s : capeAtlas.getKeys()) {
                Multithreading.runAsync(() -> {
                    JsonHolder jsonHolder = capeAtlas.optJSONObject(s);
                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL(jsonHolder.optString("url"));
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setUseCaches(true);
                        connection.addRequestProperty("User-Agent", "Mozilla/4.76 Hyperium ");
                        connection.setReadTimeout(15000);
                        connection.setConnectTimeout(15000);
                        connection.setDoOutput(true);
                        InputStream is = connection.getInputStream();
                        BufferedImage img = ImageIO.read(ImageIO.createImageInputStream(is));
                        texturesImage.put(s, img);
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                });

            }
        });

    }

    @Override
    public void confirmClicked(boolean result, int id) {
        super.confirmClicked(result, id);
        if (result) {
            Runnable runnable = ids.get(id);
            if (runnable != null) runnable.run();
        }

        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(this);
    }

    @Override
    protected void pack() {
        reg("RESET", new GuiButton(nextId(), 1, 1, "Disable Hyperium Cape"), guiButton -> {
            NettyClient client = NettyClient.getClient();
            if (client != null) {
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("set_cape", true).put("value", "default")));
            }

            HyperiumPurchase self = PurchaseApi.getInstance().getSelf();

            if (self == null) {
                GeneralChatHandler.instance().sendMessage("Unable to reset your cape: Your profile is not loaded");
                return;
            }

            JsonHolder purchaseSettings = self.getPurchaseSettings();

            if (!purchaseSettings.has("cape")) {
                purchaseSettings.put("cape", new JsonHolder());
            }

            purchaseSettings.optJSONObject("cape").put("type", "default");
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("You may need to switch worlds to update your cape.");
        }, guiButton -> {
        });

        reg("CUSTOM", new GuiButton(nextId(), 1, 22, "Custom Cape"), guiButton -> {
            Desktop desktop = Desktop.getDesktop();
            if (desktop != null) {
                try {
                    desktop.browse(new URL("https://capes.hyperium.cc").toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, guiButton -> {
        });
    }

    //22x17
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        try {
            if (!texturesImage.isEmpty()) {
                texturesImage.forEach((s, value) -> {
                    if (!textures.containsKey(s))
                        textures.put(s, new DynamicTexture(value));
                });
            }

            super.drawScreen(mouseX, mouseY, partialTicks);
            int oldScale = mc.gameSettings.guiScale;
            mc.gameSettings.guiScale = 2;

            ScaledResolution current = new ScaledResolution(Minecraft.getMinecraft());
            mc.gameSettings.guiScale = oldScale;
            float v = 2F / (oldScale);
            GlStateManager.scale(v, v, v);
            int blockWidth = 128;
            int blockHeight = 256;

            int blocksPerLine = (int) (current.getScaledWidth() / (1.5D * blockWidth));
            if (blocksPerLine % 2 == 1) blocksPerLine--;

            JsonHolder capeAtlas = PurchaseApi.getInstance().getCapeAtlas();

            int totalRows = (capeAtlas.getKeys().size() / blocksPerLine + (capeAtlas.getKeys().size() % blocksPerLine == 0 ? 0 : 1));
            int row = 0;
            int pos = 1;
            int printY = 15 - offset;

            GlStateManager.scale(2F, 2F, 2F);
            fontRendererObj.drawString("Capes", (current.getScaledWidth() / 2F - fontRendererObj.getStringWidth("Capes")) / 2,
                printY / 2F, new Color(249, 99, 0).getRGB(), true);
            String s1;

            try {
                s1 = PurchaseApi.getInstance().getSelf().getPurchaseSettings()
                    .optJSONObject("cape").optString("type");
            } catch (NullPointerException ignored) {
                return;
            }

            String s2 = capeAtlas.optJSONObject(s1).optString("name");
            if (s2.isEmpty()) s2 = "NONE";
            String text = "Active Cape: " + s2;
            fontRendererObj.drawString(text, (current.getScaledWidth() / 2F - fontRendererObj.getStringWidth(text)) / 2, (printY + 20) / 2F, new Color(249, 99, 0).getRGB(), true);
            text = "Need more credits? Click here";

            int stringWidth1 = fontRendererObj.getStringWidth(text);
            int i2 = current.getScaledWidth() / 2 - stringWidth1;
            int i3 = printY + 40;

            fontRendererObj.drawString(text, i2 / 2F, i3 / 2F, new Color(97, 132, 249).getRGB(), true);
            GuiBlock block1 = new GuiBlock(i2, i2 + stringWidth1 * 2, i3, i3 + 15);
            GlStateManager.scale(.5F, .5F, .5F);

            actions.put(block1, () -> {
                Desktop desktop = Desktop.getDesktop();
                if (desktop != null) {
                    try {
                        desktop.browse(new URL("https://purchase.sk1er.club/category/1125808").toURI());
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });

            printY += 25;
            printY += 35;
            int scaledWidth = current.getScaledWidth();
            RenderUtils.drawSmoothRect(scaledWidth / 2 - (blockWidth + 16) * blocksPerLine / 2, printY - 4,
                scaledWidth / 2 + (blockWidth + 16) * blocksPerLine / 2, printY +
                    (blockHeight + 16) * totalRows + 4, new Color(53, 106, 110).getRGB());

            for (String s : capeAtlas.getKeys()) {
                JsonHolder cape = capeAtlas.optJSONObject(s);
                if (cape.optBoolean("private")) continue;

                if (pos > blocksPerLine) {
                    pos = 1;
                    row++;
                }

                int thisBlocksCenter = (int) (scaledWidth / 2 - ((blocksPerLine / 2) - pos + .5) * (blockWidth + 16));
                int thisTopY = printY + row * (16 + blockHeight);
                RenderUtils.drawSmoothRect(thisBlocksCenter - blockWidth / 2, thisTopY, (thisBlocksCenter + blockWidth / 2), thisTopY + blockHeight, -1);
                DynamicTexture dynamicTexture = textures.get(s);

                if (dynamicTexture != null) {
                    int imgW = 120;
                    int imgH = 128;
                    GlStateManager.bindTexture(dynamicTexture.getGlTextureId());
                    float capeScale = .75F;
                    int topLeftX = (int) (thisBlocksCenter - imgW / (2F / capeScale));
                    int topLeftY = thisTopY + 4;

                    GlStateManager.translate(topLeftX, topLeftY, 0);
                    GlStateManager.scale(capeScale, capeScale, capeScale);
                    drawTexturedModalRect(0, 0, imgW / 12, 0, imgW, imgH * 2);
                    GlStateManager.scale(1F / capeScale, 1F / capeScale, 1F / capeScale);
                    GlStateManager.translate(-topLeftX, -topLeftY, 0);
                }

                String nameCape = cape.optString("name");
                GlStateManager.scale(2F, 2F, 2F);
                int x = thisBlocksCenter - fontRendererObj.getStringWidth(nameCape);
                fontRendererObj.drawString(nameCape, x / 2F, (thisTopY - 8 + blockHeight / 2F + 64 + 16) / 2, new Color(249, 99, 0).getRGB(), true);
                GlStateManager.scale(.5F, .5F, .5F);

                if (cosmeticCallback.getKeys().size() == 0 || purchasing) {
                    String string = "Loading";
                    fontRendererObj.drawString(string, thisBlocksCenter - fontRendererObj.getStringWidth(string), (thisTopY - 8 +
                        (blockHeight >> 1) + 64 + 48), new Color(91, 102, 249).getRGB(), true);
                    return;
                }

                JsonHolder jsonHolder = cosmeticCallback.optJSONObject(s);
                boolean purchased = jsonHolder.optBoolean("purchased");
                if (purchased) {
                    String string = "Purchased";
                    int widthThing3 = fontRendererObj.getStringWidth(string) / 2;
                    int leftThing3 = thisBlocksCenter - widthThing3;
                    int topThing3 = thisTopY - 8 + blockHeight / 2 + 64 + 36;
                    fontRendererObj.drawString(string, leftThing3, topThing3, new Color(41, 249, 18).getRGB(), true);

                    if (s.equalsIgnoreCase(s1)) {
                        string = "Active";
                        int stringWidth = fontRendererObj.getStringWidth(string);
                        int i = thisBlocksCenter - stringWidth;
                        int i1 = thisTopY - 8 + blockHeight / 2 + 64 + 48;
                        GlStateManager.scale(2F, 2F, 2F);
                        fontRendererObj.drawString(string, i / 2F, i1 / 2F, new Color(249, 55, 241).getRGB(), true);
                    } else {
                        int stringWidth = fontRendererObj.getStringWidth(string);
                        int i = thisBlocksCenter - stringWidth;
                        int i1 = thisTopY - 8 + blockHeight / 2 + 64 + 48;
                        string = "Make Active";
                        GuiBlock block = new GuiBlock(i, i + stringWidth * 2, i1, i1 + 20);

                        actions.put(block, () -> {
                            JsonHolder purchaseSettings = PurchaseApi.getInstance().getSelf().getPurchaseSettings();

                            if (!purchaseSettings.has("cape")) {
                                purchaseSettings.put("cape", new JsonHolder());
                            }

                            purchaseSettings.optJSONObject("cape").put("type", s);
                            NettyClient client = NettyClient.getClient();

                            if (client != null) {
                                client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("set_cape", true).put("value", s)));
                            }

                            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("You may need to switch worlds to update your cape.");
                        });

                        GlStateManager.scale(2F, 2F, 2F);
                        fontRendererObj.drawString(string, i / 2F, i1 / 2F, new Color(249, 55, 241).getRGB(), true);
                    }

                    GlStateManager.scale(.5F, .5F, .5F);
                } else {
                    if (jsonHolder.optBoolean("enough")) {
                        String string = "Click to purchase";
                        int stringWidth = fontRendererObj.getStringWidth(string);
                        int left = thisBlocksCenter - stringWidth / 2;
                        int i = thisTopY - 8 + blockHeight / 2 + 64 + 48;
                        fontRendererObj.drawString(string, left, i, new Color(249, 76, 238).getRGB(), true);
                        GuiBlock block = new GuiBlock(left, left + stringWidth, i, i + 10);

                        actions.put(block, () -> {
                            Hyperium.LOGGER.debug("Attempting to purchase " + s);
                            purchasing = true;
                            Integer integer = intMap.computeIfAbsent(s, s3 -> ++purchaseIds);
                            GuiYesNo gui = new GuiYesNo(this, "Purchase " + s, "", integer);
                            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(gui);

                            ids.put(integer, () -> {
                                GeneralChatHandler.instance().sendMessage("Attempting to purchase " + s);
                                NettyClient client = NettyClient.getClient();
                                if (client != null) {
                                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("cosmetic_purchase", true).put("value", s)));
                                }
                            });
                        });
                    } else {
                        String string = "Insufficient Credits";
                        int stringWidth = fontRendererObj.getStringWidth(string);
                        int left = thisBlocksCenter - stringWidth / 2;
                        int i = thisTopY - 8 + blockHeight / 2 + 64 + 48;
                        fontRendererObj.drawString(string, left, i, new Color(249, 9, 0).getRGB(), true);

                    }
                }
                pos++;
            }
            GlStateManager.scale(1F / v, 1F / v, 1F / v);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updatePurchases() {
        Multithreading.runAsync(() -> {
            cosmeticCallback = PurchaseApi.getInstance().get(
                "https://api.hyperium.cc/cosmetics/" + Objects.requireNonNull(UUIDUtil.getClientUUID()).toString()
                    .replace("-", ""));
            purchasing = false;
        });
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
