package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import net.minecraft.entity.player.EntityPlayer;

public class StatsHandler {

    public void initStatsViewer(EntityPlayer player) {
        Multithreading.runAsync(() -> {
            GeneralChatHandler.instance().sendMessage("Loading stats for: " + player.getName());

            HypixelApiPlayer player1 = null;
            while (player1 == null || !player1.isLoaded()) {
                player1 = Hyperium.INSTANCE.getHandlers().getDataHandler().getPlayer(player.getName());
            }
            if (!player1.isValid()) {
                GeneralChatHandler.instance().sendMessage("Unable to find player: " + player.getName());
                return;
            }
            new PlayerStatsGui(player1).show();
        });
    }
}
