package cc.hyperium.mixinsimp.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiAddonError;
import cc.hyperium.gui.GuiHyperiumScreenMainMenu;
import cc.hyperium.gui.GuiHyperiumScreenTos;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
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
            System.out.println("Hasn't accepted! Redirecting them!");
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiHyperiumScreenTos());
        } else if (!AddonMinecraftBootstrap.getDependenciesLoopMap().isEmpty() || !AddonMinecraftBootstrap.getMissingDependenciesMap().isEmpty()) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiAddonError());
        } else {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiHyperiumScreenMainMenu());
        }
    }

}
