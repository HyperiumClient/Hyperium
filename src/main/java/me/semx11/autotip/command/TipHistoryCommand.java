package me.semx11.autotip.command;

import com.hcc.commands.BaseCommand;
import me.semx11.autotip.misc.TipTracker;
import me.semx11.autotip.util.ChatColor;
import me.semx11.autotip.util.ClientMessage;
import me.semx11.autotip.util.TimeUtil;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class TipHistoryCommand implements BaseCommand {

    public String getCommandName() {
        return "tiphistory";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "tiphistory [page]";
    }

    public List<String> getCommandAliases() {
        return Collections.singletonList("lasttip");
    }

    public void onCommand(ICommandSender sender, String[] args) {
        if (TipTracker.tipsSentHistory.size() > 0) {
            int page = 1;
            int pages = (int) Math.ceil((double) TipTracker.tipsSentHistory.size() / 7.0);

            if (args.length > 0) {
                try {
                    page = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                    page = -1;
                }
            }

            if (page < 1 || page > pages) {
                ClientMessage.send(ChatColor.RED + "Invalid page number.");
            } else {
                ClientMessage.separator();
                ClientMessage.send(ChatColor.GOLD + "Tip History " + ChatColor.GRAY
                        + "[Page " + page + " of " + pages + "]" + ChatColor.GOLD + ":");

                TipTracker.tipsSentHistory.entrySet().stream()
                        .skip((page - 1) * 7)
                        .limit(7)
                        .forEach(tip -> ClientMessage.send(tip.getValue() + ": " + ChatColor.GOLD
                                + TimeUtil.formatMillis(
                                System.currentTimeMillis() - tip.getKey()) + "."));

                ClientMessage.separator();
            }
        } else {
            ClientMessage.send(ChatColor.RED + "You haven't tipped anyone yet!");
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
