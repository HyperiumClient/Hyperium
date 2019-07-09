package cc.hyperium.mods.levelhead.command;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.levelhead.guis.CustomLevelheadConfigurer;

public class CustomLevelheadCommand implements BaseCommand {
    @Override
    public String getName() {
        return "customlevelhead";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void onExecute(String[] args) {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new CustomLevelheadConfigurer());
    }
}
