package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.hud.NetworkInfo;
import cc.hyperium.handlers.handlers.hud.TabCompletionUtil;
import net.minecraft.client.Minecraft;

import java.util.List;

public class CommandPing implements BaseCommand {

    /**
     * Gets the name of the command (text after slash).
     *
     * @return The command name
     */
    @Override
    public String getName() {
        return "ping";
    }

    /**
     * Gets the usage string for the command.
     *
     * @return The command usage
     */
    @Override
    public String getUsage() {
        return "/ping or /ping <name>";
    }

    /**
     * Callback when the command is invoked
     */
    @Override
    public void onExecute(String[] args) throws CommandException {
        final String name = (args.length == 1) ? args[0] : Minecraft.getMinecraft().getSession().getUsername();
        NetworkInfo.getInstance().printPing(name);
    }

    /**
     * Called when the player clicks tab in the chat menu, used to provide suggestions for a commands arguments
     *
     * @param args the arguments the player has entered
     * @return a String List containing all viable tab completions
     */
    @Override
    public List<String> onTabComplete(String[] args) {
        return TabCompletionUtil.getListOfStringsMatchingLastWord(args, TabCompletionUtil.getTabUsernames());
    }
}
