package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.hud.NetworkInfo;
import cc.hyperium.handlers.handlers.hud.TabCompletionUtil;
import cc.hyperium.handlers.handlers.quests.PlayerQuestsGui;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import net.minecraft.client.Minecraft;

import java.util.List;

public class CommandQuests implements BaseCommand {
    @Override
    public String getName() {
        return "quests";
    }

    @Override
    public String getUsage() {
        return "/quests";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        HypixelApiPlayer player = Hyperium.INSTANCE.getHandlers().getDataHandler().getPlayer();
        System.out.println(player.getData());
        new PlayerQuestsGui(player).show();
    }

}
