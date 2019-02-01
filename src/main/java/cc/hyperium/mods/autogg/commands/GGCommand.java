package cc.hyperium.mods.autogg.commands;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.commands.CommandUsageException;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.utils.ChatColor;

public class GGCommand implements BaseCommand {

    private final AutoGG mod;

    public GGCommand(AutoGG mod) {
        this.mod = mod;
    }

    @Override
    public String getName() {
        return "autogg";
    }

    @Override
    public String getUsage() {
        return "Usage: /autogg <toggle, delay [seconds]>";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (args.length == 0 || args.length > 2) {
            throw new CommandUsageException();
        }

        final String s = args[0];

        switch (s) {
            case "toggle":
            case "t": {
                this.mod.getConfig().flipToggle();

                this.showMessage(
                    ChatColor.GRAY + "AutoGG: " + (this.mod.getConfig().isToggled() ? (
                        ChatColor.GREEN + "On") : (ChatColor.RED + "Off")));

                saveConfig();
                break;
            }
            case "delay":
            case "d":
            case "time": {
                if (args.length == 2) {
                    try {
                        final int delay = Integer.parseInt(args[1]);
                        if (delay < 0 || delay > 5) {
                            throw new NumberFormatException("Invalid integer");
                        }
                        this.mod.getConfig().setDelay(delay);

                        saveConfig();

                        this.showMessage(ChatColor.GRAY + "AutoGG delay set to "
                            + ChatColor.GREEN + this.mod.getConfig().getDelay() + "s");
                    } catch (NumberFormatException e) {
                        showMessage(ChatColor.RED + "Error: Please use an integer between 1 and 5 seconds.");
                    }
                    break;
                }
                this.showMessage(
                    ChatColor.GRAY + "AutoGG Delay: " + ChatColor.GREEN + this.mod.getConfig().getDelay() + "s");
                break;
            }
            default: {
                throw new CommandUsageException();
            }
        }
    }

    /**
     * Sends a message to the client
     *
     * @param message the message to send
     */
    private void showMessage(final String message) {
        GeneralChatHandler.instance().sendMessage(message, false);
    }

    /**
     * Saves the config
     */
    private void saveConfig() {
        Hyperium.CONFIG.save();
    }
}
