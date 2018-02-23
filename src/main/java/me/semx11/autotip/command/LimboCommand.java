package me.semx11.autotip.command;

import cc.hyperium.commands.BaseCommand;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.util.ChatColor;
import me.semx11.autotip.util.ClientMessage;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class LimboCommand implements BaseCommand {

    public static boolean executed;

    public String getCommandName() {
        return "limbo";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "limbo";
    }

    public void onCommand(ICommandSender sender, String[] args) {
        if (Autotip.onHypixel) {
            executed = true;
            Autotip.mc.thePlayer.sendChatMessage(ChatColor.RED.toString());
        } else {
            ClientMessage.send(ChatColor.RED + "You must be on Hypixel to use this command!");
        }
    }

    public List<String> onTabComplete(ICommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return getCommandName();
    }

    @Override
    public String getUsage() {
        return getCommandUsage(null);
    }

    @Override
    public void onExecute(String[] args) {
        onCommand(null, args);
    }
}
