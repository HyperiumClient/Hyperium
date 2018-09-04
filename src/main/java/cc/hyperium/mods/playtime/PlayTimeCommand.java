package cc.hyperium.mods.playtime;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.utils.ChatColor;

import java.util.Collections;
import java.util.List;

public class PlayTimeCommand implements BaseCommand {
    @Override
    public String getName() {
        return "playtime";
    }

    @Override
    public String getUsage() {
        return "/playtime";
    }

    @Override
    public void onExecute(String[] args) {
        String messageToSend = ChatColor.translateAlternateColorCodes('&', "&9[PlayTime] &7You have played for: &f");

        if (PlayTime.sessionPlayTime / 86400 != 0) {
            messageToSend = messageToSend.concat(String.valueOf((PlayTime.sessionPlayTime / 86400))).concat("d ");
        }

        if (PlayTime.sessionPlayTime / 3600 != 0) {
            messageToSend = messageToSend.concat(String.valueOf((PlayTime.sessionPlayTime / 3600) - ((PlayTime.sessionPlayTime / 86400) * 24))).concat("h ");
        }

        if (PlayTime.sessionPlayTime / 60 != 0) {
            messageToSend = messageToSend.concat(String.valueOf((PlayTime.sessionPlayTime / 60) - ((PlayTime.sessionPlayTime / 3600) * 60))).concat("m ");
        }

        if (PlayTime.sessionPlayTime != 0) {
            messageToSend = messageToSend.concat(String.valueOf((PlayTime.sessionPlayTime) - ((PlayTime.sessionPlayTime / 60) * 60))).concat("s ");
        }

        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(messageToSend, false);
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("mpt");
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return Collections.singletonList("playtime");
    }
}
