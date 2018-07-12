package cc.hyperium.mixinsimp.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiAddonError;
import cc.hyperium.gui.GuiTos;
import cc.hyperium.gui.HyperiumMainMenu;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

public class HyperiumGuiMainMenu {
    private GuiMainMenu parent;

    public HyperiumGuiMainMenu(GuiMainMenu parent) {
        this.parent = parent;
    }

    public void initGui() {
        if (Hyperium.INSTANCE.isAcceptedTos()) {
            parent.drawDefaultBackground();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!Hyperium.INSTANCE.isAcceptedTos()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiTos());
        } else if (!AddonMinecraftBootstrap.getDependenciesLoopMap().isEmpty() || !AddonMinecraftBootstrap.getMissingDependenciesMap().isEmpty()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiAddonError());
        } else {
            Minecraft.getMinecraft().displayGuiScreen(new HyperiumMainMenu());
        }
    }
}
