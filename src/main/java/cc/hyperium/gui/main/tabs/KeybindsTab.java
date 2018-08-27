package cc.hyperium.gui.main.tabs;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.keybinds.GuiKeybinds;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.SettingItem;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class KeybindsTab extends AbstractTab {
    private GuiBlock block;
    private int y, w;

    public KeybindsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;

        items.add(new SettingItem(() -> {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiKeybinds());
        },
            Icons.FA_KEYBOARD.getResource(),"Configure Keybinds","Configure Hyperium Keybinds","Click to configure.",0,0));
    }

    @Override
    public void drawTabIcon() {
        Icons.FA_KEYBOARD.bind();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        Gui.drawScaledCustomSizeModalRect(5, y + 5, 0, 0, 144, 144, w - 10, w - 10, 144, 144);
    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight(float s) {
        Gui.drawRect(0, (int) (y + s * (s * w / 2)), 3, (int) (y + w - s * (w / 2)), Color.WHITE.getRGB());
    }

    @Override
    public String getTitle() {
        return "Keybinds";
    }
}
