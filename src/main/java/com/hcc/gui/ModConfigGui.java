/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.gui;

import com.hcc.HCC;
import com.hcc.gui.settings.SettingItem;
import com.hcc.gui.settings.items.GeneralSetting;
import com.hcc.utils.HCCFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModConfigGui extends HCCGui {
    private List<SettingItem> settingItems = new ArrayList<>();
    private int offset = 0;
    /**
     * current tab
     */
    private Tabs currentTab = Tabs.HOME;


    private HCCFontRenderer fontRenderer = new HCCFontRenderer("Arial", Font.PLAIN, 12);

    private HCCFontRenderer mainFontRenderer = new HCCFontRenderer("Times New Roman", Font.BOLD, 24);

    private GuiBlock guiblock;

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        //background
        drawRect(width / 5, height / 5, width - width / 5, height - height / 5, new Color(0, 0, 0, 100).getRGB());

        //TODO: Draw string for each tab
        switch (currentTab) {
            case HOME:
              /*  GL11.glPushMatrix();
                GL11.glScalef(3F, 3F, 3F);
                mainFontRenderer.drawString("\u00a7eH\u00A7fCC", (width / 5) /3, ((height / 5) + 25) /3, 0xffffff);
                GL11.glPopMatrix();*/
                break;
            case SETTINGS:
                drawSettingsItem(Minecraft.getMinecraft(), mouseX, mouseY);
                break;
            case ADDONS:
                break;
            case FRIENDS:
                break;
            case CHROMAHUD: {

                //Check is mostly to make sure bad things don't happen. The ChromaHUD system was made for when the player exists
                if (Minecraft.getMinecraft().thePlayer == null) {
                    drawCenteredString(fontRendererObj, "Please use this while in a world or on a server", width / 2, (height - height / 2) - 12, 0xFFFFFF);
                } else {
                    Minecraft.getMinecraft().displayGuiScreen(HCC.INSTANCE.getModIntegration().getChromaHUD().getConfigGuiInstance());
                }
                break;
            }
            case ABOUT:
                String str = "Developed by Sk1er, CoalOres, Kevin and Cubxity";
                fontRenderer.drawCenteredString(str, width / 2, height - 12, Color.WHITE.getRGB());
                break;
            default:
                break;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);

        // Tab highlight
        drawRect(getX(currentTab.getIndex()), getY() + 23, getX(currentTab.getIndex() + 1), getY() + 25, new Color(149, 201, 144).getRGB());
    }

    private void drawSettingsItem(Minecraft mc, int mouseX, int mouseY) {
        int items = (height - (getY() * 2 + 25)) / 15;

        settingItems.stream()
                .filter(i -> items - i.id >= offset && (getY() + 25) + (offset + i.id) * 15 >= getY() + 25)
                .forEach(i -> {
                    i.visible = true;
                    i.drawItem(mc, mouseX, mouseY, getX(0), (getY() + 25) + (offset + i.id) * 15);
                });
    }

    @Override
    protected void pack() {
        int numberOfTabs = 6;
        this.buttonList.add(Tabs.HOME.setButton(new CustomFontButton(0, getX(0), getY(), 50, 25, "HOME")));
        this.buttonList.add(Tabs.SETTINGS.setButton(new CustomFontButton(1, getX(1), getY(), 50, 25, "SETTINGS")));
        this.buttonList.add(Tabs.ADDONS.setButton(new CustomFontButton(2, getX(2), getY(), 50, 25, "ADDONS")));
        this.buttonList.add(Tabs.FRIENDS.setButton(new CustomFontButton(3, getX(3), getY(), 50, 25, "FRIENDS")));
        this.buttonList.add(Tabs.ABOUT.setButton(new CustomFontButton(4, getX(4), getY(), 50, 25, "ABOUT")));
        this.buttonList.add(Tabs.CHROMAHUD.setButton(new CustomFontButton(5, getX(5), getY(), 50, 25, "DISPLAY")));
        //  guiblock = new GuiBlock(getX(0), width - getX(0), getY(), height - getY());
        // TODO: Make it so if they have a retarded resolution it creates arrows for them to cycle between tabs

        // Add settings item
        settingItems = new ArrayList<>(); //Clear list
        settingItems.add(new SettingItem(0, width - getX(0) * 2, "GENERAL", i -> {
            Minecraft.getMinecraft().displayGuiScreen(new GeneralSetting(this));
        }));
        settingItems.add(new SettingItem(1, width - getX(0) * 2, "CAPTUREX", i -> {
            //TODO: Display the gui
        }));
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        for (Tabs t : Tabs.values())
            if (t.getIndex() == button.id)
                currentTab = t;
        updateTabs();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i < 0)
            offset += 1;
        else if (i > 0)
            offset -= 1;
    }

    private void updateTabs() {
        //TODO: Make all components invisible here
        settingItems.parallelStream().forEach(i -> i.visible = false);

        //TODO: Make components visible corresponding to tab
        switch (currentTab) {
            case HOME:
                break;
            case SETTINGS:
                break;
            case ADDONS:
                break;
            case FRIENDS:
                break;
            case ABOUT:
                break;
            case CHROMAHUD:
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        settingItems.stream()
                .filter(i -> i.mousePressed(this.mc, mouseX, mouseY))
                .forEach(i -> {
                    i.playPressSound(mc.getSoundHandler());
                    i.callback.accept(i.id);
                });
    }

    private int getX(int n) {
        return (width / 5) + (50 * n);
    }

    private int getY() {
        return height / 5;
    }

    private enum Tabs {
        HOME(null, 0),
        SETTINGS(null, 1),
        ADDONS(null, 2),
        FRIENDS(null, 3),
        ABOUT(null, 4),
        CHROMAHUD(null, 5);

        private GuiButton button;
        private int index;

        Tabs(GuiButton button, int index) {
            this.button = button;
            this.index = index;
        }

        public GuiButton setButton(GuiButton button) {
            this.button = button;
            return this.button;
        }

        public GuiButton getButton() {
            return button;
        }

        public int getIndex() {
            return index;
        }
    }
}
