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
        selectionItem.addItem("4");
        selectionItem.addItem("5");
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
            case "4":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/4.png"));
                break;
            case "5":
                HyperiumMainMenu.setBackground(new ResourceLocation("textures/material/backgrounds/5.png"));
        }

        this.settingItems.add(selectionItem);

    }

    private int getDefaultItemY(int i) {
        return getY() + 25 + i * 15;
    }

    @Override
    public void onGuiClosed() {
        this.config.save();
    }
}
