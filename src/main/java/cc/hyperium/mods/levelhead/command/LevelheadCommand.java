package cc.hyperium.mods.levelhead.command;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.levelhead.guis.LevelheadGui;

public class LevelheadCommand implements BaseCommand {
    @Override
    public String getName() {
        return "levelhead";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("dumpcache")) {
                Levelhead.getInstance().getDisplayManager().clearCache();
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Cleared Cache");
                return;
            }
        }

        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new LevelheadGui());
    }
}
