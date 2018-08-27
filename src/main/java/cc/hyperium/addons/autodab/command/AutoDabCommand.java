package cc.hyperium.addons.autodab.command;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.config.Settings;
import cc.hyperium.utils.ChatColor;

public class AutoDabCommand implements BaseCommand {
    public String getName() {
        return "autodab";
    }

    public String getUsage() {
        return "autodab <toggle|length [Length]|f5>";
    }

    public void onExecute(final String[] args) throws CommandException {
        if (args.length == 0) {
            throw new CommandException("/" + this.getUsage());
        }
        if (args[0].equalsIgnoreCase("toggle")) {
            Settings.AUTO_DAB_ENABLED = !Settings.AUTO_DAB_ENABLED;
            this.sendMessage((Settings.AUTO_DAB_ENABLED ? "Enabled" : "Disabled") + " Auto Dab.");
        }
        else {
            if (args[0].equalsIgnoreCase("length")) {
                if (args.length == 1) {
                    this.sendMessage("The current length is " + Settings.AUTO_DAB_LENGTH + ".");
                    return;
                }
                try {
                    final int length = Integer.parseInt(args[1]);
                    if (length <= 0 || length > 10) {
                        throw new CommandException("Please enter a number 1 - 10.");
                    }
                    Settings.AUTO_DAB_LENGTH = length;
                    throw new CommandException("Set the dab length to " + length + ".");
                }
                catch (NumberFormatException e) {
                    throw new CommandException("Please enter a valid number.");
                }
            }
            else {
                if (!args[0].equalsIgnoreCase("f5")) {
                    throw new CommandException("Unrecognised sub-command.");
                }
                Settings.AUTO_DAB_THIRD_PERSON = !Settings.AUTO_DAB_THIRD_PERSON;
                this.sendMessage((Settings.AUTO_DAB_THIRD_PERSON ? "Enabled" : "Disabled") + " automatic third person.");
            }
        }
    }

    private void sendMessage(final String message) {
        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + ChatColor.BOLD + "autodab" + ChatColor.DARK_AQUA + "] " + ChatColor.WHITE + message, false);
    }
}
