package cc.hyperium.mods.playtime;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.utils.ChatColor;

import java.util.Collections;
import java.util.List;

public class TotalPlayTimeCommand implements BaseCommand {
    PlayTime playTime;

    public TotalPlayTimeCommand(PlayTime playTime) {
        this.playTime = playTime;
    }

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
        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(
            ChatColor.translateAlternateColorCodes('&', "&9[PlayTime] &7Total play time: &f" + PlayTimeUtils.fancyTime(playTime.totalPlayTime)), false
        );
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
