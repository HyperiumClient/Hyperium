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
    @Override
    public String getName() {
        return "party";
    }

    @Override
    public String getUsage() {
        return "/party <command>";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(" ").append(args[i]);
        }
        Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/party" + builder.toString());
    }

    @Override
    public boolean tabOnly() {
        return true;
    }

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

}
