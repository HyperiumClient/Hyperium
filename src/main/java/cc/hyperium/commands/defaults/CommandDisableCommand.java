package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.HyperiumCommandHandler;
import cc.hyperium.utils.ChatColor;

public class CommandDisableCommand implements BaseCommand {

    private HyperiumCommandHandler handler = Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler();

    @Override
    public String getName() {
        return "disablecommand";
    }

    @Override
    public String getUsage() {
        return "Ignores a command when executed in Hyperium";
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Please input the command to disable");
            return;
        }

        String command = args[0];

        if (command.equalsIgnoreCase("disablecommand") || command.equalsIgnoreCase("hyperium")) {
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("That command cannot be disabled!");
            return;
        }

        // True if the command is disabled, false if it is not
        boolean disabled = this.handler.addOrRemoveCommand(args[0]);

        ChatColor color = disabled ? ChatColor.RED : ChatColor.GREEN;

        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Command \'" + args[0] + "\' is " + color + (disabled ? "now" : "no longer") + ChatColor.WHITE + " disabled!");

        this.handler.saveDisabledCommands();
    }
}
