package cc.hyperium.mods.togglechat.commands;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.mods.togglechat.gui.ToggleChatMainGui;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.mods.togglechat.ToggleChatMod;

import java.util.Collections;
import java.util.List;

/**
 * The BaseCommand implementation for ToggleChat's command with aliases
 *
 * @author boomboompower
 */
public class CommandToggleChat implements BaseCommand {
    
    /** The "mod" instance */
    private ToggleChatMod mod;
    
    /** Default constructor */
    public CommandToggleChat(ToggleChatMod impl) {
        this.mod = impl;
    }
    
    @Override
    public String getName() {
        return "chattoggle";
    }
    
    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /chattoggle";
    }
    
    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("tc");
    }
    
    @Override
    public void onExecute(String[] args) throws CommandException {
        new ToggleChatMainGui(this.mod, 0).display();
    }
}
