package cc.hyperium.addons.bossbar.commands;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.commands.BaseCommand;

public class CommandBossbar implements BaseCommand {
    private BossbarAddon addon;

    public CommandBossbar(BossbarAddon addon) {
        this.addon = addon;
    }

    @Override
    public String getName() {
        return "bossbaraddon";
    }

    @Override
    public String getUsage() {
        return "/bossbaraddon";
    }

    @Override
    public void onExecute(String[] args) {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(addon.getGuiBossBarSetting());
    }
}
