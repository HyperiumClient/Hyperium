package cc.hyperium.mods.timechanger.commands;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.timechanger.TimeChanger;
import cc.hyperium.mods.timechanger.TimeChanger.TimeType;
import cc.hyperium.utils.ChatColor;

public class CommandTimeChangerReset implements BaseCommand {
    
    private final TimeChanger mod;
    
    public CommandTimeChangerReset(TimeChanger main) {
        this.mod = main;
    }
    
    @Override
    public String getName() {
        return "resettime";
    }
    
    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /resettime";
    }
    
    @Override
    public void onExecute(String[] args) throws CommandException {
        this.mod.setTimeType(TimeType.VANILLA);
        GeneralChatHandler.instance().sendMessage(ChatColor.RED + "[TimeChanger] " + ChatColor.GREEN + "Now using vanilla time.", false);
    }
}
