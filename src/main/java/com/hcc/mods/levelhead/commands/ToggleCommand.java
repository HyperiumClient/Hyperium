package com.hcc.mods.levelhead.commands;

import com.hcc.mods.levelhead.Levelhead;
import com.hcc.mods.levelhead.guis.LevelHeadGui;
import com.hcc.mods.sk1ercommon.Sk1erMod;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by Mitchell Katz on 5/8/2017.
 */
public class ToggleCommand extends CommandBase {

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "levelhead";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("limit")) {
                Sk1erMod.getInstance().sendMessage(EnumChatFormatting.RED + "Count: " + Levelhead.getInstance().count);
                Sk1erMod.getInstance().sendMessage(EnumChatFormatting.RED + "Wait: " + Levelhead.getInstance().wait);
                Sk1erMod.getInstance().sendMessage(EnumChatFormatting.RED + "Hypixel: " + Sk1erMod.getInstance().isHypixel());
                Sk1erMod.getInstance().sendMessage(EnumChatFormatting.RED + "Remote Status: " + Sk1erMod.getInstance().isEnabled());
                Sk1erMod.getInstance().sendMessage(EnumChatFormatting.RED + "Local Stats: " + Levelhead.getInstance().getSk1erMod().isHypixel());
                Sk1erMod.getInstance().sendMessage(EnumChatFormatting.RED + "Header State: " + Levelhead.getInstance().getHeaderConfig());
                Sk1erMod.getInstance().sendMessage(EnumChatFormatting.RED + "Footer State: " + Levelhead.getInstance().getFooterConfig());
                Sk1erMod.getInstance().sendMessage(EnumChatFormatting.RED + "Callback: " + Sk1erMod.getInstance().getResponse());
                return;
            } else if (args[0].equalsIgnoreCase("dumpcache")) {
                Levelhead.getInstance().levelCache.clear();

                Sk1erMod.getInstance().sendMessage("Stringcache entries: " + Levelhead.getInstance().levelCache.size());
                ;
                return;
            }
        }
        new LevelHeadGui().display();
    }
}
