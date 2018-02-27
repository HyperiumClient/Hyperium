/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.settings.items.AnimationSettings;
import cc.hyperium.gui.settings.items.CaptureXSetting;
import cc.hyperium.gui.settings.items.GeneralSetting;
import cc.hyperium.gui.settings.SettingItem;
import cc.hyperium.utils.HyperiumFontRenderer;
import javafx.scene.control.Tab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModConfigGui extends HyperiumGui {
    private List<SettingItem> settingItems = new ArrayList<>();
    private List<Tab> tabs = new ArrayList<>();
    private int offset = 0;
    private HyperiumFontRenderer fontRenderer = new HyperiumFontRenderer("Arial", Font.PLAIN, 12);
    private HyperiumFontRenderer mainFontRenderer = new HyperiumFontRenderer("Times New Roman", Font.BOLD, 24);
    private GuiBlock guiblock;

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int rightBoxEdge = width - width / 5 - 2;
        int bottomBoxEdge = height - height / 5;

        // background
        drawRect(
                width / 5,
                height / 5,
                rightBoxEdge,
                bottomBoxEdge,
                new Color(0, 0, 0, 100).getRGB()
        );

        // right side drop shadow
        drawRect(
                rightBoxEdge,
                height / 5 + 5,
                rightBoxEdge + 2,
                bottomBoxEdge + 2,
                new Color(0, 0, 0, 180).getRGB()
        );

        // left side drop shadow
        drawRect(
                width / 5 + 5,
                bottomBoxEdge,
                rightBoxEdge,
                bottomBoxEdge + 2,
                new Color(0, 0, 0, 180).getRGB()
        );

        for (Tab tab : tabs) {
            tab.draw(mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
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
        CustomFontButton button = new CustomFontButton(0, getX(0), getY(), getButtonWidth(), 25, "HOME");
        buttonList.add(button);
        this.tabs.add(new Tab(button, 0, this));

        button = new CustomFontButton(1, getX(1), getY(), getButtonWidth(), 25, "SETTINGS");
        buttonList.add(button);
        Tab tab = new ModConfigGui.Tab(button, 1, this) {
            @Override
            public void draw(int mouseX, int mouseY) {
                super.draw(mouseX, mouseY);

                drawSettingsItem(Minecraft.getMinecraft(), mouseX, mouseY);
            }
        };

        tab.addSetting(new SettingItem(
                0,  getX(0),
                getDefaultItemY(0),
                width - getX(0) * 2,
                "GENERAL",
                i -> Minecraft.getMinecraft().displayGuiScreen(new GeneralSetting(this))
        )).addSetting(new SettingItem(
                1,  getX(0),
                getDefaultItemY(1),
                width - getX(0)* 2,
                "ANIMATIONS",
                i -> Minecraft.getMinecraft().displayGuiScreen(new AnimationSettings(this))
        )).addSetting(new SettingItem(
                2, getX(0),
                getDefaultItemY(2),
                width - getX(0) * 2,
                "CAPTUREX",
                i -> Minecraft.getMinecraft().displayGuiScreen(new CaptureXSetting(this))
        ));

        if(Minecraft.getMinecraft().thePlayer!=null) {
            tab.addSetting(new SettingItem(
                    3, getX(0),
                    getDefaultItemY(3),
                    width - getX(0) * 2,
                    "CHROMAHUD",
                    i -> Minecraft.getMinecraft().displayGuiScreen(Hyperium.INSTANCE.getModIntegration().getChromaHUD().getConfigGuiInstance())
            ));
        }

        this.tabs.add(tab);

        button = new CustomFontButton(2, getX(2), getY(), getButtonWidth(), 25, "ADDONS");
        buttonList.add(button);
        this.tabs.add(new Tab(button, 2, this));

        button = new CustomFontButton(3, getX(3), getY(), getButtonWidth(), 25, "FRIENDS");
        buttonList.add(button);
        this.tabs.add(new Tab(button, 3, this));

        button = new CustomFontButton(4, getX(4), getY(), getButtonWidth(), 25, "ABOUT");
        buttonList.add(button);
        this.tabs.add(new ModConfigGui.Tab(button, 4, this) {
            @Override
            public void draw(int mouseX, int mouseY) {
                super.draw(mouseX, mouseY);

                if (this.selected) {
                    String str = "Developed by Sk1er, CoalOres, Kevin and Cubxity";
                    fontRenderer.drawCenteredString(str, width / 2, height - 12, Color.WHITE.getRGB());
                }
            }
        });
    }

    private int getDefaultItemY(int i) {
        return getY()+25 + i * 15;
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        for (Tab t : tabs) {
            t.setSelected(t.getIndex() == button.id);
        }
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

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton != 0) {
            return;
        }

        for (SettingItem i : settingItems) {
            if(i.mousePressed(mc, mouseX, mouseY)){
                i.playPressSound(mc.getSoundHandler());
                i.callback.accept(i);
            }
        }
    }

    private int getButtonWidth() {
        int boxWidth = (width / 5) * 3;

        return boxWidth / 5;
    }

    private int getX(int n) {
        return (width / 5) + (getButtonWidth() * n);
    }

    private int getY() {
        return height / 5;
    }

    private class Tab {
        private ModConfigGui owningGui;
        private CustomFontButton button;
        private int index;
        private boolean selected;
        private float selectPercent;
        private ArrayList<SettingItem> settings;

        public Tab(CustomFontButton button, int index, ModConfigGui owningGui) {
            this.button = button;
            this.index = index;
            this.owningGui = owningGui;
            this.settings = new ArrayList<>();
        }

        public void draw(int mouseX, int mouseY) {
            this.selectPercent = clamp(
                    easeOut(
                            this.selectPercent,
                            this.selected ? 1.0f : 0.0f,
                            0.01f,
                            this.selected ? 5f : 2f
                    ),
                    0.0f,
                    1.0f
            );

            int leftX = owningGui.getX(getIndex());
            int endX = owningGui.getX(getIndex() + 1);
            int beginY = owningGui.getY() + 23;
            int endY = owningGui.getY() + 25;

            int halfWidth = (endX - leftX) / 2;
            int finalWidth = MathHelper.floor_float(halfWidth * this.selectPercent);

            drawRect(
                    leftX,
                    owningGui.getY(),
                    endX,
                    endY,
                    new Color(0, 0, 0, 125).getRGB()
            );

            if (this.selectPercent > 0) {
                drawRect(
                        leftX + (halfWidth - finalWidth),
                        beginY,
                        leftX + halfWidth,
                        endY,
                        new Color(149, 250, 144).getRGB()
                );

                drawRect(
                        leftX + halfWidth,
                        beginY,
                        leftX + halfWidth + finalWidth,
                        endY,
                        new Color(149, 250, 144).getRGB()
                );
            }
        }

        public Tab addSetting(SettingItem item) {
            this.settings.add(item);

            return this;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;

            if (selected) {
                owningGui.settingItems = settings;
            }
        }

        public GuiButton getButton() {
            return button;
        }

        public int getIndex() {
            return index;
        }
    }
}