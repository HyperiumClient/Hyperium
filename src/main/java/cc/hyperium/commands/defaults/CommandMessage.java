package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.hud.TabCompletionUtil;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CommandMessage implements BaseCommand {

    /**
     * Gets the name of the command (text after slash).
     *
     * @return The command name
     */
    @Override
    public String getName() {
        return "msg";
    }

    /**
     * Gets the usage string for the command.
     *
     * @return The command usage
     */
    @Override
    public String getUsage() {
        return "/msg <name> <msg>";
    }

    /**
     * A list of aliases to the main command. This will not be used if the list
     * is empty or {@code null}.
     *
     * @return The command aliases, which behave the same as the {@link #getName()}.
     */
    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("msg", "tell", "w", "message");
    }

    /**
     * Callback when the command is invoked
     */
    @Override
    public void onExecute(String[] args) throws CommandException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/msg " + builder.toString());
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        List<String> tabUsernames = TabCompletionUtil.getTabUsernames();
        addTabHypixel(tabUsernames);
        tabUsernames.remove(Minecraft.getMinecraft().getSession().getUsername());
        return TabCompletionUtil.getListOfStringsMatchingLastWord(args, tabUsernames);
    }

    @Override
    public boolean tabOnly() {
        return true;
    }

    static void addTabHypixel(List<String> tabUsernames) {
        if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            try {
                for (String s : Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser().get().getData().getKeys()) {
                    String name = Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser().get().getData().optJSONObject(s).optString("name");
                    if (!name.isEmpty())
                        tabUsernames.add(name);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
