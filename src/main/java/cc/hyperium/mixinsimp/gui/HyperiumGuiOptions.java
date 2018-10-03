package cc.hyperium.mixinsimp.gui;

import java.util.List;

import cc.hyperium.gui.main.MainHyperiumMainGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;

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
        buttonList.add(new GuiButton(114514, parent.width / 2 - 155, parent.height / 6 + 18, 150, 20, "Hyperium Settings"));
    }

    public void actionPerformed(GuiButton button){
        if(button.id == 114514){
            MainHyperiumMainGui.INSTANCE.show();
        }
    }
}
