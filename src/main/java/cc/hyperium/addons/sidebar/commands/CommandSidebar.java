package cc.hyperium.addons.sidebar.commands;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.sidebar.SidebarAddon;
import cc.hyperium.addons.sidebar.gui.screen.GuiScreenSettings;
import cc.hyperium.commands.BaseCommand;

public class CommandSidebar implements BaseCommand {
    private SidebarAddon addon;

    public CommandSidebar(SidebarAddon addon) {
        this.addon = addon;
    }

    @Override
    public String getName() {
        return "sidebaraddon";
    }

    @Override
    public String getUsage() {
        return "/sidebaraddon";
    }

    @Override
    public void onExecute(String[] strings) {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiScreenSettings(this.addon));
    }


}
