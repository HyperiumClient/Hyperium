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
import cc.hyperium.gui.settings.SettingItem;
import cc.hyperium.gui.settings.components.OnOffSetting;
import cc.hyperium.gui.settings.components.SelectionItem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

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

    private final DefaultConfig config;
    
    private int currentID = 0;
    
    /**
     * Set to true when a setting is changed, this will trigger a save when the gui is closed
     */
    private boolean settingsUpdated;

    public BackgroundSettings(HyperiumGui previous) {
        super("BACKGROUNDS", previous);
        config = Hyperium.CONFIG;
        config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();
    
        this.currentID = 0;

        SelectionItem<String> x = registerCustomSetting("MENU BACKGROUND", backgroundSelect, i -> {
            ((SelectionItem) i).nextItem();
            
            backgroundSelect = ((SelectionItem<String>) i).getSelectedItem();
            
            refreshBackground();
        });
        x.addItems(Arrays.asList("1", "2", "3", "4", "5", "6", "CUSTOM"));
        x.setSelectedItem(backgroundSelect);
        
        refreshBackground();
        
        registerOnOffSetting("FAST CHAT", fastChatEnabled, on -> fastChatEnabled = on);
        registerOnOffSetting("FAST CONTAINERS", fastWorldGuiEnabled, on -> fastWorldGuiEnabled = on);
    
        SelectionItem<String> particlesMode = registerCustomSetting("PARTICLES MODE", particlesModeString, i -> {
            ((SelectionItem) i).nextItem();
    
            particlesModeString = ((SelectionItem<String>) i).getSelectedItem();
        });
    
        if (ParticleOverlay.getOverlay().purchased()) {
            particlesMode.addItems(Arrays.asList("OFF", "PLAIN 1", "PLAIN 2", "CHROMA 1", "CHROMA 2"));
            particlesMode.setSelectedItem(particlesModeString);
        } else {
            particlesMode.addItems(Collections.singletonList("NOT PURCHASED"));
            particlesMode.setSelectedItem("NOT PURCHASED");
        }
    
        SelectionItem<Integer> maxP = registerIntegerSetting("PARTICLES MAX", maxParticles, i -> {
            ParticleOverlay.reload();
        });
        maxP.addItems(Arrays.asList(20, 50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500));
        maxP.setSelectedItem(maxParticles);
        
        registerOnOffSetting("SHOW PARTICLES OVER INVENTORY", renderOverInventory, on -> renderOverInventory = on);
        
        registerCustomSetting("CUSTOM BACKGROUND", "DOWNLOAD", i -> Minecraft.getMinecraft().displayGuiScreen(new ChangeBackgroundGui(this)));

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
            case "6":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/6.png"));
                HyperiumMainMenu.setCustomBackground(false);
                break;
            case "CUSTOM":
                HyperiumMainMenu.setCustomBackground(true);
                break;
        }
    }
    
    private int getDefaultItemY(int i) {
        return getY() + 25 + i * 15;
    }
    
    private void registerOnOffSetting(String name, boolean enabled, Consumer<Boolean> callback) {
        OnOffSetting setting = new OnOffSetting(
            currentID,
            getX(),
            getDefaultItemY(currentID),
            this.width - (getX() * 2),
            name
        );
        setting.setEnabled(enabled).setConsumer(
            callback.andThen((on) -> settingsUpdated = true)
        );
        
        this.settingItems.add(setting);
        
        currentID++;
    }
    
    private SelectionItem<Integer> registerIntegerSetting(String name, Integer selected, Consumer<SettingItem> callback) {
        SelectionItem<Integer> item = new SelectionItem<>(
            currentID,
            getX(),
            getDefaultItemY(currentID),
            this.width - (getX() * 2),
            name,
            
            callback.andThen((i) -> {
                settingsUpdated = true;
                ((SelectionItem) i).nextItem();
            })
        );
        item.setSelectedItem(selected);
    
        this.settingItems.add(item);
    
        currentID++;
    
        return item;
    }
    
    private SelectionItem<String> registerCustomSetting(String name, String selected, Consumer<SettingItem> callback) {
        SelectionItem<String> item = new SelectionItem<>(
            currentID,
            getX(),
            getDefaultItemY(currentID),
            this.width - (getX() * 2),
            name,
            callback.andThen((i) -> {
                settingsUpdated = true;
//                ((SelectionItem) i).nextItem();
            })
        );
        item.setSelectedItem(selected);
        
        this.settingItems.add(item);
        
        currentID++;
        
        return item;
    }
    
    @Override
    public void onGuiClosed() {
        // If a setting has been modified, we'll save the config.
        if (this.settingsUpdated) {
            this.config.save();
        }
    }
}
