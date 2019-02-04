/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.levelhead.guis;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.CustomLevelheadConfigurer;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.levelhead.config.LevelheadConfig;
import cc.hyperium.mods.levelhead.renderer.LevelheadComponent;
import cc.hyperium.mods.levelhead.renderer.LevelheadTag;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Keyboard;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * @author Sk1er
 * <p>
 * Modified by boomboompower on 14/6/2017
 */
public class LevelHeadGui extends GuiScreen {


    private final String ENABLED = ChatColor.GREEN + "Enabled";
    private final String DISABLED = ChatColor.RED + "Disabled";
    private final String COLOR_CHAR = String.valueOf("\u00a7");
    private final String colors = "0123456789abcdef";
    private final List<GuiButton> sliders = new ArrayList<>();
    private final Map<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private final Minecraft mc;
    private final ReentrantLock lock = new ReentrantLock();
    private final Levelhead mod;
    private GuiBlock customBlock = new GuiBlock(0, 0, 0, 0);
    private GuiButton headerColorButton;
    private GuiButton footerColorButton;
    private GuiButton prefixButton;
    private boolean isCustom = false;
    private GuiTextField textField;
    private GuiButton buttonType;

    private int calculateHeight(int row) {
        return 55 + row * 23;
    }

    public LevelHeadGui(Levelhead modIn) {
        this.mod = modIn;
        this.mc = Minecraft.getMinecraft();
    }

    private void reg(GuiButton button, Consumer<GuiButton> consumer) {
        this.buttonList.add(button);
        this.clicks.put(button, consumer);
    }

