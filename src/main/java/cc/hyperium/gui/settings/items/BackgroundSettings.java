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

package cc.hyperium.gui.settings.items;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.ChangeBackgroundGui;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.HyperiumMainMenu;
import cc.hyperium.gui.ParticleOverlay;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;
import net.minecraft.client.Minecraft;
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

    @ConfigOpt
    public static boolean renderOverInventory = true;
    private DefaultConfig config;

    public BackgroundSettings(HyperiumGui previous) {
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

        selectionItem.addItems(Arrays.asList("1", "2", "3", "4", "5", "CUSTOM"));
        selectionItem.setSelectedItem(backgroundSelect);
        refreshBackground();

        SelectionItem<String> fastChat;
        this.settingItems.add(fastChat = new SelectionItem<>(1, getX(), getDefaultItemY(1), this.width - getX() * 2, "FAST CHAT", i -> {
            ((SelectionItem) i).nextItem();
            fastChatEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        fastChat.addDefaultOnOff();
        fastChat.setSelectedItem(fastChatEnabled ? "ON" : "OFF");


        SelectionItem<String> fastWorldGui;
        this.settingItems.add(fastWorldGui = new SelectionItem<>(2, getX(), getDefaultItemY(2), this.width - getX() * 2, "FAST CONTAINERS", i -> {
            ((SelectionItem) i).nextItem();
            fastWorldGuiEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        fastWorldGui.addDefaultOnOff();
        fastWorldGui.setSelectedItem(fastWorldGuiEnabled ? "ON" : "OFF");

        SelectionItem<String> particlesMode;
        this.settingItems.add(particlesMode = new SelectionItem<>(3, getX(), getDefaultItemY(3), this.width - getX() * 2, "PARTICLES MODE", i -> {
            if (!ParticleOverlay.getOverlay().purchased()) {
                return;
            }
            ((SelectionItem) i).nextItem();
            particlesModeString = ((SelectionItem<String>) i).getSelectedItem();
        }));
        if (ParticleOverlay.getOverlay().purchased()) {
            particlesMode.addItems(Arrays.asList("OFF", "PLAIN 1", "PLAIN 2", "CHROMA 1", "CHROMA 2"));
            particlesMode.setSelectedItem(particlesModeString);
        } else {
            particlesMode.addItems(Arrays.asList("NOT PURCHASED"));
            particlesMode.setSelectedItem("NOT PURCHASED");
        }

        SelectionItem<Integer> maxParticlesSelection;
        this.settingItems.add(maxParticlesSelection = new SelectionItem<>(4, getX(), getDefaultItemY(4), this.width - getX() * 2, "PARTICLES MAX", i -> {
            ((SelectionItem) i).nextItem();
            maxParticles = ((SelectionItem<Integer>) i).getSelectedItem();
            ParticleOverlay.reload();
        }));
        maxParticlesSelection.addItems(Arrays.asList(20, 50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500));
        maxParticlesSelection.setSelectedItem(maxParticles);

        SelectionItem<String> showOverInventory;
        this.settingItems.add(showOverInventory = new SelectionItem<>(5, getX(), getDefaultItemY(5), this.width - getX() * 2, "SHOW PARTICLES OVER INVENTORY", i -> {
            ((SelectionItem) i).nextItem();
            renderOverInventory = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        showOverInventory.addDefaultOnOff();
        showOverInventory.setSelectedItem(renderOverInventory ? "ON" : "OFF");


        SelectionItem<String> changeBg;
        settingItems.add(changeBg = new SelectionItem<>(6, getX(), getDefaultItemY(6), this.width - getX() * 2, "Custom Background", i -> Minecraft.getMinecraft().displayGuiScreen(new ChangeBackgroundGui(this))));
        changeBg.addItem("DOWNLOAD");
        changeBg.setSelectedItem("DOWNLOAD");

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
                HyperiumMainMenu.setCustomBackground(false);
                break;
            case "2":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/2.png"));
                HyperiumMainMenu.setCustomBackground(false);
                break;
            case "3":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/3.png"));
                HyperiumMainMenu.setCustomBackground(false);
                break;
            case "4":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/4.png"));
                HyperiumMainMenu.setCustomBackground(false);
                break;
            case "5":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/5.png"));
                HyperiumMainMenu.setCustomBackground(false);
                break;
            case "CUSTOM":
                HyperiumMainMenu.setCustomBackground(true);
                break;
        }
    }
}
