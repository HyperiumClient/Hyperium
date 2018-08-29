package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import club.sk1er.website.api.requests.HypixelApiPlayer;

public class StatsHandler {

    public void initStatsViewer(String player) {
        Multithreading.runAsync(() -> {
            GeneralChatHandler.instance().sendMessage("Loading stats for: " + player);

            HypixelApiPlayer player1 = null;
            while (player1 == null || !player1.isLoaded()) {
                player1 = Hyperium.INSTANCE.getHandlers().getDataHandler().getPlayer(player);
            }
            if (!player1.isValid()) {
                GeneralChatHandler.instance().sendMessage("Unable to find player: " + player);
                return;
            }
            new PlayerStatsGui(player1).show();
        });
    }
}
