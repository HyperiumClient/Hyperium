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

package cc.hyperium.mods.levelhead.guis;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.levelhead.display.*;
import cc.hyperium.mods.levelhead.purchases.LevelheadPurchaseStates;
import cc.hyperium.mods.levelhead.renderer.LevelheadChatRenderer;
import cc.hyperium.mods.levelhead.util.LevelheadJsonHolder;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.*;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;

import static cc.hyperium.utils.ChatColor.*;

public class LevelheadGui extends GuiScreen implements GuiYesNoCallback {

    private static final String COLOR_CHAR = "\u00a7";
    private final String colors = "0123456789abcdef";
    private int currentID = 2;
    private HashMap<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private HashMap<Integer, Runnable> ids = new HashMap<>();
    private LevelheadDisplay currentlyBeingEdited;
    private boolean bigChange = false;
    private boolean isCustom = false;
    private GuiTextField textField;
    private boolean purchasingStats = false;
    private int offset = 0;
    private UUID uuid = Minecraft.getMinecraft().getSession().getProfile().getId();

    @Override
    public void initGui() {
        mc.gameSettings.hideGUI = true;

        Multithreading.runAsync(() -> {
            String raw = Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/levelhead/" + uuid.toString().replace("-", ""));
            Hyperium.LOGGER.debug(raw);
            isCustom = new JsonHolder(raw).optBoolean("custom");
        });

        textField = new GuiTextField(-500, fontRendererObj, width - 124, 74, 120, 19);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        Levelhead instance = Levelhead.getInstance();
        currentID = 0;
        buttonList.clear();

        drawScaledText(UNDERLINE.toString() + BOLD + "Levelhead " + instance.getVersion(), width / 2, 5, 2, -1, true, true);
        drawScaledText("By Sk1er LLC", width / 2, 30, 1.5, -1, true, true);

        reg(new GuiButton(++currentID, 1, 2, 150, 20,
                YELLOW + "Mod Status: " + (instance.getDisplayManager().getMasterConfig().isEnabled() ? GREEN + "Enabled" : RED + "Disabled")),
            button -> {
                instance.getDisplayManager().getMasterConfig().setEnabled(!instance.getDisplayManager().getMasterConfig().isEnabled());
                button.displayString = "Mod Status: " + (instance.getDisplayManager().getMasterConfig().isEnabled() ? GREEN + "Enabled" : RED + "Disabled");
            });

        FontRenderer fontRenderer = mc.fontRendererObj;

        if (purchasingStats) {
            reg(new GuiButton(++currentID, 1, height - 21, 100, 20, "Back"), guiButton ->
                purchasingStats = false);

            drawScaledText("Purchase Extra Stats", width / 2, 50, 2.0, -1, true, true);
            drawScaledText("These stats can be displayed above players' heads, in lobbies or in tab.", width / 2, 67, 1, -1,
                true, true);

            LevelheadJsonHolder stats = instance.getPaidData().optJsonObject("stats");

            int pos = 75 - offset;

            for (String key : stats.getKeys()) {
                LevelheadJsonHolder jsonHolder = stats.optJsonObject(key);
                boolean purchased = instance.getPurchaseStatus().optBoolean(key);
                String name = jsonHolder.optString("name");
                if (purchased) name += " (Purchased)";

                if (pos > 74 && pos < width - 100) {
                    reg(new GuiButton(++currentID, width / 2 - 100, pos, name), guiButton -> {
                        if (!purchased)
                            attemptPurchase(key);
                    });
                }

                pos += 22;
            }
        } else {
            reg(new GuiButton(++currentID, 1, 44, 150, 20, YELLOW + "Purchase Extra Stats"), guiButton ->
                purchasingStats = true);

            if (currentlyBeingEdited == null) {
                currentlyBeingEdited = instance.getDisplayManager().getAboveHead().get(0);
                textField.setText(currentlyBeingEdited.getConfig().getCustomHeader());
            }

            int index = 0;

            DisplayConfig config = currentlyBeingEdited.getConfig();

            int editWidth = 125;
            doGeneral(config, instance, editWidth);

            if (currentlyBeingEdited instanceof AboveHeadDisplay) {
                doHead(config, instance, editWidth, mouseX, mouseY);
                index = 0;
            } else if (currentlyBeingEdited instanceof ChatDisplay) {
                doChat(config, instance, editWidth);
                index = 1;
            } else if (currentlyBeingEdited instanceof TabDisplay) {
                doTab(instance);
                index = 2;
            }

            String[] types =
                {
                    "Above Head Display",
                    "Chat Display",
                    "Tab Display"
                };

            drawScaledText(LIGHT_PURPLE + "Levelhead Credits: " + AQUA + instance.getRawPurchases().optInt("remaining_levelhead_credits"),
                width / 3, height - 90,
                1.5D, -1, true, true);

            drawScaledText((currentlyBeingEdited instanceof AboveHeadDisplay ? GREEN : (currentlyBeingEdited instanceof TabDisplay &&
                    instance.getLevelheadPurchaseStates().isTab()
                    ? GREEN : (currentlyBeingEdited instanceof ChatDisplay && instance.getLevelheadPurchaseStates().isChat() ? GREEN : RED))) + types[index], width / 3,
                height - 110, 1.25, new Color(255, 255, 255).getRGB(), true, true);
            int finalIndex = index;

            reg(new GuiButton(++currentID, width / 3 - 65 - 20, height - 115, 20, 20, "<"), guiButton -> {
                if (finalIndex == 0) {
                    currentlyBeingEdited = instance.getDisplayManager().getChat();
                } else if (finalIndex == 1) {
                    currentlyBeingEdited = instance.getDisplayManager().getTab();
                } else {
                    currentlyBeingEdited = instance.getDisplayManager().getAboveHead().get(0);
                }
            });

            reg(new GuiButton(++currentID, width / 3 + 65, height - 115, 20, 20, ">"), guiButton -> {
                if (finalIndex == 0) {
                    currentlyBeingEdited = instance.getDisplayManager().getTab();
                } else if (finalIndex == 1) {
                    currentlyBeingEdited = instance.getDisplayManager().getAboveHead().get(0);
                } else {
                    currentlyBeingEdited = instance.getDisplayManager().getChat();
                }
            });

            if (bigChange) {
                int start = height - 25;
                int i = 0;

                for (String s : "Some changes cannot be applied in real time.\nThey will be applied once you close this GUI".split("\n")) {
                    fontRenderer.drawString(s, width - fontRenderer.getStringWidth(s) - 5, start + (i * 10), Color.RED.getRGB(), true);
                    i++;
                }
            }

            drawScaledText(LIGHT_PURPLE + "Custom Levelhead Status: " + (isCustom ? GREEN + "Enabled" : RED + "Disabled"),
                2, height - 55, 1.25, -1,
                true, false);

            reg(new GuiButton(++currentID, 2, height - 44, 220, 20, (isCustom ? ChatColor.YELLOW + "Click to change custom Levelhead." :
                ChatColor.YELLOW + "Click to purchase custom Levelhead")), button -> {
                try {
                    if (isCustom) {
                        Minecraft.getMinecraft().displayGuiScreen(new CustomLevelheadConfigurer());
                    } else {
                        Desktop.getDesktop().browse(new URI("http://sk1er.club/customlevelhead"));
                    }
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            });

            reg(new GuiButton(++currentID, 1, 23, 150, 20, YELLOW + "Purchase Levelhead Credits"), button -> {
                Desktop desktop = Desktop.getDesktop();
                if (desktop != null) {
                    try {
                        desktop.browse(new URI("https://purchase.sk1er.club/category/1050972"));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> buttonConsumer = clicks.get(button);
        if (buttonConsumer != null) buttonConsumer.accept(button);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (currentlyBeingEdited instanceof AboveHeadDisplay) textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (currentlyBeingEdited instanceof AboveHeadDisplay) textField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();

        if (i < 0) offset += 11;
        else if (i > 0) offset -= 11;
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        super.confirmClicked(result, id);

        if (result) {
            Runnable runnable = ids.get(id);
            if (runnable != null) {
                runnable.run();
            }
        }

        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(this);
    }

    @Override
    public void onGuiClosed() {
        mc.gameSettings.hideGUI = false;
        if (bigChange) Levelhead.getInstance().getDisplayManager().clearCache();
        Levelhead.getInstance().getDisplayManager().save();
        Hyperium.CONFIG.save();
    }

    private void doGeneral(DisplayConfig config, Levelhead instance, int editWidth) {
        reg(new GuiButton(++currentID, width - editWidth - 1, 29, editWidth, 20, YELLOW + "Status: "
            + (config.isEnabled() ? GREEN + "Enabled" : RED + "Disabled")), button -> config.setEnabled(!config.isEnabled()));

        reg(new GuiButton(++currentID, width - editWidth - 1, 50, editWidth, 20, YELLOW + "Type: "
            + AQUA + instance.getTypes().optJsonObject(config.getType()).optString("name")), button -> {
            String currentType = config.getType();
            HashMap<String, String> typeMap = instance.allowedTypes();
            Set<String> keys = typeMap.keySet();
            List<String> strings = new ArrayList<>(keys);
            strings.sort(String::compareTo);
            int i = strings.indexOf(config.getType()) + 1;

            if (i >= strings.size())
                i = 0;

            config.setType(strings.get(i));

            if (config.getCustomHeader().equalsIgnoreCase(typeMap.get(currentType))) {
                config.setCustomHeader(typeMap.get(strings.get(i)));

                if (currentlyBeingEdited instanceof AboveHeadDisplay) {
                    textField.setText(config.getCustomHeader());
                }
            }

            EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
            currentlyBeingEdited.getCache().remove(thePlayer.getUniqueID());
            currentlyBeingEdited.getTrueValueCache().remove(thePlayer.getUniqueID());
            bigChange = true;
        });
    }

    private void doHead(DisplayConfig config, Levelhead instance, int editWidth, int mouseX, int mouseY) {
        List<AboveHeadDisplay> aboveHead = instance.getDisplayManager().getAboveHead();

        textField.drawTextBox();

        if (!textField.getText().equalsIgnoreCase(config.getCustomHeader())) {
            config.setCustomHeader(textField.getText());
            updatePeopleToValues();
        }

        reg(new GuiButton(++currentID, width / 3 - 100, height - 137, 200, 20, YELLOW + "Purchase Additional Above Head Layer"), button ->
            attemptPurchase("head"));
        reg(new GuiButton(++currentID, width - editWidth * 2 - 3, 29, editWidth, 20, YELLOW + "Editing Layer: " +
                AQUA + (aboveHead.indexOf(currentlyBeingEdited) + 1)),
            button -> {
                int i = aboveHead.indexOf(currentlyBeingEdited);
                i++;

                if (i >= aboveHead.size()) {
                    i = 0;
                }

                currentlyBeingEdited = aboveHead.get(i);
                textField.setText(currentlyBeingEdited.getConfig().getCustomHeader());
            });

        reg(new GuiButton(++currentID, width - editWidth * 2 - 3, 50, editWidth, 20, YELLOW + "Show On Self: "
            + AQUA + (currentlyBeingEdited.getConfig().isShowSelf() ? "YES" : "NO")), button ->
            currentlyBeingEdited.getConfig().setShowSelf(!currentlyBeingEdited.getConfig().isShowSelf()));

        int colorConfigStart = 93 + 2;
        reg(new GuiButton(++currentID, width - editWidth * 2 - 2, colorConfigStart, editWidth, 20, YELLOW + "Header Mode: " +
            AQUA + getMode(true)), button -> {
            if (config.isHeaderRgb()) {
                config.setHeaderRgb(false);
                config.setHeaderChroma(true);
            } else if (config.isHeaderChroma()) {
                config.setHeaderRgb(false);
                config.setHeaderChroma(false);
            } else {
                config.setHeaderRgb(true);
                config.setHeaderChroma(false);
            }

            updatePeopleToValues();
        });

        if (config.isHeaderRgb()) {
            regSlider(new GuiSlider(++currentID, width - editWidth * 2 - 2, colorConfigStart + 22, editWidth, 20, YELLOW + "Header Red: "
                + AQUA, "", 0,
                255, config.getHeaderRed(), false, true, slider -> {
                config.setHeaderRed(slider.getValueInt());
                updatePeopleToValues();
            }));
            regSlider(new GuiSlider(++currentID, width - editWidth * 2 - 2, colorConfigStart + 44, editWidth, 20, YELLOW + "Header Green: "
                + AQUA, "", 0,
                255, config.getHeaderGreen(), false, true, slider -> {
                config.setHeaderGreen(slider.getValueInt());
                updatePeopleToValues();
            }));
            regSlider(new GuiSlider(++currentID, width - editWidth * 2 - 2, colorConfigStart + 66, editWidth, 20, YELLOW + "Header Blue: "
                + AQUA, "", 0,
                255, config.getHeaderBlue(), false, true, slider -> {
                config.setHeaderBlue(slider.getValueInt());
                updatePeopleToValues();
            }));
        } else if (!config.isHeaderChroma()) {
            reg(new GuiButton(++currentID, width - editWidth * 2 - 2, colorConfigStart + 22, editWidth, 20, config.getHeaderColor() +
                "Rotate Header Color"), button -> {
                int primaryId = colors.indexOf(removeColorChar(config.getHeaderColor()));

                if (++primaryId == colors.length()) {
                    primaryId = 0;
                }

                config.setHeaderColor(COLOR_CHAR + colors.charAt(primaryId));
                updatePeopleToValues();
            });
        }

        reg(new GuiButton(++currentID, width - editWidth - 1, colorConfigStart, editWidth, 20, YELLOW + "Footer Mode: " +
            AQUA + getMode(false)), button -> {
            updateFooterColorState(config);
            button.displayString = "Footer Mode: " + getMode(false);
            updatePeopleToValues();
        });

        if (config.isFooterRgb()) {
            regSlider(new GuiSlider(++currentID, width - editWidth - 1, colorConfigStart + 22, editWidth, 20, YELLOW + "Footer Red: " +
                AQUA, "", 0,
                255, config.getFooterRed(), false, true, slider -> {
                config.setFooterRed(slider.getValueInt());
                updatePeopleToValues();
            }));
            regSlider(new GuiSlider(++currentID, width - editWidth - 1, colorConfigStart + 44, editWidth, 20, YELLOW + "Footer Green: " +
                AQUA, "", 0,
                255, config.getFooterGreen(), false, true, slider -> {
                config.setFooterGreen(slider.getValueInt());
                updatePeopleToValues();
            }));
            regSlider(new GuiSlider(++currentID, width - editWidth - 1, colorConfigStart + 66, editWidth, 20, YELLOW + "Footer Blue: " +
                AQUA, "", 0,
                255, config.getFooterBlue(), false, true, slider -> {
                config.setFooterBlue(slider.getValueInt());
                updatePeopleToValues();
            }));
        } else if (!config.isFooterChroma()) {
            reg(new GuiButton(++currentID, width - editWidth - 1, colorConfigStart + 22, editWidth, 20, config.getFooterColor() +
                "Rotate Footer Color"), button ->
                incrementColor(config, false));
        }

        regSlider(new GuiSlider(++currentID, width - editWidth * 2 - 2, colorConfigStart + 88, editWidth, 20, YELLOW + "Vertical Offset: " +
            AQUA, "", -50,
            100, instance.getDisplayManager().getMasterConfig().getOffset() * 100D, false, true, slider ->
            instance.getDisplayManager().getMasterConfig().setOffset(slider.getValue() / 100D)));

        regSlider(new GuiSlider(++currentID, width - editWidth - 1, colorConfigStart + 88, editWidth, 20, YELLOW + "Font Size: " + AQUA,
            "", 1, 20,
            instance.getDisplayManager().getMasterConfig().getFontSize() * 10D, false, true, slider -> {
            instance.getDisplayManager().getMasterConfig().setFontSize(slider.getValue() / 10D);
            updatePeopleToValues();
        }));

        if (isCustom) {
            reg(new GuiButton(++currentID, width - editWidth * 2 - 2, colorConfigStart + 88 + 22, editWidth * 2 + 1, 20,
                    YELLOW + "Export Colors to Custom Levelhead"),
                guiButton -> {
                    final LevelheadJsonHolder object;
                    String encode;
                    String url;
                    ChatComponentText text;
                    ChatStyle style;
                    ChatComponentText valueIn;
                    ChatStyle style2;
                    object = new LevelheadJsonHolder();
                    object.put("header_obj", currentlyBeingEdited.getHeaderConfig());
                    object.put("footer_obj", currentlyBeingEdited.getFooterConfig());
                    try {
                        encode = URLEncoder.encode(object.toString(), "UTF-8");
                        url = "https://sk1er.club/user?levelhead_color=" + encode;
                        text = new ChatComponentText("Click here to update your custom Levelhead colors");
                        style = new ChatStyle();
                        style.setBold(true);
                        style.setColor(EnumChatFormatting.YELLOW);
                        style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                        valueIn = new ChatComponentText("Please be logged in to your Sk1er.club for this to work. Do /levelhead dumpcache after clicking to see new colors!");
                        valueIn.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                        style2 = new ChatStyle();
                        style2.setColor(EnumChatFormatting.RED);
                        valueIn.setChatStyle(style2);
                        style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, valueIn));
                        text.setChatStyle(style);
                        mc.thePlayer.addChatComponentMessage(valueIn);
                        mc.thePlayer.addChatComponentMessage(text);
                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                    }
                    mc.displayGuiScreen(null);
                });
        }

        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.translate(0, 0, 50);
        ScaledResolution current = new ScaledResolution(mc);
        int posX = current.getScaledWidth() / 3;
        int posY = current.getScaledHeight() / 2;
        GuiInventory.drawEntityOnScreen(posX, posY + 50, 50, posX - mouseX, posY - mouseY, mc.thePlayer);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();

        drawScaledText("Custom Prefix: ", width - editWidth * 3 / 2 - 3, 77, 1.5, -1, true, true);
    }

    private void incrementColor(DisplayConfig config, boolean header) {
        int primaryId = colors.indexOf(removeColorChar(header ? config.getHeaderColor() : config.getFooterColor()));

        if (++primaryId == colors.length()) {
            primaryId = 0;
        }

        String val = COLOR_CHAR + colors.charAt(primaryId);
        if (header) config.setHeaderColor(val);
        else config.setFooterColor(val);
        updatePeopleToValues();
    }

    private void doChat(DisplayConfig config, Levelhead instance, int editWidth) {
        EntityPlayerSP player = mc.thePlayer;
        LevelheadPurchaseStates levelheadPurchaseStates = instance.getLevelheadPurchaseStates();
        String formattedName = player.getDisplayName().getFormattedText();
        IChatComponent component = new ChatComponentTranslation(formattedName + WHITE + ": Levelhead rocks!");
        String chatTag = "Some stat";

        LevelheadDisplay chatDisplay = instance.getDisplayManager().getChat();

        if (chatDisplay != null && levelheadPurchaseStates.isChat()) {
            chatTag = chatDisplay.getTrueValueCache().get(player.getUniqueID());
        }

        IChatComponent chatComponent = LevelheadChatRenderer.modifyChat(component, chatTag, chatDisplay != null ? chatDisplay.getConfig() : new DisplayConfig());
        String fakeChatText = chatComponent.getFormattedText();

        int fakeChatTop = height - 200;

        drawRect(width / 3 - fontRendererObj.getStringWidth(fakeChatText) / 2, fakeChatTop, width / 3 +
            fontRendererObj.getStringWidth(fakeChatText) / 2, fakeChatTop + 10, Integer.MIN_VALUE);
        drawScaledText(fakeChatText, width / 3, fakeChatTop + 1, 1.0, -1, true, true);

        if (!levelheadPurchaseStates.isChat()) {
            String text = "Levelhead chat display not purchased!\nPlease purchase to activate and configure.";

            int i = 2;
            for (String s : text.split("\n")) {
                int offset = i * 10;
                int stringWidth = fontRendererObj.getStringWidth(s);
                int tmpLeft = width / 3 - stringWidth / 2;
                fakeChatTop = height - 180;
                drawRect(tmpLeft, fakeChatTop + offset, tmpLeft + stringWidth, fakeChatTop + offset + 8, Integer.MIN_VALUE);
                fontRendererObj.drawString(s, tmpLeft, fakeChatTop + offset, Color.YELLOW.getRGB(), true);
                i++;
            }

            reg(new GuiButton(++currentID, width / 3 - 60, height - 140, 120, 20, YELLOW + "Purchase Chat Display"), button ->
                attemptPurchase("chat"));
        } else {
            reg(new GuiButton(++currentID, width - editWidth - 1, 71, editWidth, 20, "Rotate Bracket Color"), button ->
                incrementColor(config, true));
            reg(new GuiButton(++currentID, width - editWidth - 1, 92, editWidth, 20, "Rotate Value Color"), button ->
                incrementColor(config, false));
        }
    }

    private void doTab(Levelhead instance) {
        EntityPlayerSP player = mc.thePlayer;
        NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(player.getUniqueID());
        LevelheadDisplay tab = instance.getDisplayManager().getTab();

        if (tab != null) {
            String formattedText = player.getDisplayName().getFormattedText();
            int totalTabWidth = 9 + fontRendererObj.getStringWidth(formattedText) + getLevelheadWidth(playerInfo) + 15;
            int fakeTabTop = height - 200;
            int centerOn = width / 3;
            int leftStart = centerOn - totalTabWidth / 2;
            drawRect(leftStart, fakeTabTop, centerOn + totalTabWidth / 2, fakeTabTop + 8, Integer.MIN_VALUE);
            drawRect(leftStart, fakeTabTop, centerOn + totalTabWidth / 2, fakeTabTop + 8, 553648127);
            fontRendererObj.drawString(formattedText, leftStart + 9, fakeTabTop, -1, true);

            mc.getTextureManager().bindTexture(player.getLocationSkin());
            int l2 = 8;
            int i3 = 8;
            Gui.drawScaledCustomSizeModalRect(leftStart, fakeTabTop, 8.0F, (float) l2, 8, i3, 8, 8, 64.0F, 64.0F);

            if (player.isWearing(EnumPlayerModelParts.HAT)) {
                int j3 = 8;
                int k3 = 8;
                Gui.drawScaledCustomSizeModalRect(leftStart, fakeTabTop, 40.0F, (float) j3, 8, k3, 8, 8, 64.0F, 64.0F);
            }

            drawPing(leftStart + totalTabWidth, fakeTabTop, playerInfo);
            drawPingHook(0, leftStart + totalTabWidth, fakeTabTop, playerInfo);
            LevelheadPurchaseStates purchaseStates = instance.getLevelheadPurchaseStates();

            if (!purchaseStates.isTab()) {
                String text = "Levelhead tab display not purchased!\nPlease purchase to activate and configure.";
                int i = 1;

                for (String s : text.split("\n")) {
                    int offset = i * 10;
                    int stringWidth = fontRendererObj.getStringWidth(s);
                    int tmpLeft = width / 3 - stringWidth / 2;
                    fakeTabTop = height - 180;
                    drawRect(tmpLeft, fakeTabTop + offset, tmpLeft + stringWidth, fakeTabTop + offset + 8, Integer.MIN_VALUE);
                    fontRendererObj.drawString(s, tmpLeft, fakeTabTop + offset, Color.YELLOW.getRGB(), true);
                    i++;
                }

                reg(new GuiButton(++currentID, centerOn - 60, height - 140, 120, 20, YELLOW + "Purchase Tab Display"),
                    button -> attemptPurchase("tab"));
            }
        }
    }

    private void drawPing(int x, int y, NetworkPlayerInfo playerInfo) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(icons);
        int i = 0;
        int j;

        if (playerInfo.getResponseTime() < 0) {
            j = 5;
        } else if (playerInfo.getResponseTime() < 300) {
            j = 1;
        } else if (playerInfo.getResponseTime() < 600) {
            j = 2;
        } else if (playerInfo.getResponseTime() < 1000) {
            j = 3;
        } else {
            j = 4;
        }

        zLevel += 100.0F;
        drawTexturedModalRect(x - 11, y, i * 10, 176 + j * 8, 10, 8);
        zLevel -= 100.0F;
    }

    public static void drawPingHook(int i, int x, int y, NetworkPlayerInfo playerInfo) {
        if (!Levelhead.getInstance().getDisplayManager().getMasterConfig().isEnabled()) {
            return;
        }

        Levelhead instance = Levelhead.getInstance();
        LevelheadDisplay tab = instance.getDisplayManager().getTab();
        if (tab != null) {
            if (!tab.getConfig().isEnabled()) return;
            if (instance.getLevelheadPurchaseStates().isTab()) {
                String s = tab.getTrueValueCache().get(playerInfo.getGameProfile().getId());
                if (s != null) {
                    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
                    int x1 = i + x - 12 - fontRenderer.getStringWidth(s);

                    Scoreboard board = Minecraft.getMinecraft().theWorld.getScoreboard();
                    ScoreObjective objective = board.getObjectiveInDisplaySlot(0);

                    if (objective != null) {
                        int score = board.getValueFromObjective(playerInfo.getGameProfile().getName(), objective).getScorePoints();
                        int extraWidth = fontRenderer.getStringWidth(" " + score);

                        x1 -= extraWidth;
                    }

                    DisplayConfig config = tab.getConfig();
                    if (config.isFooterChroma()) {
                        fontRenderer.drawString(s, x1, y, instance.getRGBColor());
                    } else if (config.isFooterRgb()) {
                        fontRenderer.drawString(s, x1, y, new Color(config.getFooterRed(), config.getFooterGreen(), config.getFooterBlue()).getRGB());
                    } else {
                        fontRenderer.drawString(config.getFooterColor() + s, x1, y, -1);
                    }
                }
            }
        }
    }

    public static int getLevelheadWidth(NetworkPlayerInfo playerInfo) {
        if (!Levelhead.getInstance().getDisplayManager().getMasterConfig().isEnabled()) {
            return 0;
        }

        Levelhead instance = Levelhead.getInstance();
        LevelheadDisplay tab = instance.getDisplayManager().getTab();

        if (tab != null) {
            if (!tab.getConfig().isEnabled()) return 0;

            if (instance.getLevelheadPurchaseStates().isTab()) {
                String s = tab.getTrueValueCache().get(playerInfo.getGameProfile().getId());
                if (s != null) return Minecraft.getMinecraft().fontRendererObj.getStringWidth(s) + 2;
            }
        }

        return 0;
    }

    private String removeColorChar(String message) {
        return message.replace(COLOR_CHAR, "");
    }

    private String getMode(boolean header) {
        DisplayConfig config = currentlyBeingEdited.getConfig();

        return header ? config.isHeaderChroma() ? "Chroma" : config.isHeaderRgb() ? "RGB" : "Classic" :
            config.isFooterChroma() ? "Chroma" : config.isFooterChroma() ? "RGB" : "Classic";
    }

    private void updatePeopleToValues() {
        UUID uuid = mc.thePlayer.getUniqueID();
        currentlyBeingEdited.getCache().remove(uuid);
        currentlyBeingEdited.getTrueValueCache().remove(uuid);
        bigChange = true;
    }

    private void attemptPurchase(String key) {
        Levelhead instance = Levelhead.getInstance();
        LevelheadJsonHolder paidData = instance.getPaidData();
        Hyperium.LOGGER.debug(paidData);
        LevelheadJsonHolder extraDisplays = paidData.optJsonObject("extra_displays");
        LevelheadJsonHolder stats = paidData.optJsonObject("stats");
        boolean found;
        boolean display = false;
        String name = null;
        String description = null;
        int cost = 0;
        LevelheadJsonHolder seed = null;
        boolean single = false;

        if (extraDisplays.has(key)) {
            seed = extraDisplays.optJsonObject(key);
        } else if (stats.has(key)) {
            seed = stats.optJsonObject(key);
        }

        found = seed != null;
        if (seed != null) {
            name = seed.optString("name");
            description = seed.optString("description");
            cost = seed.optInt("cost");
            single = seed.optBoolean("single");
        }

        int remainingLevelheadCredits = instance.getRawPurchases().optInt("remaining_levelhead_credits");
        if (remainingLevelheadCredits < cost) {
            mc.displayGuiScreen(null);
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Insufficient Levelhead credits! " + name + " costs " + cost + " credits but you only have "
                + remainingLevelheadCredits);
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("You can purchase more Levelhead credits here: https://purchase.sk1er.club/category/1050972");
            return;
        }

        if (instance.getAuth().isFailed()) {
            mc.displayGuiScreen(null);
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Could not verify your identity. Please restart your client. If the issue persists, contact Sk1er.");
            return;
        }

        if (found) {
            String finalName = name;
            ids.put(key.hashCode(), () -> {
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Attempting to purchase: " + finalName);
                Multithreading.runAsync(() -> {
                    LevelheadJsonHolder jsonHolder = new LevelheadJsonHolder(
                        Sk1erMod.getInstance().rawWithAgent(
                            "https://api.sk1er.club/levelhead_purchase?access_token=" + instance.getAuth().getAccessKey() + "&request=" + key +
                                "&hash=" + instance.getAuth().getHash()));
                    if (jsonHolder.optBoolean("success")) {
                        instance.refreshPurchaseStates();
                        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Successfully purchased: " + finalName);
                    } else {
                        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Failed to purchase: " + finalName + ". Cause: " + jsonHolder.optString("cause"));
                    }
                });
            });

            GuiYesNo gui = new GuiYesNo(this, "Purchase " + finalName, "Description:" +
                " " + description + ". This item may be purchased " + (single ? "one time" : "many times") + ". Type: " + (display ? "Display" : "Extra Stat"),
                "Purchase for "
                + cost + " credits", "Cancel", key.hashCode());
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(gui);
        } else {
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Could not find package: " + key + ". Please contact Sk1er immediately.");
        }
    }

    private void regSlider(GuiSlider slider) {
        reg(slider, null);
    }

    private void reg(GuiButton button, Consumer<GuiButton> consumer) {
        buttonList.removeIf(button1 -> button1.id == button.id);
        clicks.keySet().removeIf(button1 -> button1.id == button.id);
        buttonList.add(button);
        if (consumer != null) clicks.put(button, consumer);
    }

    private void drawScaledText(String text, int trueX, int trueY, double scaleFac, int color, boolean shadow, boolean centered) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scaleFac, scaleFac, scaleFac);
        fontRendererObj.drawString(text, (float) (((double) trueX) / scaleFac) - (centered ? fontRendererObj.getStringWidth(text) / 2F : 0),
            (float) (((double) trueY) / scaleFac), color, shadow);
        GlStateManager.scale(1 / scaleFac, 1 / scaleFac, 1 / scaleFac);
        GlStateManager.popMatrix();
    }

    private void updateFooterColorState(DisplayConfig config) {
        if (config.isFooterRgb()) {
            config.setFooterRgb(false);
            config.setFooterChroma(true);
        } else if (config.isFooterChroma()) {
            config.setFooterRgb(false);
            config.setFooterChroma(false);
        } else {
            config.setFooterRgb(true);
            config.setFooterChroma(false);
        }
    }
}
