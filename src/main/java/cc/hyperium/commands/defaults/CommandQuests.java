package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.quests.PlayerQuestsGui;
import club.sk1er.website.api.requests.HypixelApiPlayer;

import java.util.concurrent.ExecutionException;

public class CommandQuests implements BaseCommand {

    /**
     * Gets the name of the command (text after slash).
     *
     * @return The command name
     */
    @Override
    public String getName() {
        return "quests";
    }

    /**
     * Gets the usage string for the command.
     *
     * @return The command usage
     */
    @Override
    public String getUsage() {
        return "/quests";
    }

    /**
     * Callback when the command is invoked
     */
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
