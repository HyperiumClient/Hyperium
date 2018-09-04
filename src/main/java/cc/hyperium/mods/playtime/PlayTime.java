package cc.hyperium.mods.playtime;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.AbstractMod;

public class PlayTime extends AbstractMod {
    static long startSysTime;
    static long startConfigTime;
    static long sessionPlayTime;
    static long totalPlayTime;

    @Override
    public AbstractMod init() {
        startConfigTime = Settings.TOTAL_PLAYTIME;
        EventBus.INSTANCE.register(this);
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new PlayTimeCommand());
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new TotalPlayTimeCommand());
        startSysTime = System.currentTimeMillis();
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Play Time", "1.0", "SHARDCoder");
    }

    @InvokeEvent
    public void tickEvent(TickEvent event) {
        Settings.TOTAL_PLAYTIME = startConfigTime + (System.currentTimeMillis() - PlayTime.startSysTime);
        sessionPlayTime = (System.currentTimeMillis() - PlayTime.startSysTime) / 1000;
        totalPlayTime = Settings.TOTAL_PLAYTIME / 1000;
    }
}
