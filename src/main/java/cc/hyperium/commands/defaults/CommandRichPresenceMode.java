package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.config.Settings;
import cc.hyperium.mods.discord.CustomRichPresenceUpdater;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRichPresenceMode implements BaseCommand {
    @Override
    public String getName() {
        return "richpresence";
    }

    @Override
    public String getUsage() {
        return "/richpresence [mode]";
    }

    @Override
    public void onExecute(String[] args) {
        try {
            Settings.CUSTOM_RP_MODE = args[0];

            CustomRichPresenceUpdater.discordRPupdate();
            Hyperium.INSTANCE.getNotification().display("CustomRP", "Mode set to\n    " + Settings.CUSTOM_RP_MODE, 2F, null, null, Color.CYAN);
        } catch (Exception exception) {
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(getUsage());
        }
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("rp");
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        ArrayList<String> tabCompleteList = new ArrayList<> ();

        tabCompleteList.add("reset");
        tabCompleteList.add("eVowels");
        tabCompleteList.add("eAll");
        tabCompleteList.add("sellout");
        tabCompleteList.add("bestCoder");
        tabCompleteList.add("merch");
        tabCompleteList.add("respects");
        tabCompleteList.add("sleepy");

        return tabCompleteList;
    }
}
