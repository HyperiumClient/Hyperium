package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.quests.PlayerQuestsGui;
import club.sk1er.website.api.requests.HypixelApiPlayer;

import java.util.concurrent.ExecutionException;

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
        HypixelApiPlayer player = null;
        try {
            player = Hyperium.INSTANCE.getHandlers().getDataHandler().getCurrentUser().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        new PlayerQuestsGui(player).show();
    }

}
