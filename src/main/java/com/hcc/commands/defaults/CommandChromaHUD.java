package com.hcc.commands.defaults;

import com.hcc.HCC;
import com.hcc.commands.BaseCommand;
import com.hcc.commands.CommandException;
import com.hcc.mods.chromahud.gui.GeneralConfigGui;

/**
 * Created by mitchellkatz on 2/19/18. Designed for production use on Sk1er.club
 */
public class CommandChromaHUD implements BaseCommand{
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
        HCC.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GeneralConfigGui(HCC.INSTANCE.getModIntegration().getChromaHUD()));
    }
}
