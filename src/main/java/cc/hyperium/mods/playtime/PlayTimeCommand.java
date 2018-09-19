package cc.hyperium.mods.playtime;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.utils.ChatColor;

import java.util.Collections;
import java.util.List;

public class PlayTimeCommand implements BaseCommand {
    PlayTime playTime;

    public PlayTimeCommand(PlayTime playTime) {
        this.playTime = playTime;
    }

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
        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(
            ChatColor.translateAlternateColorCodes('&', "&9[PlayTime] &7You have played for: &f" + PlayTimeUtils.fancyTime(playTime.sessionPlayTime)), false
        );
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
