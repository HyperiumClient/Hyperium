package cc.hyperium.gui.settings.items;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.HyperiumMainMenu;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class BackgroundSettings extends SettingGui {

    private DefaultConfig config;
    
    @ConfigOpt
    public static String backgroundSelect = "1";
    @ConfigOpt
    public static boolean fastWorldGuiEnabled = false; // Default to false
    @ConfigOpt
    public static boolean fastChatEnabled = false;
    @ConfigOpt
    public static String particlesModeString = "OFF";

    private SelectionItem<String> fastWorldGui;
    private SelectionItem<String> fastChat;
    private SelectionItem<String> particlesMode;

    public BackgroundSettings(GuiScreen previous) {
        super("BACKGROUNDS", previous);
        config = Hyperium.CONFIG;
        config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();
        
        SelectionItem<String> selectionItem = new SelectionItem<>(0, getX(), getDefaultItemY(0),  width - getX() * 2, "MENU BACKGROUND", i -> {
            ((SelectionItem) i).nextItem();
            backgroundSelect = (String) ((SelectionItem) i).getSelectedItem();
            refreshBackground();
        });
        
        selectionItem.addItems(Arrays.asList("1", "2", "3", "4", "5"));
        selectionItem.setSelectedItem(backgroundSelect);
        refreshBackground();

        this.settingItems.add(selectionItem);

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

    }

    private int getDefaultItemY(int i) {
        return getY() + 25 + i * 15;
    }

    @Override
    public void onGuiClosed() {
        this.config.save();
    }

    private void refreshBackground(){
        switch (((SelectionItem<String>)settingItems.get(0)).getSelectedItem()) {
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
