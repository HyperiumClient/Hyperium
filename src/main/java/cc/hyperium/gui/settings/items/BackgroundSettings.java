package cc.hyperium.gui.settings.items;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import static cc.hyperium.gui.HyperiumMainMenu.setBackground;

public class BackgroundSettings extends SettingGui {

    private DefaultConfig config;

    private SelectionItem backgroundNo;
    
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
        settingItems.add(backgroundNo = new SelectionItem(0, getX(), getDefaultItemY(0),  width - getX() * 2, "BACKGROUND:", i->{
            ((SelectionItem)i).nextItem();
            backgroundSelect = (String) ((SelectionItem) i).getSelectedItem();
        }));
        backgroundNo.addItem("1");
        backgroundNo.addItem("2");
        backgroundNo.addItem("3");
        backgroundNo.setSelectedItem(backgroundNo.toString());
        System.out.println(backgroundNo.getSelectedItem());
        if(backgroundNo.getSelectedItem() == "1") {
            setBackground(new ResourceLocation("textures/material/backgrounds/1.png"));
        }else if (backgroundNo.getSelectedItem() == "2") {
            setBackground(new ResourceLocation("textures/material/backgrounds/2.png"));
        } else if (backgroundNo.getSelectedItem() == "3") {
            setBackground(new ResourceLocation("textures/material/backgrounds/3.png"));
        }
    }

    private int getDefaultItemY(int i) {
        return getY()+25 + i * 15;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        config.save();
    }

}
