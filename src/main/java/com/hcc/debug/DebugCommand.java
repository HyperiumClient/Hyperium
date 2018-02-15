package com.hcc.debug;

import com.hcc.HCC;
import com.hcc.gui.integrations.HypixelFriendsGui;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class DebugCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "hccdebug";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "hccdebug";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        HCC.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new HypixelFriendsGui());
    }
}
