package cc.hyperium.mods.playtime;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.utils.ChatColor;
import java.util.Collections;
import java.util.List;

public class TotalPlayTimeCommand implements BaseCommand {
    @Override
    public String getName() {
        return "totalplaytime";
    }

    @Override
    public String getUsage() {
        return "/totalplaytime";
    }

    @Override
    public void onExecute(String[] args) {
        String messageToSend = ChatColor.translateAlternateColorCodes('&', "&9[PlayTime] &7Total play time: &f");

        if (PlayTime.totalPlayTime / 86400 != 0) {
            messageToSend = messageToSend.concat(String.valueOf((PlayTime.totalPlayTime / 86400))).concat("d ");
        }

        if (PlayTime.totalPlayTime / 3600 != 0) {
            messageToSend = messageToSend.concat(String.valueOf((PlayTime.totalPlayTime / 3600) - ((PlayTime.totalPlayTime / 86400) * 24))).concat("h ");
        }

        if (PlayTime.totalPlayTime / 60 != 0) {
            messageToSend = messageToSend.concat(String.valueOf((PlayTime.totalPlayTime / 60) - ((PlayTime.totalPlayTime / 3600) * 60))).concat("m ");
        }

        if (PlayTime.totalPlayTime != 0) {
            messageToSend = messageToSend.concat(String.valueOf((PlayTime.totalPlayTime) - ((PlayTime.totalPlayTime / 60) * 60))).concat("s ");
        }

        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(messageToSend, false);
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("tpt");
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return Collections.singletonList("totalplaytime");
    }
}
