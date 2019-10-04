package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.bossbar.BossbarGui;

import java.util.Collections;
import java.util.List;

public class CommandBossbarGui implements BaseCommand {

    @Override
    public String getName() {
        return "bossbar";
    }

    @Override
    public String getUsage() {
        return "/bossbar";
    }

    @Override
    public void onExecute(String[] args) {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new BossbarGui());
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("bb");
    }
}
