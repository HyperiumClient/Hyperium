package com.hcc.commands.defaults;

import com.hcc.HCC;
import com.hcc.commands.BaseCommand;
import com.hcc.commands.CommandException;
import com.hcc.commands.CommandUsageException;
import com.hcc.gui.integrations.HypixelPrivateMessage;

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
            new HypixelPrivateMessage(HCC.INSTANCE.getHandlers().getPrivateMessageHandler().getChat(args[0])).show();
        }
    }
}
