package cc.hyperium.addons.customcrosshair.command;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.customcrosshair.gui.GuiCustomCrosshairEditCrosshair;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.commands.BaseCommand;

import java.util.Collections;
import java.util.List;

public class CommandCustomCrosshair implements BaseCommand {

    private CustomCrosshairAddon addon;

    public CommandCustomCrosshair(CustomCrosshairAddon addon) {
        this.addon = addon;
    }

    @Override
    public String getName() {
        return "customcrosshairaddon";
    }

    @Override
    public String getUsage() {
        return "/customcrosshairaddon";
    }

    @Override
    public void onExecute(String[] strings) {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiCustomCrosshairEditCrosshair(this.addon));
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("cch");
    }
}
