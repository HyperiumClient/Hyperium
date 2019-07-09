package cc.hyperium.mixinsimp.gui;

import cc.hyperium.gui.hyperium.HyperiumMainGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.resources.I18n;

import java.util.List;

public class HyperiumGuiOptions {
    private GuiOptions parent;

    public HyperiumGuiOptions(GuiOptions parent) {
        this.parent = parent;
    }

    public void initGui(List<GuiButton> buttonList) {
        buttonList.forEach(b -> {
            if (b.id == 200) {
                b.yPosition = parent.height - 30;
            }
        });
        buttonList.add(new GuiButton(114514, parent.width / 2 - 155, parent.height / 6 + 18, 150, 20, I18n.format("button.ingame.hyperiumsettings")));
    }

    public void actionPerformed(GuiButton button) {
        if (button.id == 114514) {
            HyperiumMainGui.INSTANCE.show();
        }
    }
}
