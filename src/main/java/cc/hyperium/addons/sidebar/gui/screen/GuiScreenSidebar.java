package cc.hyperium.addons.sidebar.gui.screen;

import cc.hyperium.addons.sidebar.SidebarAddon;
import cc.hyperium.addons.sidebar.gui.GuiSidebar;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;

import java.io.IOException;

public class GuiScreenSidebar extends GuiScreen
{
    protected SidebarAddon addon;
    protected GuiSidebar sidebar;
    private boolean dragging;
    private int lastX;
    private int lastY;

    public GuiScreenSidebar(final SidebarAddon addon) {
        this.addon = addon;
        this.sidebar = addon.getSidebarGui();
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.mc.thePlayer != null) {
            final ScoreObjective scoreObjective = this.mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreObjective != null) {
                this.sidebar.drawSidebar(scoreObjective, new ScaledResolution(this.mc));
            }
        }
        if (this.dragging) {
            final GuiSidebar sidebar = this.sidebar;
            sidebar.offsetX += mouseX - this.lastX;
            final GuiSidebar sidebar2 = this.sidebar;
            sidebar2.offsetY += mouseY - this.lastY;
        }
        this.lastX = mouseX;
        this.lastY = mouseY;
    }

    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.sidebar.contains(mouseX, mouseY)) {
            this.dragging = true;
        }
    }

    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
    }

    public void onGuiClosed() {
        this.addon.saveConfig();
    }
}
