package cc.hyperium.mods.timechanger.commands;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.commands.CommandUsageException;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.timechanger.TimeChanger;
import cc.hyperium.mods.timechanger.TimeChanger.TimeType;
import cc.hyperium.utils.ChatColor;

import org.apache.commons.lang3.math.NumberUtils;

public class CommandTimeChangerFastTime implements BaseCommand {
    
    private final TimeChanger mod;
    
    public CommandTimeChangerFastTime(TimeChanger main) {
        this.mod = main;
    }
    
    @Override
    public String getName() {
        return "fasttime";
    }
    
    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /fasttime <multiplier>";
    }
    
    @Override
    public void onExecute(String[] args) throws CommandException {
        if (args.length == 0) {
            throw new CommandUsageException();
        }
        
        final double multiplier = NumberUtils.toDouble(args[0], -1.0);
        
        if (multiplier < 0.0) {
            GeneralChatHandler.instance().sendMessage(ChatColor.RED + "[TimeChanger] " + ChatColor.RED + "Invalid multiplier!", false);
            return;
        }
        
        this.mod.setTimeType(TimeType.FAST);
        this.mod.setFastTimeMultiplier(multiplier);
        GeneralChatHandler.instance().sendMessage(ChatColor.RED + "[TimeChanger] " + ChatColor.GREEN + "Time set to fast (" + multiplier + ").", false);
    }
}
