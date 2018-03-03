package cc.hyperium.gui.settings.items;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.HyperiumMainMenu;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class BackgroundSettings extends SettingGui {

    private DefaultConfig config;
    
    @ConfigOpt
    public static String backgroundSelect = "1";

    @ConfigOpt
    public static boolean fastWorldGuiEnabled = true;

    private SelectionItem<String> fastWorldGui;

    @ConfigOpt
    public static boolean fastChatEnabled = true;

    private SelectionItem<String> fastChat;


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
        });
        
        selectionItem.addItem("1");
        selectionItem.addItem("2");
        selectionItem.addItem("3");
        selectionItem.setSelectedItem(backgroundSelect);

        switch (selectionItem.getSelectedItem()) {
            case "1":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/1.png"));
                break;
            case "2":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/2.png"));
                break;
            case "3":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/3.png"));
                break;
        }

        this.settingItems.add(selectionItem);

        this.settingItems.add(this.fastChat = new SelectionItem<>(1, getX(), getDefaultItemY(7), this.width - getX() * 2, "FAST CHAT", i -> {
            ((SelectionItem) i).nextItem();
            fastChatEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        this.fastChat.addDefaultOnOff();
        this.fastChat.setSelectedItem(fastChatEnabled ? "ON" : "OFF");

        this.settingItems.add(this.fastWorldGui = new SelectionItem<>(2, getX(), getDefaultItemY(7), this.width - getX() * 2, "FAST CONTAINERS", i -> {
            ((SelectionItem) i).nextItem();
            fastWorldGuiEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        this.fastWorldGui.addDefaultOnOff();
        this.fastWorldGui.setSelectedItem(fastWorldGuiEnabled ? "ON" : "OFF");

    }

    private int getDefaultItemY(int i) {
        return getY() + 25 + i * 15;
    }

    @Override
    public void onGuiClosed() {
        this.config.save();
    }
}
