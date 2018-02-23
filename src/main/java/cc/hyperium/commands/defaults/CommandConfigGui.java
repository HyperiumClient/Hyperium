package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.gui.ModConfigGui;

/**
 * A command to open the clients main configuration menu
 *
 * @author Sk1er
 */
public class CommandConfigGui implements BaseCommand {
    
    @Override
    public String getName() {
        return "Hyperiumconfig";
    }
    
    @Override
    public String getUsage() {
        return "Hyperiumconfig";
    }
    
    @Override
    public void onExecute(String[] args) {
        new ModConfigGui().show();
    }
}
