package cc.hyperium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class GuiIngameMenu extends net.minecraft.client.gui.GuiIngameMenu {

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(8, this.width / 2 + 2, this.height / 4 + 48, 98, 20, "Credits"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 8) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiCredits(this));
        }
    }
}
