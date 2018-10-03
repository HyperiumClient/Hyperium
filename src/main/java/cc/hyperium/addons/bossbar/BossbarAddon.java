package cc.hyperium.addons.bossbar;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.addons.bossbar.commands.CommandBossbar;
import cc.hyperium.addons.bossbar.config.BossbarConfig;
import cc.hyperium.addons.bossbar.gui.GuiBossbarSetting;

public class BossbarAddon extends AbstractAddon {
    private BossbarConfig bossbarConfig;
    private GuiBossbarSetting guiBossBarSetting;
    private CommandBossbar commandBossbar;

    public GuiBossbarSetting getGuiBossBarSetting() {
        return guiBossBarSetting;
    }

    public CommandBossbar getCommandBossbar() {
        return commandBossbar;
    }

    @Override
    public AbstractAddon init() {
        bossbarConfig = new BossbarConfig();
        guiBossBarSetting = new GuiBossbarSetting(this);
        commandBossbar = new CommandBossbar(this);
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(commandBossbar);
        return this;
    }

    public BossbarConfig getConfig() {
        return bossbarConfig;
    }

    @Override
    public Metadata getAddonMetadata() {
        AbstractAddon.Metadata metadata = new AbstractAddon.Metadata(this, "BossbarAddon", "1.0", "SiroQ");
        metadata.setDescription("Allows for full bossbar customization");
        return metadata;
    }
}
