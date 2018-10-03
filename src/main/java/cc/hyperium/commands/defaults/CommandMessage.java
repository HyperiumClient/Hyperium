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
    @Override
    public String getName() {
        return "msg";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("msg", "tell", "w", "message");
    }

    @Override
    public String getUsage() {
        return "/msg <name> <msg>";
    }

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
