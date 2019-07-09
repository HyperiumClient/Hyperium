package cc.hyperium.mixinsimp.gui;

import cc.hyperium.gui.keybinds.GuiKeybinds;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class HyperiumGuiControls {

    public void initGui(List<GuiButton> buttonList) {
        buttonList.add(new GuiButton(10, ResolutionUtil.current().getScaledWidth() / 2 - 60, ResolutionUtil.current().getScaledHeight() - 10, 120, 10, "Hyperium Binds"));
    }

    public void actionPerformed(GuiButton button) {
        if (button.id == 10) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiKeybinds());
        }
    }
}
