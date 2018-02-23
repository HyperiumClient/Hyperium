package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.commands.CommandUsageException;
import cc.hyperium.gui.integrations.HypixelPrivateMessage;

public class CommandPrivateMessage implements BaseCommand {
    
    @Override
    public String getName() {
        return "pm";
    }

    @Override
    public String getUsage() {
        return "pm <name>";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (args.length != 1) {
            throw new CommandUsageException();
        } else {
            new HypixelPrivateMessage(Hyperium.INSTANCE.getHandlers().getPrivateMessageHandler().getChat(args[0])).show();
        }
    }
}
