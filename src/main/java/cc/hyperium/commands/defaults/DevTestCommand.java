package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import net.minecraft.client.Minecraft;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class DevTestCommand implements BaseCommand {

    /**
     * Gets the name of the command (text after slash).
     *
     * @return The command name
     */
    @Override
    public String getName() {
        return "test";
    }

    /**
     * Gets the usage string for the command.
     *
     * @return The command usage
     */
    @Override
    public String getUsage() {
        return null;
    }

    /**
     * Callback when the command is invoked
     */
    @Override
    public void onExecute(String[] args) throws CommandException {
        Hyperium.INSTANCE.getHandlers().getYeetHandler().yeet(Minecraft.getMinecraft().thePlayer.getUniqueID());
    }
}
