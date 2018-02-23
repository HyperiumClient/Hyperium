package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.mods.chromahud.gui.GeneralConfigGui;

/**
 * Created by mitchellkatz on 2/19/18. Designed for production use on Sk1er.club
 */
public class CommandChromaHUD implements BaseCommand {
    @Override
    public String getName() {
        return "chromahud";
    }

    @Override
    public String getUsage() {
        return "chromahud";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GeneralConfigGui(Hyperium.INSTANCE.getModIntegration().getChromaHUD()));
    }
}