    @Override
    public void initGui() {
        Multithreading.runAsync(() -> {
            String raw = Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/levelhead/" + Minecraft.getMinecraft().getSession().getProfile().getId().toString().replace("-", ""));
            this.isCustom = new JsonHolder(raw).optBoolean("custom");
            updateCustom();
        });
        Keyboard.enableRepeatEvents(true);

        Levelhead instance = Hyperium.INSTANCE.getModIntegration().getLevelhead();
        LevelheadConfig config = instance.getConfig();
        reg(new GuiButton(1, this.width / 2 - 155, calculateHeight(0), 150, 20, "LevelHead: " + getLevelToggle()), button -> {
            config.setEnabled(!config.isEnabled());
            button.displayString = "LevelHead: " + getLevelToggle();
            sendChatMessage(String.format("Toggled %s!", (config.isEnabled() ? "On" : "Off")));
        });
        reg(new GuiButton(69, this.width / 2 + 5, calculateHeight(0), 150, 20, "Show self: " + (config.isShowSelf() ? ChatColor.GREEN + "On" : ChatColor.RED + "Off")), button -> {
            config.setShowSelf(!config.isShowSelf());
            button.displayString = "Show self: " + (config.isShowSelf() ? ChatColor.GREEN + "On" : ChatColor.RED + "Off");
        });
        //RGB -> Chroma
        //Chroma -> Classic
        //Classic -> RGB
        reg(new GuiButton(2, this.width / 2 - 155, calculateHeight(4), 150, 20, "Header Mode: " + getMode(true)), button -> {
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
            button.displayString = "Header Mode: " + getMode(true);
        });

        reg(new GuiButton(3, this.width / 2 + 5, calculateHeight(4), 150, 20, "Footer Mode: " + getMode(false)), button -> {
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
            button.displayString = "Header Mode: " + getMode(false);
        });


        reg(this.prefixButton = new GuiButton(6, this.width / 2 + 5, calculateHeight(1), 150, 20, "Set Prefix"), button -> changePrefix());

        this.textField = new GuiTextField(0, mc.fontRendererObj, this.width / 2 - 154, calculateHeight(1), 148, 20);

        //Color rotate
        reg(this.headerColorButton = new GuiButton(4, this.width / 2 - 155, calculateHeight(5), 150, 20, "Rotate Color"), button -> {
            int primaryId = colors.indexOf(removeColorChar(config.getHeaderColor()));
            if (++primaryId == colors.length()) {
                primaryId = 0;
            }
            config.setHeaderColor(COLOR_CHAR + colors.charAt(primaryId));
        });
        reg(this.footerColorButton = new GuiButton(5, this.width / 2 + 5, calculateHeight(5), 150, 20, "Rotate Color"), button -> {
            int primaryId = colors.indexOf(removeColorChar(config.getFooterColor()));
            if (++primaryId == colors.length()) {
                primaryId = 0;
            }
            config.setFooterColor(COLOR_CHAR + colors.charAt(primaryId));
        });
        reg(new GuiSlider(13, this.width / 2 - 155, calculateHeight(2), 150, 20, "Display Distance: ", "", 5, 64, config.getRenderDistance(), false, true, slider -> {
            config.setRenderDistance(slider.getValueInt());
            slider.dragging = false;
        }), null);

        reg(new GuiSlider(14, this.width / 2 + 5, calculateHeight(2), 150, 20, "Cache size: ", "", 150, 5000, config.getPurgeSize(), false, true, slider -> {
            config.setPurgeSize(slider.getValueInt());
            slider.dragging = false;
        }), null);


        JsonHolder types = instance.getTypes();
        reg(this.buttonType = new GuiButton(4, this.width / 2 - 155, calculateHeight(3), 150 * 2 + 10, 20, "Current Type: " + types.optJSONObject(instance.getType()).optString("name")), button -> {
            String currentType = instance.getType();
            List<String> keys = types.getKeys();
            int i = keys.indexOf(currentType);
            i++;
            if (i >= keys.size()) {
                i = 0;
            }
            if (config.getCustomHeader().equalsIgnoreCase(types.optJSONObject(currentType).optString("name"))) {
                config.setCustomHeader(types.optJSONObject(keys.get(i)).optString("name"));
            }
            instance.setType(keys.get(i));
            button.displayString = "Current Type: " + types.optJSONObject(instance.getType()).optString("name");
            Hyperium.INSTANCE.getModIntegration().getLevelhead().levelCache.clear();
        });

        //public GuiSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, ISlider par)
        regSlider(new GuiSlider(6, this.width / 2 - 155, calculateHeight(5), 150, 20, "Header Red: ", "", 0, 255, config.getHeaderRed(), false, true, slider -> {
            config.setHeaderRed(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(7, this.width / 2 - 155, calculateHeight(6), 150, 20, "Header Green: ", "", 0, 255, config.getHeaderGreen(), false, true, slider -> {
            config.setHeaderGreen(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(8, this.width / 2 - 155, calculateHeight(7), 150, 20, "Header Blue: ", "", 0, 255, config.getHeaderBlue(), false, true, slider -> {
            config.setHeaderBlue(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);


        regSlider(new GuiSlider(10, this.width / 2 + 5, calculateHeight(5), 150, 20, "Footer Red: ", "", 0, 255, config.getFooterRed(), false, true, slider -> {
            config.setFooterRed(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(11, this.width / 2 + 5, calculateHeight(6), 150, 20, "Footer Green: ", "", 0, 255, config.getFooterGreen(), false, true, slider -> {
            config.setFooterGreen(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(12, this.width / 2 + 5, calculateHeight(7), 150, 20, "Footer Blue: ", "", 0, 255, config.getFooterBlue(), false, true, slider -> {
            config.setFooterBlue(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);

    }

    private void updateCustom() {
        lock.lock();
        reg(new GuiButton(13, this.width / 2 - 155, this.height - 44, 310, 20, (isCustom ? ChatColor.YELLOW + "Click to change custom Levelhead." : ChatColor.YELLOW + "Click to purchase a custom Levelhead message")), button -> {

            try {
                if (isCustom) {
                    Desktop.getDesktop().browse(new URI("https://sk1er.club/user"));
                } else {
                    Desktop.getDesktop().browse(new URI("https://sk1er.club/customlevelhead"));
                }
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }

        });
        if (isCustom) {
            GuiButton button1 = new GuiButton(16, this.width / 2 - 155, this.height - 22, 310, 20, ChatColor.YELLOW + "Export these colors to my custom Levelhead");
            reg(button1, button -> {
                JsonHolder object = new JsonHolder();
                object.put("header_obj", Hyperium.INSTANCE.getModIntegration().getLevelhead().getHeaderConfig());
                object.put("footer_obj", Hyperium.INSTANCE.getModIntegration().getLevelhead().getFooterConfig());
                try {
                    String encode = URLEncoder.encode(object.toString(), "UTF-8");
                    String url = "https://sk1er.club/user?levelhead_color=" + encode;
                    ChatComponentText text = new ChatComponentText("Click here to update your custom Levelhead colors");
                    ChatStyle style = new ChatStyle();
                    style.setBold(true);
                    style.setColor(EnumChatFormatting.YELLOW);
                    style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                    ChatComponentText valueIn = new ChatComponentText("Please be logged in to your Sk1er.club for this to work. Do /levelhead dumpcache after clicking to see new colors!");
                    ChatStyle style1 = new ChatStyle();
                    style1.setColor(EnumChatFormatting.RED);
                    valueIn.setChatStyle(style1);
                    style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, valueIn));
                    text.setChatStyle(style);
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                mc.displayGuiScreen(null);
            });
        }
        lock.unlock();
    }

    private void regSlider(GuiSlider slider, Consumer<GuiButton> but) {
        slider.yPosition += 30;
        reg(slider, but);
        sliders.add(slider);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float ticks) {
        lock.lock();
        drawDefaultBackground();

        drawTitle();
        drawLook();
        textField.drawTextBox();

        headerColorButton.visible = !this.mod.getConfig().isHeaderChroma() && !this.mod.getConfig().isHeaderRgb();
        footerColorButton.visible = !this.mod.getConfig().isFooterChroma() && !this.mod.getConfig().isFooterRgb();
        prefixButton.enabled = !textField.getText().isEmpty();
        if (this.mod.getConfig().isHeaderRgb()) {
            for (GuiButton slider : sliders) {
                if (slider.displayString.contains("Header"))
                    slider.visible = true;
            }
        } else {
            for (GuiButton slider : sliders) {
                if (slider.displayString.contains("Header"))
                    slider.visible = false;
            }
        }
        if (this.mod.getConfig().isFooterRgb()) {
            for (GuiButton slider : sliders) {
                if (slider.displayString.contains("Footer"))
                    slider.visible = true;

            }
        } else {
            for (GuiButton slider : sliders) {
                if (slider.displayString.contains("Footer"))
                    slider.visible = false;
            }
        }

        for (GuiButton aButtonList : this.buttonList) {
            aButtonList.drawButton(this.mc, mouseX, mouseY);
        }
        lock.unlock();
    }

    private String getMode(boolean header) {
        LevelheadConfig config = this.mod.getConfig();
        if (header) {
            return config.isHeaderChroma() ? "Chroma" : config.isHeaderRgb() ? "RGB" : "Classic";
        } else {
            return config.isFooterChroma() ? "Chroma" : config.isFooterRgb() ? "RGB" : "Classic";
        }
    }

    public void updatePeopleToValues() {
        Levelhead levelhead = Hyperium.INSTANCE.getModIntegration().getLevelhead();
        levelhead.levelCache.forEach((uuid, levelheadTag) -> {
            String value = levelhead.getTrueValueCache().get(uuid);
            if (value == null)
                return;
            JsonHolder footer = new JsonHolder().put("level", NumberUtils.isNumber(value) ? Long.parseLong(value) : -1).put("strlevel", value);
            LevelheadTag tag = levelhead.buildTag(footer, uuid);
            levelheadTag.reApply(tag);
        });
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null) {
            guiButtonConsumer.accept(button);
            // Adjust loaded levelhead names
            updatePeopleToValues();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            mc.displayGuiScreen(null);
        } else if (textField.isFocused() && keyCode == 28) {
            changePrefix();
        } else {
            if (Character.isLetterOrDigit(typedChar) || isCtrlKeyDown() || keyCode == 14) {
                textField.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        textField.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && customBlock.isMouseOver(mouseX, mouseY)) {
            new CustomLevelheadConfigurer().show();
            return;
        }

        if (mouseButton == 0) {
            for (GuiButton guibutton : this.buttonList) {
                if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
                    guibutton.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(guibutton);
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void display() {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(this);
    }

    @Override
    public void sendChatMessage(String msg) {
        GeneralChatHandler.instance().sendMessage(msg);
    }

    private void changePrefix() {
        if (!textField.getText().isEmpty()) {
            this.mod.getConfig().setCustomHeader(textField.getText());
            this.mod.levelCache.clear();
            sendChatMessage(String.format("LevelHead prefix is now %s!", ChatColor.GOLD + textField.getText() + ChatColor.YELLOW));
        } else {
            sendChatMessage("No prefix supplied!");
        }
        mc.displayGuiScreen(null);
    }

    private void drawTitle() {
        String text = "Sk1er LevelHead v5.0";

        drawCenteredString(mc.fontRendererObj, text, this.width / 2, 5, Color.WHITE.getRGB());
        drawHorizontalLine(this.width / 2 - mc.fontRendererObj.getStringWidth(text) / 2 - 5, this.width / 2 + mc.fontRendererObj.getStringWidth(text) / 2 + 5, 15, Color.WHITE.getRGB());
        drawCenteredString(mc.fontRendererObj, ChatColor.YELLOW + "Custom Levelhead Status: " + (isCustom ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled / Inactive"), this.width / 2,
            20, Color.WHITE.getRGB());

    }

    private void drawLook() {
        FontRenderer renderer = mc.fontRendererObj;
        if (Hyperium.INSTANCE.getModIntegration().getLevelhead().getConfig().isEnabled()) {
            drawCenteredString(renderer, "This is how levels will display", this.width / 2, 30, Color.WHITE.getRGB());
            LevelheadTag levelheadTag = Hyperium.INSTANCE.getModIntegration().getLevelhead().buildTag(new JsonHolder(), null);
            LevelheadComponent header = levelheadTag.getHeader();
            int h = 40;
            if (header.isChroma())
                drawCenteredString(renderer, header.getValue(), this.width / 2, h, Hyperium.INSTANCE.getModIntegration().getLevelhead().getRGBColor());
            else if (header.isRgb()) {
                drawCenteredString(renderer, header.getValue(), this.width / 2, h, new Color(header.getRed(), header.getGreen(), header.getBlue(), header.getAlpha()).getRGB());

            } else {
                drawCenteredString(renderer, header.getColor() + header.getValue(), this.width / 2, h, Color.WHITE.getRGB());
            }

            LevelheadComponent footer = levelheadTag.getFooter();
            footer.setValue("5");
            if (footer.isChroma())
                drawCenteredString(renderer, footer.getValue(), (this.width / 2 + renderer.getStringWidth(header.getValue()) / 2 + 3), h, Hyperium.INSTANCE.getModIntegration().getLevelhead().getRGBColor());
            else if (footer.isRgb()) {
                drawCenteredString(renderer, footer.getValue(), (this.width / 2 + renderer.getStringWidth(header.getValue()) / 2 + 3), h, new Color(footer.getRed(), footer.getBlue(), footer.getGreen(), footer.getAlpha()).getRGB());
            } else {
                drawCenteredString(renderer, footer.getColor() + footer.getValue(), (this.width / 2 + renderer.getStringWidth(header.getValue()) / 2 + 3), h, Color.WHITE.getRGB());
            }

        } else {
            drawCenteredString(renderer, "LevelHead is disabled", this.width / 2, 30, Color.WHITE.getRGB());
            drawCenteredString(renderer, "Player level\'s will not appear", this.width / 2, 40, Color.WHITE.getRGB());
        }
    }

    private String getLevelToggle() {
        return this.mod.getConfig().isEnabled() ? ENABLED : DISABLED;
    }

    private String removeColorChar(String message) {
        return message.replace(COLOR_CHAR, "");
    }
}
