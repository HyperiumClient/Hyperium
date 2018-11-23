package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.hud.TabCompletionUtil;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParty implements BaseCommand {

    /**
     * Gets the name of the command (text after slash).
     *
     * @return The command name
     */
    @Override
    public String getName() {
        return "party";
    }

    /**
     * Gets the usage string for the command.
     *
     * @return The command usage
     */
    @Override
    public String getUsage() {
        return "/party <command>";
    }

    /**
     * Callback when the command is invoked
     */
    @Override
    public void onExecute(String[] args) throws CommandException {
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(" ").append(arg);
        }
        Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/party" + builder.toString());
    }

    /**
     * Called when the player clicks tab in the chat menu, used to provide suggestions for a commands arguments
     *
     * @param args the arguments the player has entered
     * @return a String List containing all viable tab completions
     */
    @Override
    public List<String> onTabComplete(String[] args) {
        if (!Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel())
            return new ArrayList<>();
        List<String> first = Arrays.asList("invite", "leave", "promote", "home", "remove", "warp", "accept", "disband", "settings", "mute", "poll", "challenge", "kickoffline", "private");
        List<String> tabUsernames = TabCompletionUtil.getTabUsernames();
        List<String> complete = new ArrayList<>();
        try {
            for (String s : Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser().get().getData().getKeys()) {
                String name = Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser().get().getData().optJSONObject(s).optString("name");
                if (!name.isEmpty())
                    tabUsernames.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        complete.addAll(first);
        complete.addAll(tabUsernames);
        tabUsernames.remove(Minecraft.getMinecraft().getSession().getUsername());
        return TabCompletionUtil.getListOfStringsMatchingLastWord(args, complete);
    }

    /**
     * Tells the command handler not to register the command, and to use {@link #onTabComplete(String[])}
     *
     * @return {@code true} if this command should not be executed
     */
    @Override
    public boolean tabOnly() {
        return true;
    }

}
