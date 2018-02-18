package com.hcc.commands.defaults;

import com.hcc.commands.BaseCommand;
import com.hcc.gui.ModConfigGui;

public class HCCConfigGui implements BaseCommand {
    @Override
    public String getName() {
        return "hccconfig";
    }

    @Override
    public String getUsage() {
        return "hccconfig";
    }

    @Override
    public void onExecute(String[] args) {
        new ModConfigGui().show();
    }
}
