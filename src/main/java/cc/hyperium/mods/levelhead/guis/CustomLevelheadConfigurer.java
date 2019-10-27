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
import cc.hyperium.mods.levelhead.util.LevelheadJsonHolder;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.function.Consumer;

public class CustomLevelheadConfigurer extends GuiScreen {

    private int idIteration;
    private GuiTextField header;
    private GuiTextField level;
    private LevelheadJsonHolder levelheadPropose = new LevelheadJsonHolder();
    private HashMap<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();

    @Override
    public void initGui() {
        super.initGui();

        header = new GuiTextField(nextId(), fontRendererObj, width / 2 - 205, 30, 200, 20);
        level = new GuiTextField(nextId(), fontRendererObj, width / 2 + 5, 30, 200, 20);
        header.setMaxStringLength(50);
        level.setMaxStringLength(50);

        Multithreading.runAsync(() -> {
            LevelheadJsonHolder jsonHolder = new LevelheadJsonHolder(Sk1erMod.getInstance().rawWithAgent("https://sk1er.club/newlevel/" + mc.getSession().getProfile().getId().toString().replace("-", "")));
            header.setText(jsonHolder.optString("header"));
            level.setText(jsonHolder.optString("true_footer"));
        });
        Multithreading.runAsync(() -> levelheadPropose = new LevelheadJsonHolder(Sk1erMod.getInstance().rawWithAgent("https://api.hyperium.cc/levelhead_propose" + mc.getSession().getProfile().getId().toString().replace("-", ""))));
        Multithreading.runAsync(() -> {
            LevelheadJsonHolder jsonHolder = new LevelheadJsonHolder(Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/levelheadv5/" + mc.getSession().getProfile().getId().toString().replace("-", "") + "/LEVEL"));
            if (!jsonHolder.has("uuid")) {
                if (mc.currentScreen instanceof CustomLevelheadConfigurer) {
                    mc.displayGuiScreen(null);
                    Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("You must purchase Custom Levelhead to use this!");
                }
            }
        });

        refresh();
        reg(new GuiButton(nextId(), width / 2 - 205, 55, 200, 20, "Reset to default"), button -> {
            Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/customlevelhead/reset?hash=" + Levelhead.getInstance().getAuth().getHash() + "&level=default&header=default");
            refresh();
        });
        reg(new GuiButton(nextId(), width / 2 + 5, 55, 200, 20, "Send for review"), button -> {
            Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/customlevelhead/propose?hash=" + Levelhead.getInstance().getAuth().getHash() + "&footer=" + URLEncoder.encode(level.getText()) + "&header=" + URLEncoder.encode(header.getText()));
            refresh();
        });
        reg(new GuiButton(nextId(), width / 2 - 50, 80, 100, 20, "Refresh"), button -> refresh());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        header.drawTextBox();
        level.drawTextBox();
        drawScaledText(ChatColor.UNDERLINE.toString() + ChatColor.BOLD + "Custom Levelhead Message Configurer", width / 2, 5, 2, 16777215, true, true);
        if (levelheadPropose.getKeys().size() == 0) {
            drawScaledText(ChatColor.RED + "Loading: Error", width / 2, 115, 1.25, Color.RED.getRGB(), true, true);
            return;
        }
        if (levelheadPropose.optBoolean("denied")) {
            drawScaledText(ChatColor.YELLOW + "Status: " + ChatColor.RED + "Denied", width / 2, 115, 1.25, 16777215, true, true);
            return;
        }
        if (levelheadPropose.optBoolean("enabled")) {
            int i = 115;
            drawScaledText(ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Accepted", width / 2, i - 5, 1.25, 16777215, true, true);
            drawScaledText(ChatColor.YELLOW + "Header: " + ChatColor.GRAY + levelheadPropose.optString("header"), width / 2, 125, 1.25, 16777215, true, true);
            drawScaledText(ChatColor.YELLOW + "Level: " + ChatColor.GRAY + levelheadPropose.optString("strlevel"), width / 2, 140, 1.25, 16777215, true, true);
        } else {
            int i = 115;
            drawScaledText(ChatColor.YELLOW + "Status: Pending", width / 2, i - 5, 1.25, 16777215, true, true);
            drawScaledText(ChatColor.YELLOW + "Header: " + ChatColor.GRAY + levelheadPropose.optString("header"), width / 2, 125, 1.25, 16777215, true, true);
            drawScaledText(ChatColor.YELLOW + "Level: " + ChatColor.GRAY + levelheadPropose.optString("strlevel"), width / 2, 140, 1.25, 16777215, true, true);
            drawScaledText(ChatColor.YELLOW + "It will be reviewed soon!", width / 2, 155, 1.25, 16777215, true, true);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        header.mouseClicked(mouseX, mouseY, mouseButton);
        level.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        header.textboxKeyTyped(typedChar, keyCode);
        level.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null) {
            guiButtonConsumer.accept(button);
        }
    }

    private void drawScaledText(String text, int trueX, int trueY, double scaleFac, int color, boolean shadow, boolean centered) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scaleFac, scaleFac, scaleFac);
        fontRendererObj.drawString(text, (float) (((double) trueX) / scaleFac) - (centered ? fontRendererObj.getStringWidth(text) / 2F : 0), (float) (((double) trueY) / scaleFac), color, shadow);
        GlStateManager.scale(1 / scaleFac, 1 / scaleFac, 1 / scaleFac);
        GlStateManager.popMatrix();
    }

    private void refresh() {
        Multithreading.runAsync(() -> {
            LevelheadJsonHolder jsonHolder = new LevelheadJsonHolder(Sk1erMod.getInstance().rawWithAgent("https://sk1er.club/newlevel/" + mc.getSession().getProfile().getId().toString().replace("-", "")));
            header.setText(jsonHolder.optString("header"));
            level.setText(jsonHolder.optString("true_footer"));
        });
        Multithreading.runAsync(() -> levelheadPropose = new LevelheadJsonHolder(Sk1erMod.getInstance().rawWithAgent("https://api.hyperium.cc/levelhead_propose" + mc.getSession().getProfile().getId().toString().replace("-", ""))));
    }

    private void reg(GuiButton button, Consumer<GuiButton> consumer) {
        buttonList.removeIf(button1 -> button1.id == button.id);
        clicks.keySet().removeIf(button1 -> button1.id == button.id);
        buttonList.add(button);
        if (consumer != null) {
            clicks.put(button, consumer);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public int nextId() {
        return (++idIteration);
    }
}
