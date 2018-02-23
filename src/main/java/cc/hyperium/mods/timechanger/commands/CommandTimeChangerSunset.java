package cc.hyperium.mods.timechanger.commands;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.timechanger.TimeChanger;
import cc.hyperium.mods.timechanger.TimeChanger.TimeType;
import cc.hyperium.utils.ChatColor;

public class CommandTimeChangerSunset implements BaseCommand {
    
    private final TimeChanger mod;
    
    public CommandTimeChangerSunset(TimeChanger main) {
        this.mod = main;
    }
    
    @Override
    public String getName() {
        return "sunset";
    }
    
    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /sunset";
    }
    
    @Override
    public void onExecute(String[] args) throws CommandException {
        this.mod.setTimeType(TimeType.SUNSET);
        GeneralChatHandler.instance().sendMessage(ChatColor.RED + "[TimeChanger] " + ChatColor.GREEN + "Time set to sunset.", false);
    }
}
