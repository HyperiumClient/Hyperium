package cc.hyperium.mods.timechanger.commands;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.timechanger.TimeChanger;
import cc.hyperium.mods.timechanger.TimeChanger.TimeType;
import cc.hyperium.utils.ChatColor;

public class CommandTimeChangerNight implements BaseCommand {
    
    private final TimeChanger mod;
    
    public CommandTimeChangerNight(TimeChanger main) {
        this.mod = main;
    }
    
    @Override
    public String getName() {
        return "night";
    }
    
    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /night";
    }
    
    @Override
    public void onExecute(String[] args) throws CommandException {
        this.mod.setTimeType(TimeType.NIGHT);
        GeneralChatHandler.instance().sendMessage(ChatColor.RED + "[TimeChanger] " + ChatColor.GREEN + "Time set to night.", false);
    }
}
