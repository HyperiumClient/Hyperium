package cc.hyperium.mods.keystrokes;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandKeystrokes implements BaseCommand {

    public String getCommandName() {
        return "keystrokesmod";
    }

    public String getCommandUsage(ICommandSender sender) {
        return getCommandName();
    }

    public void processCommand(ICommandSender sender, String[] args) {
        KeystrokesMod.openGui();
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
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
    public void onExecute(String[] args) throws CommandException {
        processCommand(null,args);
    }
}