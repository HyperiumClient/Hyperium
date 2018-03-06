/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
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

package cc.hyperium.gui.settings.items;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.HyperiumMainMenu;
import cc.hyperium.gui.ParticleOverlay;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class BackgroundSettings extends SettingGui {

    @ConfigOpt
    public static String backgroundSelect = "1";
    @ConfigOpt
    public static boolean fastWorldGuiEnabled = false; // Default to false
    @ConfigOpt
    public static boolean fastChatEnabled = false;
    @ConfigOpt
    public static String particlesModeString = "OFF";
    @ConfigOpt
    public static int maxParticles = 200;
    private DefaultConfig config;
    private SelectionItem<String> fastWorldGui;
    private SelectionItem<String> fastChat;
    private SelectionItem<String> particlesMode;
    private SelectionItem<Integer> maxParticlesSelection;

    public BackgroundSettings(GuiScreen previous) {
        super("BACKGROUNDS", previous);
        config = Hyperium.CONFIG;
        config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();

        SelectionItem<String> selectionItem;
        settingItems.add(selectionItem = new SelectionItem<>(0, getX(), getDefaultItemY(0), width - getX() * 2, "MENU BACKGROUND", i -> {
            ((SelectionItem) i).nextItem();
            backgroundSelect = (String) ((SelectionItem) i).getSelectedItem();
            refreshBackground();
        }));

        selectionItem.addItems(Arrays.asList("1", "2", "3", "4", "5"));
        selectionItem.setSelectedItem(backgroundSelect);
        refreshBackground();

        this.settingItems.add(this.fastChat = new SelectionItem<>(1, getX(), getDefaultItemY(1), this.width - getX() * 2, "FAST CHAT", i -> {
            ((SelectionItem) i).nextItem();
            fastChatEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        this.fastChat.addDefaultOnOff();
        this.fastChat.setSelectedItem(fastChatEnabled ? "ON" : "OFF");

        this.settingItems.add(this.fastWorldGui = new SelectionItem<>(2, getX(), getDefaultItemY(2), this.width - getX() * 2, "FAST CONTAINERS", i -> {
            ((SelectionItem) i).nextItem();
            fastWorldGuiEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        this.fastWorldGui.addDefaultOnOff();
        this.fastWorldGui.setSelectedItem(fastWorldGuiEnabled ? "ON" : "OFF");

        this.settingItems.add(this.particlesMode = new SelectionItem<>(3, getX(), getDefaultItemY(3), this.width - getX() * 2, "PARTICLES MODE", i -> {
            ((SelectionItem) i).nextItem();
            particlesModeString = ((SelectionItem<String>) i).getSelectedItem();
        }));
        this.particlesMode.addItems(Arrays.asList("OFF", "PLAIN 1", "PLAIN 2", "CHROMA 1", "CHROMA 2"));
        this.particlesMode.setSelectedItem(particlesModeString);

        this.settingItems.add(maxParticlesSelection = new SelectionItem<>(4, getX(), getDefaultItemY(4), this.width - getX() * 2, "PARTICLES MAX", i -> {
            ((SelectionItem) i).nextItem();
            maxParticles = ((SelectionItem<Integer>) i).getSelectedItem();
            ParticleOverlay.reload();
        }));
        this.maxParticlesSelection.addItems(Arrays.asList(20, 50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500));
        this.maxParticlesSelection.setSelectedItem(maxParticles);

    }

    private int getDefaultItemY(int i) {
        return getY() + 25 + i * 15;
    }

    @Override
    public void onGuiClosed() {
        this.config.save();
    }

    private void refreshBackground() {
        switch (((SelectionItem<String>) settingItems.get(0)).getSelectedItem()) {
            case "1":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/1.png"));
                break;
            case "2":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/2.png"));
                break;
            case "3":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/3.png"));
                break;
            case "4":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/4.png"));
                break;
            case "5":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/5.png"));
        }
    }
}
