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

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.gui.CustomLevelheadConfigurer;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.levelhead.config.LevelheadConfig;
import cc.hyperium.mods.levelhead.renderer.LevelheadComponent;
import cc.hyperium.mods.levelhead.renderer.LevelheadTag;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    // Recommended GUI display Values (this.height / 2 - 100 (sign) value)
    //        - 58
    //        - 34
    //        - 10
    //        + 14
    //        + 38
    //        + 62

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
    private Levelhead levelHead;

    public LevelHeadGui(Levelhead modIn) {
        this.mod = modIn;
        this.mc = Minecraft.getMinecraft();
    }

    private void reg(GuiButton button, Consumer<GuiButton> consumer) {
        button.yPosition += 30;
        this.buttonList.add(button);
        this.clicks.put(button, consumer);
    }

    @Override
    public void initGui() {
        if (Sk1erMod.getInstance().getApIKey() != null && Minecraft.getMinecraft().getSession().getProfile().getId() != null) {
            Multithreading.runAsync(() -> {
                String raw = Sk1erMod.getInstance().rawWithAgent("http://sk1er.club/modquery/" + Sk1erMod.getInstance().getApIKey() + "/levelhead/" + Minecraft.getMinecraft().getSession().getProfile().getId().toString().replace("-", ""));
                this.isCustom = new JsonHolder(raw).optBoolean("custom");
                updateCustom();
            });
        }

        Keyboard.enableRepeatEvents(true);

        reg(new GuiButton(1, this.width / 2 - 155, 60, 150, 20, "LevelHead: " + getLevelToggle()), button -> {
            this.mod.getConfig().setEnabled(!this.mod.getConfig().isEnabled());
            button.displayString = "LevelHead: " + getLevelToggle();
            sendChatMessage(String.format("Toggled %s!", (this.mod.getConfig().isEnabled() ? "on" : "off")));
        });
        reg(new GuiButton(69, this.width / 2 + 5, 60, 150, 20, "Show self: " + (this.mod.getConfig().isShowSelf() ? ChatColor.GREEN + "on" : ChatColor.RED + "off")), button -> {
            this.mod.getConfig().setShowSelf(!this.mod.getConfig().isShowSelf());
            button.displayString = "Show self: " + (this.mod.getConfig().isShowSelf() ? ChatColor.GREEN + "on" : ChatColor.RED + "off");
        });
        //RGB -> Chroma
        //Chroma -> Classic
        //Classic -> RGB
        reg(new GuiButton(2, this.width / 2 - 155, 85, 150, 20, "Header Mode: " + getMode(true)), button -> {
            if (this.mod.getConfig().isHeaderRgb()) {
                this.mod.getConfig().setHeaderRgb(false);
                this.mod.getConfig().setHeaderChroma(true);
            } else if (this.mod.getConfig().isHeaderChroma()) {
                this.mod.getConfig().setHeaderRgb(false);
                this.mod.getConfig().setHeaderChroma(false);
            } else {
                this.mod.getConfig().setHeaderRgb(true);
                this.mod.getConfig().setHeaderChroma(false);
            }
            button.displayString = "Header Mode: " + getMode(true);
        });

        reg(new GuiButton(3, this.width / 2 + 5, 85, 150, 20, "Footer Mode: " + getMode(false)), button -> {

            if (this.mod.getConfig().isFooterRgb()) {
                this.mod.getConfig().setFooterRgb(false);
                this.mod.getConfig().setFooterChroma(true);
            } else if (this.mod.getConfig().isFooterChroma()) {
                this.mod.getConfig().setFooterRgb(false);
                this.mod.getConfig().setFooterChroma(false);
            } else {
                this.mod.getConfig().setFooterRgb(true);
                this.mod.getConfig().setFooterChroma(false);
            }
            button.displayString = "Header Mode: " + getMode(false);
        });


        reg(this.prefixButton = new GuiButton(6, this.width / 2 + 5, 35, 150, 20, "Set Prefix"), button -> changePrefix());

        this.textField = new GuiTextField(0, mc.fontRendererObj, this.width / 2 - 155, 65, 150, 20);

        //Color rotate
        reg(this.headerColorButton = new GuiButton(4, this.width / 2 - 155, 110, 150, 20, "Rotate Color"), button -> {
            int primaryId = colors.indexOf(removeColorChar(this.mod.getConfig().getHeaderColor()));
            if (++primaryId == colors.length()) {
                primaryId = 0;
            }
            this.mod.getConfig().setHeaderColor(COLOR_CHAR + colors.charAt(primaryId));
        });
        reg(this.footerColorButton = new GuiButton(5, this.width / 2 + 5, 110, 150, 20, "Rotate Color"), button -> {
            int primaryId = colors.indexOf(removeColorChar(this.mod.getConfig().getFooterColor()));
            if (++primaryId == colors.length()) {
                primaryId = 0;
            }
            this.mod.getConfig().setFooterColor(COLOR_CHAR + colors.charAt(primaryId));
        });
        reg(new GuiSlider(13, this.width / 2 - 155, 130, 150, 20, "Display Distance: ", "", 5, 64, this.mod.getConfig().getRenderDistance(), false, true, slider -> {
            this.mod.getConfig().setRenderDistance(slider.getValueInt());
            slider.dragging = false;
        }), null);

        reg(new GuiSlider(14, this.width / 2 + 5, 130, 150, 20, "Cache size: ", "", 150, 5000, this.mod.getConfig().getPurgeSize(), false, true, slider -> {
            this.mod.getConfig().setPurgeSize(slider.getValueInt());
            slider.dragging = false;
        }), null);

        regSlider(new GuiSlider(6, this.width / 2 - 155, 125, 150, 20, "Header Red: ", "", 0, 255, this.mod.getConfig().getHeaderRed(), false, true, slider -> {
            this.mod.getConfig().setHeaderRed(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(7, this.width / 2 - 155, 150, 150, 20, "Header Green: ", "", 0, 255, this.mod.getConfig().getHeaderGreen(), false, true, slider -> {
            this.mod.getConfig().setHeaderGreen(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(8, this.width / 2 - 155, 175, 150, 20, "Header Blue: ", "", 0, 255, this.mod.getConfig().getHeaderBlue(), false, true, slider -> {
            this.mod.getConfig().setHeaderBlue(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);


        regSlider(new GuiSlider(10, this.width / 2 + 5, 125, 150, 20, "Footer Red: ", "", 0, 255, this.mod.getConfig().getFooterRed(), false, true, slider -> {
            this.mod.getConfig().setFooterRed(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(11, this.width / 2 + 5, 150, 150, 20, "Footer Green: ", "", 0, 255, this.mod.getConfig().getFooterGreen(), false, true, slider -> {
            this.mod.getConfig().setFooterGreen(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(12, this.width / 2 + 5, 175, 150, 20, "Footer Blue: ", "", 0, 255, this.mod.getConfig().getFooterBlue(), false, true, slider -> {
            this.mod.getConfig().setFooterBlue(slider.getValueInt());
            updatePeopleToValues();
            slider.dragging = false;
        }), null);


    }

    private void updateCustom() {
        lock.lock();
        reg(new GuiButton(13, this.width / 2 - 155, 205, 310, 20, (isCustom ? ChatColor.YELLOW + "Click to change custom Levelhead." : ChatColor.YELLOW + "Click to purchase a custom Levelhead message")), button -> {

            try {
                if (isCustom) {
                    Desktop.getDesktop().browse(new URI("http://sk1er.club/user"));
                } else {
                    Desktop.getDesktop().browse(new URI("http://sk1er.club/customlevelhead"));
                }
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }

        });
        if (isCustom) {
            GuiButton button1 = new GuiButton(16, this.width / 2 - 155, 230, 310, 20, ChatColor.YELLOW + "Export these colors to my custom Levelhead");
            reg(button1, button -> {
                JsonHolder object = new JsonHolder();
                object.put("header_obj", this.mod.getHeaderConfig());
                object.put("footer_obj", this.mod.getFooterConfig());
                NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("levelhead_color", true).put("object", object)));
                GeneralChatHandler.instance().sendMessage("Exported settings!");
                Minecraft.getMinecraft().displayGuiScreen(null);
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
        drawTitle("Sk1er LevelHead");
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

    private void updatePeopleToValues() {
        this.mod.levelCache.forEach((uuid, levelheadTag) -> {
            if (levelheadTag == null)
                return;
            Integer value = this.mod.getTrueLevelCache().get(uuid);
            if (value == null)
                return;
            JsonHolder footer = new JsonHolder().put("level", value).put("strlevel", value + "");
            LevelheadTag tag = this.mod.buildTag(footer, uuid);
            if (tag != null)
                levelheadTag.reApply(tag);
            else {
                int primaryId = colors.indexOf(removeColorChar(this.mod.getConfig().getFooterColor()));
                if (++primaryId == colors.length()) {
                    primaryId = 0;
                }
                this.mod.getConfig().setFooterColor(COLOR_CHAR + colors.charAt(primaryId));
            }
        });
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null) {
            guiButtonConsumer.accept(button);
            //Adjust loaded levelhead names
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
//                    net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre event = new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre(this, guibutton, this.buttonList);
//                    if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
//                        break;
//                    guibutton = event.button;
                    guibutton.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(guibutton);
//                    if (this.equals(this.mc.currentScreen))
//                        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post(this, event.button, this.buttonList));
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void display() {
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onTick(TickEvent event) {
        EventBus.INSTANCE.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
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

    private void drawTitle(String text) {
        String text1 = ChatColor.YELLOW + "Custom Levelhead Status: " + (isCustom ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled / Inactive");
        drawCenteredString(mc.fontRendererObj, text1, this.width / 2,
                10, Color.WHITE.getRGB());
        int stringWidth = fontRendererObj.getStringWidth(text1);
        this.customBlock = new GuiBlock(width / 2 - stringWidth / 2, width / 2 + stringWidth / 2, 10, 20);
        drawCenteredString(mc.fontRendererObj, text, this.width / 2, 20, Color.WHITE.getRGB());
        drawHorizontalLine(this.width / 2 - mc.fontRendererObj.getStringWidth(text) / 2 - 5, this.width / 2 + mc.fontRendererObj.getStringWidth(text) / 2 + 5, 30, Color.WHITE.getRGB());
    }

    private void drawLook() {
        FontRenderer renderer = mc.fontRendererObj;
        if (this.mod.getConfig().isEnabled()) {
            drawCenteredString(renderer, "This is how levels will display", this.width / 2, 40, Color.WHITE.getRGB());
            LevelheadTag levelheadTag = this.mod.buildTag(new JsonHolder(), null);
            LevelheadComponent header = levelheadTag.getHeader();
            if (header.isChroma())
                drawCenteredString(renderer, header.getValue(), this.width / 2, 50, levelHead.getRGBColor());
            else if (header.isRgb()) {
//                GlStateManager.color(header.getRed(), header.getGreen(), header.getBlue(), header.getAlpha());
                drawCenteredString(renderer, header.getValue(), this.width / 2, 50, new Color(header.getRed(), header.getGreen(), header.getBlue(), header.getAlpha()).getRGB());

            } else {
                drawCenteredString(renderer, header.getColor() + header.getValue(), this.width / 2, 50, Color.WHITE.getRGB());
            }

            LevelheadComponent footer = levelheadTag.getFooter();
            footer.setValue("5");
            if (footer.isChroma())
                drawCenteredString(renderer, footer.getValue(), (this.width / 2 + renderer.getStringWidth(header.getValue()) / 2 + 3), 50, levelHead.getRGBColor());
            else if (footer.isRgb()) {
                drawCenteredString(renderer, footer.getValue(), (this.width / 2 + renderer.getStringWidth(header.getValue()) / 2 + 3), 50, new Color(footer.getRed(), footer.getBlue(), footer.getGreen(), footer.getAlpha()).getRGB());
            } else {
                drawCenteredString(renderer, footer.getColor() + footer.getValue(), (this.width / 2 + renderer.getStringWidth(header.getValue()) / 2 + 3), 50, Color.WHITE.getRGB());
            }


        } else {
            drawCenteredString(renderer, "LevelHead is disabled", this.width / 2, 40, Color.WHITE.getRGB());
            drawCenteredString(renderer, "Player level\'s will not appear", this.width / 2, 50, Color.WHITE.getRGB());
        }
    }

    private String getLevelToggle() {
        return this.mod.getConfig().isEnabled() ? ENABLED : DISABLED;
    }

    private String removeColorChar(String message) {
        return message.replace(COLOR_CHAR, "");
    }
}