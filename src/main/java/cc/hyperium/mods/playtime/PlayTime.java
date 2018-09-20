package cc.hyperium.mods.playtime;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.AbstractMod;

public class PlayTime extends AbstractMod {
    long startSysTime;
    long startConfigTime;
    long sessionPlayTime;
    long totalPlayTime;

    @Override
    public AbstractMod init() {
        startConfigTime = Settings.TOTAL_PLAYTIME;
        EventBus.INSTANCE.register(this);
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new PlayTimeCommand(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new TotalPlayTimeCommand(this));
        startSysTime = System.currentTimeMillis();
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Play Time", "1.0", "SHARDCoder");
    }

    @InvokeEvent
    public void tickEvent(TickEvent event) {
        Settings.TOTAL_PLAYTIME = startConfigTime + (System.currentTimeMillis() - startSysTime);
        sessionPlayTime = (System.currentTimeMillis() - startSysTime) / 1000;
        totalPlayTime = Settings.TOTAL_PLAYTIME / 1000;
    }
}
