package cc.hyperium.mods.keystrokes;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;

public class CommandKeystrokes implements BaseCommand {
    
    private final KeystrokesMod mod;
    
    public CommandKeystrokes(KeystrokesMod mod) {
        this.mod = mod;
    }
    
    @Override
    public String getName() {
        return "keystrokesmod";
    }
    
    @Override
    public String getUsage() {
        return "Usage: " + getName();
    }
    
    @Override
    public void onExecute(String[] args) {
        new GuiScreenKeystrokes(this.mod).display();
    }
}