package com.hcc.commands.defaults;

import com.hcc.commands.BaseCommand;
import com.hcc.gui.ModConfigGui;

/**
 * A command to open the clients main configuration menu
 *
 * @author Sk1er
 */
public class CommandConfigGui implements BaseCommand {
    
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
